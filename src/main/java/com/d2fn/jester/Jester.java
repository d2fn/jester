package com.d2fn.jester;

import com.d2fn.jester.bdb.BdbEnvironment;
import com.d2fn.jester.bot.JesterBot;
import com.d2fn.jester.config.JesterConfiguration;
import com.d2fn.jester.plugin.Plugin;
import com.d2fn.jester.plugin.RecallPlugin;
import com.d2fn.jester.plugin.RewritingPlugin;
import com.d2fn.jester.plugin.WhoAreYouPlugin;
import com.d2fn.jester.plugin.gis.GoogleImageSearchPlugin;
import com.d2fn.jester.plugin.sensu.SensuPlugin;
import com.d2fn.jester.plugin.twitter.TwitterPlugin;
import com.d2fn.jester.plugin.zerocater.ZeroCaterPlugin;
import com.d2fn.jester.resource.PingResource;
import com.d2fn.jester.rewrite.*;
import com.google.common.collect.Lists;
import com.sun.jersey.api.client.Client;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.client.HttpClientBuilder;
import com.yammer.dropwizard.client.JerseyClientBuilder;
import com.yammer.dropwizard.client.JerseyClientConfiguration;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

import static java.util.Arrays.asList;

/**
 * Jester
 * @author Dietrich Featherston
 */
public class Jester extends Service<JesterConfiguration> {
    private static final Logger log = LoggerFactory.getLogger(Jester.class);
    
    public static void main(String[] args) throws Exception {
        new Jester().run(args);
    }

    @Override
    public void initialize(Bootstrap<JesterConfiguration> bootstrap) {
    }

    @Override
    public void run(JesterConfiguration config, Environment environment) throws Exception {
        log.info("\n\n{}\n", config.getProse());

        HttpClient httpClient = new HttpClientBuilder().using(config.getHttpClientConfiguration()).build();
        BdbEnvironment bdbEnv = new BdbEnvironment(config.getBdbConfiguration());

        final CompositeRewriter compositeRewriter = buildCompositeRewriter(httpClient);
        final SentenceRewriter sentenceRewriter = new SentenceRewriter(compositeRewriter);

        final Collection<Plugin> plugins = Lists.newArrayList();
        plugins.add(new WhoAreYouPlugin());
        plugins.add(new GoogleImageSearchPlugin(httpClient));
        plugins.add(new TwitterPlugin(httpClient, sentenceRewriter));
        plugins.add(new RecallPlugin(bdbEnv, httpClient));
        plugins.add(new RewritingPlugin(compositeRewriter));
        plugins.add(new SensuPlugin(buildJerseyClient(config.getSensu().getJersey(), environment), config.getSensu().getUrl()));

        JesterConfiguration.ZerocaterConfiguration zcConfig = config.getZerocater();
        plugins.add(new ZeroCaterPlugin(zcConfig.getUsername(), zcConfig.getPassword()));

        JesterBot bot = new JesterBot(config.getBot(), plugins);
        environment.manage(bot);
        environment.manage(bdbEnv);
        environment.addResource(new PingResource());
    }

    private Client buildJerseyClient(JerseyClientConfiguration configuration, Environment environment) {
        final Client client = new JerseyClientBuilder().using(configuration).using(environment).build();
        return client;
    }

    private CompositeRewriter buildCompositeRewriter(HttpClient httpClient) {
        final CloudappRewriter cloudappRewriter = new CloudappRewriter(httpClient);
        final KingshitRewriter kingshitRewriter = new KingshitRewriter(httpClient);
        final InstagramRewriter instagramRewriter = new InstagramRewriter(httpClient);
        final TwitterImageRewriter twitterImageRewriter = new TwitterImageRewriter(httpClient);
        final TwitterSlideshowImageRewriter twitterSlideshowImageRewriter = new TwitterSlideshowImageRewriter(httpClient);
        final LinkRewriter linkRewriter = new LinkRewriter(new RedirectFinder());
        return new CompositeRewriter(asList(
                cloudappRewriter,
                kingshitRewriter,
                instagramRewriter,
                linkRewriter,
                twitterImageRewriter,
                twitterSlideshowImageRewriter));
    }

}
