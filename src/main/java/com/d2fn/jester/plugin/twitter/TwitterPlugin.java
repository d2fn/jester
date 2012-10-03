package com.d2fn.jester.plugin.twitter;

import com.d2fn.jester.bot.JesterBot;
import com.d2fn.jester.plugin.Message;
import com.d2fn.jester.plugin.Plugin;
import com.d2fn.jester.plugin.twitter.response.Tweet;
import com.d2fn.jester.plugin.twitter.response.TwitterUser;
import com.d2fn.jester.rewrite.Rewriter;
import com.d2fn.jester.rewrite.SentenceRewriter;
import com.google.common.base.Optional;
import com.yammer.dropwizard.logging.Log;
import org.apache.http.client.HttpClient;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * TwitterPlugin
 * @author Dietrich Featherston
 */
public class TwitterPlugin implements Plugin {
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
                log.error(e, "error looking up user");
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
                        log.error(e, "error retrieving timeline");
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

    private static final Log log = Log.forClass(TwitterPlugin.class);
}
