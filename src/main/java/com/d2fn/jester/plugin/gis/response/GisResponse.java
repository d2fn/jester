package com.d2fn.jester.plugin.gis.response;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * GisResponse
 * @author Dietrich Featherston
 */
public class GisResponse {

    @JsonProperty
    private ResponseData responseData;
    
    @JsonProperty
    private int responseStatus = 200;
    
    @JsonProperty
    private String responseDetails;

    public ResponseData getResponseData() {
        return responseData;
    }

    public int getResponseStatus() {
        return responseStatus;
    }
    
    public String getResponseDetails() {
        return responseDetails;
    }
}
