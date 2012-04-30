package com.d2fn.jester.plugin.twitter;

import com.d2fn.jester.bot.JesterBot;
import com.d2fn.jester.plugin.Message;
import com.d2fn.jester.plugin.Plugin;
import com.d2fn.jester.plugin.twitter.response.Tweet;
import com.d2fn.jester.plugin.twitter.response.TwitterUser;
import com.yammer.dropwizard.logging.Log;
import org.apache.http.client.HttpClient;

/**
 * TwitterPlugin
 * @author Dietrich Featherston
 */
public class TwitterPlugin implements Plugin {

    private TwitterClient twitter;
    
    public TwitterPlugin(HttpClient httpClient) {
        twitter = new TwitterClient(httpClient);
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
                bot.sendMessage(msg.getChannel(), response);
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
                            bot.sendMessage(msg.getChannel(), response);
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
    
    private static final Log log = Log.forClass(TwitterPlugin.class);
}
