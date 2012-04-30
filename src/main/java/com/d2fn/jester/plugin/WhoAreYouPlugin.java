package com.d2fn.jester.plugin;

import com.d2fn.jester.bot.JesterBot;

public class WhoAreYouPlugin implements Plugin {

    @Override
    public String getName() {
        return "who-are-you";
    }

    @Override
    public void call(JesterBot bot, Message msg) throws Exception {
        if(msg.mentions(bot.getName())) {
            bot.sendMessage(msg.getChannel(), msg.getSender() + ": i am halping");
        }
    }
}
