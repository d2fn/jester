package com.d2fn.jester.plugin.gis;

import com.d2fn.jester.bot.JesterBot;
import com.d2fn.jester.plugin.Message;
import com.d2fn.jester.plugin.Plugin;
import com.yammer.dropwizard.logging.Log;
import org.apache.http.client.HttpClient;

import java.util.List;

/**
 * GoogleImageSearchPlugin
 * @author Dietrich Featherston
 */
public class GoogleImageSearchPlugin implements Plugin {

    private static final Log log = Log.forClass(GoogleImageSearchPlugin.class);

    private GisClient gis;
    
    public String getName() {
        return "google-image-search";
    }
    
    public GoogleImageSearchPlugin(HttpClient httpClient) {
        gis = new GisClient(httpClient);
    }

    @Override
    public void call(JesterBot bot, Message msg) throws Exception {
        if(msg.isCommand("gis") && msg.hasArguments()) {
            String q = processArguments(msg.getArguments());
            log.info("running google image search for '{}'", q);
            String url = gis.search(q);
            if(url != null) {
                log.info("gis for '{}' returned {}", q, url);
                String response = msg.getSender() + ": ftfy -> " + url;
                bot.sendMessage(msg.getChannel(), response);
            }
        }
    }
    
    private String processArguments(List<String> args) {
        StringBuilder sb = new StringBuilder();
        for(String arg : args) {
            sb.append(arg).append(" ");
        }
        return sb.toString();
    }
}
