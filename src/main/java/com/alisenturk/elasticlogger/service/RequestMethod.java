package com.alisenturk.elasticlogger.service;

/**
 *
 * @author alisenturk
 */
public enum RequestMethod {
    POST("POST","POST"),
    GET("GET","GET"),
    DELETE("DELETE","DELETE"),
    PUT("PUT","PUT");

    RequestMethod(String value, String label) {
        this.value = value;
        this.label = label;
    }
    
    
    
    private String value;
    private String label;

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
