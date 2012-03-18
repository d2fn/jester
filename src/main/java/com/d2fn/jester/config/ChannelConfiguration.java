package com.d2fn.jester.config;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * ChannelConfiguration
 * @author Dietrich Featherston
 */
public class ChannelConfiguration {
    
    @NotEmpty
    @JsonProperty
    private String name;

    @JsonProperty
    private String key;
    
    public String getName() {
        return name;
    }
    
    public String getKey() {
        return key;
    }

    public boolean isProtected() {
        return key != null;
    }
}
