package com.d2fn.jester;

import com.d2fn.jester.bdb.BdbEnvironment;
import com.d2fn.jester.bot.JesterBot;
import com.d2fn.jester.config.JesterConfiguration;
import com.d2fn.jester.plugin.Plugin;
import com.d2fn.jester.plugin.RecallPlugin;
import com.d2fn.jester.plugin.WhoAreYouPlugin;
import com.d2fn.jester.plugin.gis.GoogleImageSearchPlugin;
import com.d2fn.jester.plugin.RewritingPlugin;
import com.d2fn.jester.plugin.twitter.TwitterPlugin;
import com.d2fn.jester.plugin.zerocater.ZeroCaterPlugin;
import com.d2fn.jester.rewrite.*;
import com.google.common.collect.Lists;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.client.HttpClientFactory;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.logging.Log;
import org.apache.http.client.HttpClient;

import java.util.Collection;

import static java.util.Arrays.asList;

/**
 * Jester
 * @author Dietrich Featherston
 */
public class Jester extends Service<JesterConfiguration> {
    
    private static final Log log = Log.forClass(Jester.class);
    
    public static void main(String[] args) throws Exception {
        new Jester().run(args);
    }

    private Jester() {
        super("jester");
    }

    @Override
    public void initialize(JesterConfiguration config, Environment environment) {
        log.info("\n\n{}\n", config.getProse());
        HttpClient httpClient = new HttpClientFactory(config.getHttpClientConfiguration()).build();
        BdbEnvironment bdbEnv = new BdbEnvironment(config.getBdbConfiguration());

        final CompositeRewriter compositeRewriter = buildCompositeRewriter(httpClient);
        final SentenceRewriter sentenceRewriter = new SentenceRewriter(compositeRewriter);

        final Collection<Plugin> plugins = Lists.newArrayList();
        plugins.add(new WhoAreYouPlugin());
        plugins.add(new GoogleImageSearchPlugin(httpClient));
        plugins.add(new TwitterPlugin(httpClient, sentenceRewriter));
        plugins.add(new RecallPlugin(bdbEnv, httpClient));
        plugins.add(new RewritingPlugin(compositeRewriter));

		JesterConfiguration.ZerocaterConfiguration zcConfig = config.getZerocater();
        plugins.add(new ZeroCaterPlugin(zcConfig.getUsername(), zcConfig.getPassword()));

        JesterBot bot = new JesterBot(config.getBot(), plugins);
        environment.manage(bot);
        environment.manage(bdbEnv);
    }

    private CompositeRewriter buildCompositeRewriter(HttpClient httpClient) {
        final CloudappRewriter cloudappRewriter = new CloudappRewriter(httpClient);
        final InstagramRewriter instagramRewriter = new InstagramRewriter(httpClient);
        final TwitterImageRewriter twitterImageRewriter = new TwitterImageRewriter(httpClient);
        final TcoRewriter tcoRewriter = new TcoRewriter();
        return new CompositeRewriter(asList(
                cloudappRewriter,
                instagramRewriter,
                tcoRewriter,
                twitterImageRewriter));
    }

}
