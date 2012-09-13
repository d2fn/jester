package com.d2fn.jester.plugin.cloudapp;

import com.d2fn.jester.bot.JesterBot;
import com.d2fn.jester.plugin.Message;
import com.d2fn.jester.plugin.Plugin;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;
import com.yammer.dropwizard.client.HttpClientConfiguration;
import com.yammer.dropwizard.client.HttpClientFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CloudappPlugin implements Plugin {
    private static final Pattern LINK_RE = Pattern.compile("http://cl\\.ly[\\w_\\d-//]+");
    private static final Pattern EMBEDDED_LINK_RE = Pattern.compile("a class=\"embed\".*(http://cl\\.ly[^\"]+)", Pattern.MULTILINE);
    private final HttpClient http;

    public static void main(String[] args) throws Exception {
        String url = "http://cl.ly/image/0E35473n2k0E";
        final HttpClientConfiguration config = new HttpClientConfiguration();
        HttpClient client = new HttpClientFactory(config).build();
        final CloudappPlugin plugin = new CloudappPlugin(client);
        final String imageUrl = plugin.findEmbeddedImageUrl(url);
        System.out.println("imageUrl = " + imageUrl);
    }

    public CloudappPlugin(HttpClient http) {
        this.http = http;
    }

    @Override
    public String getName() {
        return "Cloudapp";
    }

    @Override
    public void call(JesterBot bot, Message msg) throws Exception {
        final Matcher matcher = LINK_RE.matcher(msg.getMessage());
        while (matcher.find()) {
            final String link = matcher.group();
            final String imageUrl = findEmbeddedImageUrl(link);
            if (!Strings.isNullOrEmpty(imageUrl)) {
                String response = msg.getSender() + ": ftfy -> " + imageUrl;
                bot.sendMessage(msg.getChannel(), response);
            }
        }
    }

    String findEmbeddedImageUrl(String link) throws Exception {
        int retries = 3;
        while (retries > 0) {
            HttpGet get = new HttpGet(link);
            HttpResponse httpResponse = http.execute(get);
            InputStream in = httpResponse.getEntity().getContent();
            try {
                if (in == null) {
                    throw new Exception("Could not get remote link");
                }
                final byte[] bytes = ByteStreams.toByteArray(in);
                final String content = new String(bytes, Charsets.UTF_8);
                return findEmbeddedImageUrlInContent(content);
            } catch (Exception e) {
                retries--;
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    // <a class="embed" href="http://cl.ly/image/352H3C1b3f1K/Screen%20Shot%202012-09-12%20at%201.37.00%20PM.png">Direct link</a>
    private String findEmbeddedImageUrlInContent(String content) {
        final Matcher matcher = EMBEDDED_LINK_RE.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
