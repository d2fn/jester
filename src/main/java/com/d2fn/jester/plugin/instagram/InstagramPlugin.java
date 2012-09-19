package com.d2fn.jester.plugin.instagram;

import com.d2fn.jester.bot.JesterBot;
import com.d2fn.jester.plugin.Message;
import com.d2fn.jester.plugin.Plugin;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InstagramPlugin implements Plugin {
    private static final Pattern LINK_RE = Pattern.compile("http://(wwww\\.)?instagram[^\\\\s]+");
    private static final Pattern EMBEDDED_LINK_RE = Pattern.compile("img class=\"photo\".*(http://[^\"]+)", Pattern.MULTILINE);
    private final HttpClient http;

    public static void main(String[] args) {
        String text = "some text http://instagram.com/p/PxgNNlEKHA.html some other link";
        final Matcher matcher = LINK_RE.matcher(text);
        while (matcher.find()) {
            System.out.println("matcher.group() = " + matcher.group());
        }
    }

    public InstagramPlugin(HttpClient http) {
        this.http = http;
    }

    @Override
    public String getName() {
        return "instagram";
    }

    @Override
    public void call(JesterBot bot, Message msg) throws Exception {
        final Matcher matcher = LINK_RE.matcher(msg.getMessage());
        while (matcher.find()) {
            String link = matcher.group();
            String imageUrl = findImageUrl(link);
            if (!Strings.isNullOrEmpty(imageUrl)) {
                String response = msg.getSender() + ": ftfy -> " + imageUrl;
                bot.sendMessage(msg.getChannel(), response);
            }
        }
    }

    private String findImageUrl(String link) throws Exception {
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

    private String findEmbeddedImageUrlInContent(String content) throws Exception {
        final Matcher matcher = EMBEDDED_LINK_RE.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
