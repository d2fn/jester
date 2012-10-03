package com.d2fn.jester.rewrite;

import com.google.common.base.Optional;
import com.google.common.base.Strings;

import javax.ws.rs.core.HttpHeaders;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TcoRewriter implements Rewriter {
    private static final Pattern LINK_PATTERN = Pattern.compile("(http://)?(www\\.)?t.co/[^\\s]+");

    @Override
    public Optional<String> rewrite(String input) throws Exception {
        final Matcher matcher = LINK_PATTERN.matcher(input);
        if (matcher.matches()) {
            final String link = findRedirect(matcher.group());
            return Optional.fromNullable(link);
        }
        return Optional.absent();
    }

    private String findRedirect(String link) throws Exception {
        if (!link.startsWith("http://")) {
            link = "http://" + link;
        }
        final URL url = new URL(link);
        final HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setInstanceFollowRedirects(false);

        if (connection.getResponseCode() == 301) {
            final String location = connection.getHeaderField(HttpHeaders.LOCATION);
            if (!Strings.isNullOrEmpty(location)) {
                return location;
            }
        }
        return null;
    }

}
