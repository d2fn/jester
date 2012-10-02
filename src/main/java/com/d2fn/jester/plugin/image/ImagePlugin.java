package com.d2fn.jester.plugin.image;

import com.d2fn.jester.bot.JesterBot;
import com.d2fn.jester.plugin.Message;
import com.d2fn.jester.plugin.Plugin;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.regex.Pattern.compile;

/**
 * A {@link com.d2fn.jester.plugin.Plugin} that searches for embedded image links in remote resources.
 *
 * A {@link Pattern} is supplied to detect links from the chat channel. Another Pattern is
 * supplied to find embedded links in the content of the remote URLs.  Any found embedded link
 * is then sent back into the chat channel.
 */
public class ImagePlugin implements Plugin {
    private static final Logger LOG = LoggerFactory.getLogger(ImagePlugin.class);
    public static Pattern INSTAGRAM_LINK = compile("http://(www\\.)?instagram[^\\s]+");
    public static Pattern INSTAGRAM_EMBEDDED_LINK = compile("img class=\"photo\".*(http://[^\"]+)", Pattern.MULTILINE);
    public static Pattern CLOUDAPP_LINK = compile("http://(www\\.)?cl\\.ly[^\\s]+");
    public static Pattern CLOUDAPP_EMBEDDED_LINK = compile("a class=\"embed\".*(http://cl\\.ly[^\"]+)", Pattern.MULTILINE);

    private final String name;
    private final Pattern linkPattern;
    private final Pattern embeddedLinkPattern;
    private final HttpClient http;

    ImagePlugin(String name, Pattern linkPattern, Pattern embeddedLinkPattern, HttpClient http) {
        this.name = checkNotNull(name);
        this.linkPattern = checkNotNull(linkPattern);
        this.embeddedLinkPattern = checkNotNull(embeddedLinkPattern);
        this.http = checkNotNull(http);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void call(JesterBot bot, Message msg) throws Exception {
        final Matcher matcher = linkPattern.matcher(msg.getMessage());
        while (matcher.find()) {
            final String link = matcher.group();
            final String imageUrl = findEmbeddedImageUrl(link);
            if (!Strings.isNullOrEmpty(imageUrl)) {
                String response = msg.getSender() + ": ftfy -> " + imageUrl;
                bot.sendMessage(msg.getChannel(), response);
            }
        }
    }

    private String findEmbeddedImageUrl(String link) throws Exception {
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
