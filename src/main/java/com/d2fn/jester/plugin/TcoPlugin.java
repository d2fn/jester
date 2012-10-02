package com.d2fn.jester.plugin;

import com.d2fn.jester.bot.JesterBot;
import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import javax.ws.rs.core.HttpHeaders;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;

public class TcoPlugin implements Plugin {
    private static final Pattern LINK_PATTERN = Pattern.compile("(http://)?(www\\.)?t.co/[^\\s]+");
    private final HttpClient http;
    private final Collection<Plugin> delegates;

    public TcoPlugin(HttpClient http, Collection<Plugin> delegates) {
        this.http = checkNotNull(http);
        this.delegates = checkNotNull(delegates);
    }

    @Override
    public String getName() {
        return "t.co";
    }

    @Override
    public void call(JesterBot bot, Message msg) throws Exception {
        final Matcher matcher = LINK_PATTERN.matcher(msg.getMessage());
        final List<String> links = Lists.newArrayList();
        while (matcher.find()) {
            final String link = matcher.group();
            final String redirectedLink = findRedirect(link);
            if (redirectedLink != null) {
                links.add(redirectedLink);
            }
        }
        if (!links.isEmpty()) {
            Message newMessage = new Message(
                    msg.getChannel(),
                    msg.getSender(),
                    msg.getLogin(),
                    msg.getHostname(),
                    Joiner.on(" ").join(links));
            for (Plugin delegate : delegates) {
                if (delegate != this) {
                    delegate.call(bot, newMessage);
                }
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
