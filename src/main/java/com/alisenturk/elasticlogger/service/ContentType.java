package com.alisenturk.elasticlogger.service;

/**
 *
 * @author alisenturk
 */
public enum ContentType {
    JSON("application/json","Json"),
    HTML("html","Html"),
    TEXT("text","Text");

    ContentType(String value, String label) {
        this.value = value;
        this.label = label;
    }
           
    private String  value;
    private String  label;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
    
}
