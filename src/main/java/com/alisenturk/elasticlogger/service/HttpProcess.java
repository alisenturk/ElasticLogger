package com.alisenturk.elasticlogger.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author alisenturk
 */
public class HttpProcess {
    
    private ElasticSetting  settings;
    private String  parameter;

    /**
     * 
     * @param contentType : ContentType.java
     * @param data  : Json formatında data
     * @param token : Token bilgisi
     * @param clzz  : Geri döndürülecek datanın tipi
     */
    public HttpResponseData send(ContentType contentType,String data,String token,Class clzz){
        HttpResponseData result = new HttpResponseData();
        
        String restUrl = settings.getHostAddress() + ":" + settings.getPortNumber() +"/"+settings.getIndexName();
        if(settings.getMappingName()!=null && settings.getMappingName().length()>0){
            restUrl +="/"+settings.getMappingName();     
        }
        if(parameter!=null && parameter.length()>0){
            restUrl += "/"+parameter;
        }
        
        Client      client      = Client.create();
        WebResource webResource = client.resource(restUrl);
        if(token!=null){
               webResource.header("token",token);
               webResource.head().getHeaders().add("token",token);
        }

        ClientResponse response = webResource.type(contentType.getValue()).post(ClientResponse.class, data);                   
        if (response.getStatus() != 200) {
               result.setStatusCode(String.valueOf(response.getStatus()));
               result.setStatusMessage(response.getStatusInfo().toString());
        }else{
            result.setStatusCode("OK");
            result.setStatusMessage("OK");
        }

        result.setResponseData(response.getEntity(String.class));
        
        return result;
 
    }
    public HttpResponseData send(RequestMethod   requestMethod,ContentType     contentType,String query){
        HttpResponseData resp = null;
        HttpURLConnection httpConn = null;
        try{
            String url = settings.getHostAddress() + ":" + settings.getPortNumber() +"/"+settings.getIndexName();
            if(settings.getMappingName()!=null && settings.getMappingName().length()>0){
                url +="/"+settings.getMappingName();     
            }
            if(parameter!=null && parameter.length()>0){
                url += "/"+parameter;
            }
            URL object = new URL(url.trim());
            httpConn = (HttpURLConnection) object.openConnection();
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            httpConn.setRequestProperty("Content-Type",contentType.getValue() );
            httpConn.setRequestProperty("Accept", contentType.getValue());
            httpConn.setRequestMethod(requestMethod.getValue()); //DELETE, GET , PUT,POST                                         
            
            if(query!=null && query.length()>3){
                OutputStreamWriter wr = new OutputStreamWriter(httpConn.getOutputStream());
                wr.write(query);          
                wr.flush();
            }
            
            StringBuilder sb = new StringBuilder();
            int HttpResult = httpConn.getResponseCode();

            switch (HttpResult) {
                case HttpURLConnection.HTTP_OK:{
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }   br.close();
                    resp = new HttpResponseData("OK","",sb.toString());
                    break;
                }case HttpURLConnection.HTTP_CREATED:{
                    resp = new HttpResponseData("OK","",httpConn.getResponseMessage());
                    break;
                }default:{
                    resp = new HttpResponseData("NOK",httpConn.getResponseMessage(),"");
                    break;
                }
            }
            httpConn.disconnect();
        }catch(Exception e){
            resp = new HttpResponseData("NOK",e.getMessage(),"");
        }finally{
            if(httpConn!=null){
                httpConn.disconnect();
            }
            httpConn = null;
        }
        
        return resp;
    }

    public ElasticSetting getSettings() {
        return settings;
    }

    public void setSettings(ElasticSetting settings) {
        this.settings = settings;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
    
    
}
