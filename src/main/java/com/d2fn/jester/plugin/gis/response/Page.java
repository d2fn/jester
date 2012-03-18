package com.d2fn.jester.plugin.gis.response;

import org.codehaus.jackson.annotate.JsonProperty;

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
