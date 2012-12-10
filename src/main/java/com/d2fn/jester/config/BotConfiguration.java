package com.d2fn.jester.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Arrays;

/**
 * BotConfiguration
 * @author Dietrich Featherston
 */
public class BotConfiguration {

    // Fields

    @NotEmpty
    @JsonProperty
    private String name;

    @NotEmpty
    @JsonProperty
    private String hostname;

    @JsonProperty
    private Boolean verbose = true;

    @NotEmpty
    @JsonProperty
    private ChannelConfiguration[] channels;


    // Accessors

    public String getName() {
        return name;
    }

    public String getHostname() {
        return hostname;
    }

    public Boolean verbose() {
        return verbose;
    }

    public ChannelConfiguration[] getChannels() {
        return Arrays.copyOf(channels, channels.length);
    }
}
