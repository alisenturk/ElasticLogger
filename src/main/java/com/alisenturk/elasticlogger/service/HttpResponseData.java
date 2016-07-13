package com.alisenturk.elasticlogger.service;

/**
 *
 * @author alisenturk
 */
public class HttpResponseData {
    
    private String  statusCode;
    private String  statusMessage;
    private String  responseData;

    public HttpResponseData() {
        super();
    }

    public HttpResponseData(String status, String errorMessage, String responseData) {
        this.statusCode = status;
        this.statusMessage = errorMessage;
        this.responseData = responseData;
    }

    @Override
    public String toString() {
        return "HttpResponseData{" + "status=" + statusCode + ", errorMessage=" + statusMessage + ", responseData=" + responseData + '}';
    }

    
    
    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }
    
    
    
}
