package com.d2fn.jester.plugin.gis;

import com.d2fn.jester.bot.JesterBot;
import com.d2fn.jester.plugin.Message;
import com.d2fn.jester.plugin.Plugin;
import com.google.common.base.Joiner;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;

public class GoogleImageSearchPlugin implements Plugin {
    private static final Logger log = LoggerFactory.getLogger(GoogleImageSearchPlugin.class);
    private GisClient gis;
    private final SecureRandom random = new SecureRandom();

    public GoogleImageSearchPlugin(HttpClient httpClient) {
        gis = new GisClient(httpClient);
    }

    public String getName() {
        return "google-image-search";
    }
    
    @Override
    public void call(JesterBot bot, Message msg) throws Exception {
        if(shouldRandomlySearch(msg.getChannel()) || (msg.isCommand("gis") && msg.hasArguments())) {
            String query = Joiner.on(" ").join(msg.getArguments());
            log.info("running google image search for '{}'", query);
            String url = gis.search(query);
            if(url != null) {
                log.info("gis for '{}' returned {}", query, url);
                String response = msg.getSender() + ": ftfy -> " + url;
                bot.sendMessage(msg.getChannel(), response);
            }
        }
    }

    private boolean shouldRandomlySearch(String channel) {
        return 0 == random.nextInt(500) && channel.equals("kingshit");
    }

}
