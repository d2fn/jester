package com.d2fn.jester.rewrite;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.io.ByteStreams;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractImageRewriter implements Rewriter {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractImageRewriter.class);
    private final Pattern linkPattern;
    private final Pattern embeddedLinkPattern;
    private final HttpClient http;

    public AbstractImageRewriter(Pattern linkPattern, Pattern embeddedLinkPattern, HttpClient http) {
        this.linkPattern = checkNotNull(linkPattern);
        this.embeddedLinkPattern = checkNotNull(embeddedLinkPattern);
        this.http = checkNotNull(http);
    }

    @Override
    public Optional<String> rewrite(String input) throws Exception {
        if (linkPattern.matcher(input).matches()) {
            final String imageUrl = findEmbeddedImageUrl(input);
            return Optional.fromNullable(imageUrl);
        }
        return Optional.absent();
    }

    private String findEmbeddedImageUrl(String link) throws Exception {
        if (!link.startsWith("http://")) {
            link = "http://" + link;
        }
        int retries = 3;
        while (retries > 0) {
            HttpGet get = new HttpGet(link);
            try {
                HttpResponse httpResponse = http.execute(get);
                InputStream in = httpResponse.getEntity().getContent();
                try {
                    if (in == null) {
                        throw new Exception("Could not get remote link: " + link);
                    }
                    final byte[] bytes = ByteStreams.toByteArray(in);
                    final String content = new String(bytes, Charsets.UTF_8);
                    return findEmbeddedImageUrlInContent(content);
                } finally {
                    if (in != null) {
                        in.close();
                    }
                }
            } catch (Exception e) {
                retries--;
                if (retries > 0) {
                    LOG.error("Could not fetch {}, retrying...", link);
                }
            }
        }
        return null;
    }

    private String findEmbeddedImageUrlInContent(String content) {
        final Matcher matcher = embeddedLinkPattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }


}
