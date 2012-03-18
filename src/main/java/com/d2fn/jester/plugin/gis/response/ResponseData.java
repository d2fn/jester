package com.d2fn.jester.plugin.gis.response;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Collections;
import java.util.List;

/**
 * ResponseData
 * @author Dietrich Featherston
 */
public class ResponseData {

    @JsonProperty
    private List<GisImageResult> results;

    @JsonProperty
    private Cursor cursor;
    
    public List<GisImageResult> getResults() {
        return Collections.unmodifiableList(results);
    }
}
