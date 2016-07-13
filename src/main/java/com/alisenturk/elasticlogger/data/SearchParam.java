package com.alisenturk.elasticlogger.data;

/**
 *
 * @author alisenturk
 */
public class SearchParam {
    
    private String          paramName;
    private FilterType      filterType;
    private String[]        value;
    private VariableType    varType;

    public SearchParam() {
        super();
    }
    
    public SearchParam(String paramName, FilterType filterType, String[] value, VariableType varType) {
        this.paramName = paramName;
        this.filterType = filterType;
        this.value = value;
        this.varType = varType;
    }

    
    
    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public FilterType getFilterType() {
        return filterType;
    }

    public void setFilterType(FilterType filterType) {
        this.filterType = filterType;
    }

    public String[] getValue() {
        return value;
    }

    public void setValue(String[] value) {
        this.value = value;
    }

    public VariableType getVarType() {
        return varType;
    }

    public void setVarType(VariableType varType) {
        this.varType = varType;
    }
    
    
    
}
