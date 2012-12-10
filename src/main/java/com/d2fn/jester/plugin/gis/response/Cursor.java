package com.d2fn.jester.plugin.gis.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;
import java.util.List;

/**
 * Cursor
 * @author Dietrich Featherston
 */
public class Cursor {
    @JsonProperty
    private String resultCount;

    @JsonProperty
    private String estimatedResultCount;

    @JsonProperty
    private Integer currentPageIndex;

    @JsonProperty
    private URI moreResultsUrl;

    @JsonProperty
    private String searchResultTime;

    @JsonProperty
    private List<Page> pages;
}
