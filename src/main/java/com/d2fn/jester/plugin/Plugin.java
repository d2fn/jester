package com.d2fn.jester.plugin;

import com.d2fn.jester.bot.JesterBot;

/**
 * Plugin
 * @author Dietrich Featherston
 */
public interface Plugin {
    public String getName();
    public void call(JesterBot bot, Message msg) throws Exception;
}
