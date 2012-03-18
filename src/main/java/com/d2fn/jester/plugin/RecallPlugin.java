package com.d2fn.jester.plugin;

import com.d2fn.jester.bdb.BdbEnvironment;
import com.d2fn.jester.bdb.binding.SavedLink;
import com.d2fn.jester.bdb.binding.StringKey;
import com.d2fn.jester.bot.JesterBot;
import com.sleepycat.collections.StoredMap;
import org.apache.http.client.HttpClient;

import java.util.List;

/**
 * RecallPlugin
 * @author Dietrich Featherston
 */
public class RecallPlugin implements Plugin {

    private BdbEnvironment bdb;
    private HttpClient httpClient;

    public RecallPlugin(BdbEnvironment bdb, HttpClient httpClient) {
        this.bdb = bdb;
        this.httpClient = httpClient;
    }

    @Override
    public String getName() {
        return "recall";
    }

    @Override
    public void call(JesterBot bot, Message msg) throws Exception {
        if(msg.isCommand("recall") && msg.hasArguments()) {
            SavedLink link = recall(msg.getArguments().get(0));
            bot.sendMessage(msg.getChannel(), link.getUrl());
        }
        else {
            List<String> words = msg.getWords();
            if(words.size() == 2) {
                String symbol = words.get(0);
                String url = words.get(1);
                if(url.startsWith("http://")) {
                    saveLink(symbol, url, msg);
                }
            }
        }
    }

    private void saveLink(String symbol, String url, Message msg) {
        StoredMap map = bdb.open("recall", StringKey.class, SavedLink.class);
        map.put(new StringKey(symbol), new SavedLink(url, msg));
    }
    
    private SavedLink recall(String symbol) {
        StoredMap map = bdb.open("recall", StringKey.class, SavedLink.class);
        return (SavedLink)map.get(new StringKey(symbol));
    }
}
