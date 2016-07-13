package com.alisenturk.elasticlogger.data;

/**
 *
 * @author alisenturk
 */
public enum FilterType {
    TERM("term"),
    RANGE("range"),
    WILDCARD("wildcard");

    FilterType(String value) {
        this.value = value;
    }
    
    private String value;

    public String getValue() {
        return value;
    }
    
    
    
}
