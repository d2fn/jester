package com.d2fn.jester.bdb.binding;

import com.d2fn.jester.plugin.Message;

import java.io.Serializable;

/**
 * SavedLink
 * @author Dietrich Featherston
 */
public class SavedLink implements Serializable {

    private String url;
    private Message message;

    public SavedLink(String url, Message message) {
        this.url = url;
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public Message getMessage() {
        return message;
    }
}
