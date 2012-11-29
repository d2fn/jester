package com.d2fn.jester.plugin.zerocater;

import com.d2fn.jester.bot.JesterBot;
import com.d2fn.jester.plugin.Message;
import com.d2fn.jester.plugin.Plugin;

public class ZeroCaterPlugin implements Plugin {
    private final String username;
    private final String password;

    public ZeroCaterPlugin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String getName() {
        return "zerocater";
    }

    @Override
    public void call(JesterBot bot, Message msg) throws Exception {
        if ("lunch".equals(msg.getMessage())) {
            bot.sendMessage(msg.getChannel(), msg.getSender() + ": scrape scrape scrape");
            bot.sendMessage(msg.getChannel(), msg.getSender() + ": http://www.lolroflmao.com/wp-content/uploads/2011/07/i-cant-see-shit.jpg");
        }
    }
}
