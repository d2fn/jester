package com.d2fn.jester.plugin;

import com.d2fn.jester.bot.JesterBot;

import java.util.List;

/**
 * RecallPlugin
 * @author Dietrich Featherston
 */
public class RecallPlugin implements Plugin {

    @Override
    public String getName() {
        return "recall";
    }

    @Override
    public void call(JesterBot bot, Message msg) throws Exception {
        List<String> words = msg.getWords();
        if(words.size() == 2) {
            String symbol = words.get(0);
            String url = words.get(1);
            if(url.startsWith("http://")) {

            }
        }
    }
}
