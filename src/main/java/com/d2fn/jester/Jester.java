package com.d2fn.jester;

import com.d2fn.jester.bdb.BdbEnvironment;
import com.d2fn.jester.bot.JesterBot;
import com.d2fn.jester.config.JesterConfiguration;
import com.d2fn.jester.plugin.Plugin;
import com.d2fn.jester.plugin.RecallPlugin;
import com.d2fn.jester.plugin.WhoAreYouPlugin;
import com.d2fn.jester.plugin.gis.GoogleImageSearchPlugin;
import com.d2fn.jester.plugin.image.ImagePlugin;
import com.d2fn.jester.plugin.image.ImagePluginBuilder;
import com.d2fn.jester.plugin.twitter.TwitterPlugin;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.client.HttpClientFactory;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.logging.Log;
import org.apache.http.client.HttpClient;

import java.util.ArrayList;
import java.util.Collection;

import static java.util.regex.Pattern.compile;

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

        Collection<Plugin> plugins = new ArrayList<Plugin>();
        plugins.add(new WhoAreYouPlugin());
        plugins.add(new GoogleImageSearchPlugin(httpClient));
        plugins.add(new TwitterPlugin(httpClient));
        plugins.add(new RecallPlugin(bdbEnv, httpClient));
        plugins.add(buildCloudappPlugin(httpClient));
        plugins.add(buildInstagramPlugin(httpClient));

        JesterBot bot = new JesterBot(config.getBot(), plugins);
        environment.manage(bot);
        environment.manage(bdbEnv);
    }

    private Plugin buildInstagramPlugin(HttpClient httpClient) {
        return new ImagePluginBuilder()
                .setName("Instagram")
                .setLinkPattern(ImagePlugin.INSTAGRAM_LINK)
                .setEmbeddedLinkPattern(ImagePlugin.INSTAGRAM_EMBEDDED_LINK)
                .setHttp(httpClient)
                .createImagePlugin();
    }

    private Plugin buildCloudappPlugin(HttpClient httpClient) {
        return new ImagePluginBuilder()
                .setName("Cloudapp")
                .setLinkPattern(ImagePlugin.CLOUDAPP_LINK)
                .setEmbeddedLinkPattern(ImagePlugin.CLOUDAPP_EMBEDDED_LINK)
                .setHttp(httpClient)
                .createImagePlugin();
    }
}
