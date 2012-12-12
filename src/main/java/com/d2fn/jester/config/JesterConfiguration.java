package com.d2fn.jester.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.client.HttpClientConfiguration;
import com.yammer.dropwizard.client.JerseyClientConfiguration;
import com.yammer.dropwizard.config.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.net.URI;

/**
 * JesterConfiguration
 * @author Dietrich Featherston
 */
public class JesterConfiguration extends Configuration {

    public static class GraphiteConfiguration {
        private String urlPrefix;
        private File storageLocation;
        private String resultUrlPrefix;
        private JerseyClientConfiguration jersey = new JerseyClientConfiguration();
        private int numImagesToKeep = 1000;

        public String getUrlPrefix() {
            return urlPrefix;
        }

        public File getStorageLocation() {
            return storageLocation;
        }

        public String getResultUrlPrefix() {
            return resultUrlPrefix;
        }

        public JerseyClientConfiguration getJersey() {
            return jersey;
        }

        public int getNumImagesToKeep() {
            return numImagesToKeep;
        }
    }

    public static class ZerocaterConfiguration {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }

    public static class SensuConfiguration {
        private URI url;
        private JerseyClientConfiguration jersey = new JerseyClientConfiguration();

        public URI getUrl() {
            return url;
        }

        public JerseyClientConfiguration getJersey() {
            return jersey;
        }
    }

    @NotEmpty
    @JsonProperty
    private String prose;

    @Valid
    @NotNull
    @JsonProperty("http_client")
    private HttpClientConfiguration httpClient = new HttpClientConfiguration();

    private ZerocaterConfiguration zerocater = new ZerocaterConfiguration();

    @Valid
    @NotNull
    private SensuConfiguration sensu = new SensuConfiguration();

    @NotNull
    @JsonProperty
    private BotConfiguration bot;

    @NotNull
    @JsonProperty
    private BdbConfiguration bdb;

    private GraphiteConfiguration graphite = new GraphiteConfiguration();

    public GraphiteConfiguration getGraphite() {
        return graphite;
    }

    public String getProse() {
        return prose;
    }

    public BotConfiguration getBot() {
        return bot;
    }

    public HttpClientConfiguration getHttpClientConfiguration() {
        return httpClient;
    }
    
    public BdbConfiguration getBdbConfiguration() {
        return bdb;
    }

    public ZerocaterConfiguration getZerocater() {
        return zerocater;
    }

    public SensuConfiguration getSensu() {
        return sensu;
    }
}
