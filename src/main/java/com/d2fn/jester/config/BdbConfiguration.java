package com.d2fn.jester.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class BdbConfiguration {
    @JsonProperty
    @NotEmpty
    private String path;

    public String getPath() {
        return path;
    }
}
