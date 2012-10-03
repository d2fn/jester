package com.d2fn.jester.plugin;

import com.d2fn.jester.bot.JesterBot;
import com.d2fn.jester.rewrite.Rewriter;
import com.google.common.base.Optional;

public class RewritingPlugin implements Plugin {
    private final Rewriter rewriter;

    public RewritingPlugin(Rewriter rewriter) {
        this.rewriter = rewriter;
    }

    @Override
    public String getName() {
        return "Rewriter";
    }

    @Override
    public void call(JesterBot bot, Message msg) throws Exception {
        for (String string : msg.getMessage().split(" ")) {
            final Optional<String> optional = rewriter.rewrite(string);
            if (optional.isPresent()) {
                String response = msg.getSender() + ": ftfy -> " + optional.get();
                bot.sendMessage(msg.getChannel(), response);
            }
        }
    }
}
