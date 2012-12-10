package com.d2fn.jester.plugin.gis.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

public class ResponseData {
    @JsonProperty
    private List<GisImageResult> results;

    @JsonProperty
    private Cursor cursor;
    
    public List<GisImageResult> getResults() {
        return Collections.unmodifiableList(results);
    }
}
