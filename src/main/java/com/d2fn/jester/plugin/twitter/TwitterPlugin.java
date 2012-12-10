package com.d2fn.jester.plugin.twitter;

import com.d2fn.jester.bot.JesterBot;
import com.d2fn.jester.plugin.Message;
import com.d2fn.jester.plugin.Plugin;
import com.d2fn.jester.plugin.twitter.response.Tweet;
import com.d2fn.jester.plugin.twitter.response.TwitterUser;
import com.d2fn.jester.rewrite.Rewriter;
import com.d2fn.jester.rewrite.SentenceRewriter;
import com.google.common.base.Optional;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

public class TwitterPlugin implements Plugin {
    private static final Logger log = LoggerFactory.getLogger(TwitterPlugin.class);
    private TwitterClient twitter;
    private final Rewriter rewriter;
    
    public TwitterPlugin(HttpClient httpClient, SentenceRewriter sentenceRewriter) {
        twitter = new TwitterClient(httpClient);
        this.rewriter = checkNotNull(sentenceRewriter);
    }

    public String getName() {
        return "twitter";
    }

    public void call(JesterBot bot, Message msg) throws Exception {
        if(msg.isCommand("sup")) {
            
        }
        else if(msg.isCommand("describe") && msg.hasArguments()) {
            try {
                String screenName = msg.getArguments().get(0);
                TwitterUser user = twitter.lookupUser(screenName);
                String response = user.getName() + ": \"" + user.getDescription() + "\" " + user.getProfilePicUrl();
                bot.sendMessage(msg.getChannel(), rewriteResponse(response));
            }
            catch(Exception e) {
                log.error("error looking up user", e);
                bot.sendMessage(msg.getChannel(), "¯\\_(ツ)_/¯");
            }
        }
        else {
            for(String word : msg.getWords()) {
                if(twitter.isTweet(word)) {
                    try {
                        Tweet tweet = twitter.getTweet(word);
                        if(tweet != null) {
                            String response = tweet.getUser().getScreenName() + ": " + tweet.getText();
                            bot.sendMessage(msg.getChannel(), rewriteResponse(response));
                        }
                    }
                    catch(Exception e) {
                        log.error("error retrieving timeline", e);
                        bot.sendMessage(msg.getChannel(), "¯\\_(ツ)_/¯");
                    }
                }
            }
        }
    }

    private String rewriteResponse(String response) throws Exception {
        final Optional<String> optional = rewriter.rewrite(response);
        if (optional.isPresent()) {
            return optional.get();
        }
        return response;
    }

}
