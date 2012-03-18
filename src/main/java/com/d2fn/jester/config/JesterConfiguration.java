package com.d2fn.jester.config;

import com.d2fn.jester.bot.JesterBot;
import com.yammer.dropwizard.client.HttpClientConfiguration;
import com.yammer.dropwizard.client.JerseyClientConfiguration;
import com.yammer.dropwizard.config.Configuration;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * JesterConfiguration
 * @author Dietrich Featherston
 */
public class JesterConfiguration extends Configuration {

    @NotEmpty
    @JsonProperty
    private String prose;

    @Valid
    @NotNull
    @JsonProperty("http_client")
    private HttpClientConfiguration httpClient = new HttpClientConfiguration();
//    private JerseyClientConfiguration httpClient = new JerseyClientConfiguration();

    @NotNull
    @JsonProperty
    private BotConfiguration bot;


    public String getProse() {
        return prose;
    }

    public BotConfiguration getBot() {
        return bot;
    }

    public HttpClientConfiguration getHttpClientConfiguration() {
        return httpClient;
    }
}
