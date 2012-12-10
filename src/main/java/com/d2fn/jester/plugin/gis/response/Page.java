package com.d2fn.jester.plugin.gis.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Page
 * @author Dietrich Featherston
 */
public class Page {

    @JsonProperty
    private String start;
    
    @JsonProperty
    private Integer label;
}
