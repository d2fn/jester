package com.d2fn.jester.config;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * BdbConfiguration
 */
public class BdbConfiguration {
    
    @JsonProperty
    @NotEmpty
    private String path;

    public String getPath() {
        return path;
    }
}
