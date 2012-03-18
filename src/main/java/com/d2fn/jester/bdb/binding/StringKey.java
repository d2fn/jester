package com.d2fn.jester.bdb.binding;

import java.io.Serializable;

/**
 * StringKey
 * @author Dietrich Featherston
 */
public class StringKey implements Serializable {
    
    private String key;
    
    public StringKey(String key) {
        this.key = key;
    }
    
    public String getKey() {
        return key;
    }
}
