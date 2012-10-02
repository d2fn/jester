package com.d2fn.jester.plugin.tco;

import com.d2fn.jester.bot.JesterBot;
import com.d2fn.jester.plugin.Message;
import com.d2fn.jester.plugin.Plugin;
import com.google.common.base.Strings;

import javax.ws.rs.core.HttpHeaders;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TcoPlugin implements Plugin {
    private static final Pattern LINK_PATTERN = Pattern.compile("(http://)?(www\\.)?t.co/[^\\s]+");

    public TcoPlugin() {
    }

    @Override
    public String getName() {
        return "t.co";
    }

    @Override
    public void call(JesterBot bot, Message msg) throws Exception {
        final Matcher matcher = LINK_PATTERN.matcher(msg.getMessage());
        while (matcher.find()) {
            final String link = matcher.group();
            final String redirectedLink = findRedirect(link);
            if (redirectedLink != null) {
                String response = msg.getSender() + ": ftfy -> " + redirectedLink;
                bot.sendAndReprocessMessage(msg.getChannel(), response, msg);
            }
        }
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
