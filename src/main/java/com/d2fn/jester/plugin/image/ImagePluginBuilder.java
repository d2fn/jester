package com.d2fn.jester.plugin.image;

import org.apache.http.client.HttpClient;

import java.util.regex.Pattern;

public class ImagePluginBuilder {
    private String name;
    private Pattern linkPattern;
    private Pattern embeddedLinkPattern;
    private HttpClient http;

    public ImagePluginBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ImagePluginBuilder setLinkPattern(Pattern linkPattern) {
        this.linkPattern = linkPattern;
        return this;
    }

    public ImagePluginBuilder setEmbeddedLinkPattern(Pattern embeddedLinkPattern) {
        this.embeddedLinkPattern = embeddedLinkPattern;
        return this;
    }

    public ImagePluginBuilder setHttp(HttpClient http) {
        this.http = http;
        return this;
    }

    public ImagePlugin createImagePlugin() {
        return new ImagePlugin(name, linkPattern, embeddedLinkPattern, http);
    }
}
