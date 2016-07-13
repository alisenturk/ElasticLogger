package com.alisenturk.elasticlogger.service;

/**
 *
 * @author alisenturk
 */
public class ElasticSetting {
    private String  hostAddress;
    private String  portNumber;
    private String  indexName;
    private String  mappingName;
    private boolean debugMode = false;

    public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public String getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(String portNumber) {
        this.portNumber = portNumber;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getMappingName() {
        return mappingName;
    }

    public void setMappingName(String mappingName) {
        this.mappingName = mappingName;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    @Override
    public String toString() {
        return "ElasticSetting{" + "hostAddress=" + hostAddress + ", portNumber=" + portNumber + ", indexName=" + indexName + ", mappingName=" + mappingName + '}';
    }
    
    
}
