package com.d2fn.jester.plugin.twitter.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Tweet
 * @author Dietrich Featherston
 */
public class Tweet {

    @NotEmpty
    @JsonProperty
    private String id;

    @NotEmpty
    @JsonProperty
    private String text;

    @JsonProperty
    private TwitterUser user;
    
    public TwitterUser getUser() {
        return user;
    }
    
    public String getText() {
        return text;
    }
}
