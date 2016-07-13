package com.alisenturk.elasticlogger.data;

/**
 *
 * @author alisenturk
 */
public enum OrderByType {
    
    ASC("asc","Küçükten Büyüğe"),
    DESC("desc","Büyükten Küçüğe");

    OrderByType(String value, String label) {
        this.value = value;
        this.label = label;
    }
    
    
    
    private String  value;
    private String  label;

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    
}
