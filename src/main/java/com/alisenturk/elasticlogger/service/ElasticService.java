package com.alisenturk.elasticlogger.service;

import com.alisenturk.elasticlogger.data.Count;
import com.alisenturk.elasticlogger.data.FilterType;
import com.alisenturk.elasticlogger.data.OrderByType;
import com.alisenturk.elasticlogger.data.SearchParam;
import com.alisenturk.elasticlogger.data.VariableType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alisenturk
 */
public class ElasticService <T>{
    
    private ElasticSetting setting;

    public ElasticSetting getSetting() {
        return setting;
    }

    public void setSetting(ElasticSetting setting) {
        this.setting = setting;
    }
    
    private ElasticService(){
    }
    
    /**
     * 
     * @param setting
     * @return ElasticSearch üzerinde CRUD işlemlerinin gerçekleştirilmesini sağlayan objeyi oluşturu. 
     * Setting mecburidir. Setting içinde oluşturulacak db ve tablo ismi belirtilir.
     */
    public static ElasticService createElasticService(ElasticSetting setting){
        ElasticService es = new ElasticService();
        es.setSetting(setting);
        return es;
    }
    private static Object json2Object(String json,Class clzz){
        Gson gson = new Gson();
        return gson.fromJson(json,clzz);                    
    }
    public long getDocumentCount(){
        long count = 0;
        try{
            HttpProcess http = new HttpProcess();
            http.setSettings(setting);            
            http.setParameter("_count");
            HttpResponseData respData =  http.send(RequestMethod.GET, ContentType.JSON,"");
            if(respData.getStatusCode().equals("OK")){
               Count countData = (Count)json2Object(respData.getResponseData(),Count.class);
               if(countData!=null){
                   count = countData.getCount();
               }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return count;
    }
    
    private String insertDocument2Elastic(String json,long docId){
        
        String strResponse = "OK";
        
        String restUrl = setting.getHostAddress() + ":" + setting.getPortNumber() +"/"+setting.getIndexName();
        if(setting.getMappingName()!=null && setting.getMappingName().length()>0){
            restUrl +="/"+setting.getMappingName();    
        }
        if(docId>0){
            restUrl += "/"+docId;
        }
        Client client                     = Client.create();
        WebResource webResource = client.resource(restUrl);
        ClientResponse response = webResource.type(ContentType.JSON.getValue()).post(ClientResponse.class, json);                   
        String output = response.getEntity(String.class);
        if(response.getStatus() != 200 && response.getStatus()!=201) {
               strResponse = response.getStatus() + "|"+ output;
        }
        client.destroy();
        return strResponse;
    }

    /**
     * 
     * @param list
     * @param docId
     * @return list içindeki tüm objeleri json formatında elasticsearch kayıt eder
     */
    public String addDocument(List<T> list,long docId){
        String result = "OK";
        try{
            Gson gson = new GsonBuilder().setDateFormat(DateFormat.FULL, DateFormat.FULL).create();

            String json = "";
            for(T t:list){
                json = gson.toJson(t);
                insertDocument2Elastic(json, docId++);
            }
        }catch(Exception e){
            result = e.getMessage();
        }
        return result;
    }
    /**
     * 
     * @param docId
     * @return belirtilen document ID'ye sahip kaydı siler.
     */
    public String deleteDocumentById(long docId){
        String result = "OK";
        try{
            HttpProcess http = new HttpProcess();
            http.setSettings(setting);
            http.setParameter("/"+docId);
            HttpResponseData resData = http.send(RequestMethod.DELETE, ContentType.JSON,"");
            if(!resData.getStatusCode().equals("OK"))
                result = resData.getStatusMessage();
            if(setting.isDebugMode()){
                System.out.println("deleteDocumentById["+docId+"]..:" + resData.toString());
            }
        }catch(Exception e){
            result = e.getMessage();
        }
        return result;
    }
    /**
     * 
     * @return Oluşturulan Index'i siler. Yani db'yi siler.
     */
    public String deleteDocumentIndex(){
        String result = "OK";
        try{
            HttpProcess http = new HttpProcess();
            http.setSettings(setting);  
            http.getSettings().setMappingName(null);
            HttpResponseData resData = http.send(RequestMethod.DELETE, ContentType.JSON,"");
            if(!resData.getStatusCode().equals("OK"))
                result = resData.getStatusMessage();
            if(setting.isDebugMode()){
                System.out.println("deleteDocumentIndex..:" + resData.toString());
            }
        }catch(Exception e){
            result = e.getMessage();
        }
        return result;
    }
    
    private void updateDocument2Elastic(String json,long docId){
        
        HttpProcess http = new HttpProcess();
        http.setSettings(setting);
        http.setParameter("/"+docId + " ");
        HttpResponseData respData =  http.send(RequestMethod.POST, ContentType.JSON,json);
        if(setting.isDebugMode()){
            System.out.println("updateDocument2Elastic.json...:" + json);
            System.out.println(respData.toString());
        }
    }
    public String   updateDocument(List<T> list,long docId){
        String result = "OK";
        try{
            Gson gson = new GsonBuilder().setDateFormat(DateFormat.FULL, DateFormat.FULL).create();
            String json = "";
            for(T t:list){
                json = gson.toJson(t);
                updateDocument2Elastic(json, docId++);
            }
        }catch(Exception e){
            result = e.getMessage();
        }
        return "";
    }
    
    private String preSearch(String searchText,int maxCount,int fromRecord,List<String> fields,String orderby,OrderByType orderByType){
        StringBuilder qb = new StringBuilder();
        int fieldCount = fields.size();
        int fieldIndx = 0;
        qb.append("{ ");
        qb.append("    \"query\": {");
        qb.append("      \"filtered\": {");
        qb.append("        \"query\": {");
        qb.append("          \"query_string\": {  ");
        qb.append("            \"query\": \""+searchText+"\" ,");
        qb.append("             \"lowercase_expanded_terms\":true ");
        qb.append("          } ");
        qb.append("        } ");
        qb.append("      } ");
        qb.append("    }, ");
        qb.append("    \"fields\": [ ");
        for(String field:fields){
            fieldIndx++;
            qb.append(" \"" + field + "\"");
            if((fieldIndx+1)<=fieldCount){
                qb.append(",");
            }
        }
        qb.append("],");
        qb.append("\"from\": " + fromRecord +",");
        qb.append("\"size\": "+ maxCount +",");        
        if(orderby!=null && orderByType!=null){
        qb.append("\"sort\": {");
        qb.append("\""+orderby+"\": {");
        qb.append(" \"order\": \""+orderByType.getValue()+"\"");
        qb.append("    }");
        qb.append("  },");
        }
        qb.append("    \"explain\": false");
        qb.append("}");
        
        return qb.toString();
    }
    private String convertJsonData(String val,VariableType type){
        if(type.equals(VariableType.INT)){
            return val.toLowerCase();
        }else{
            return "\""+val.toLowerCase()+"\"";
        }
    }
    private String preSearchByParams(List<SearchParam> mustParams,int maxCount,int fromRecord,List<String> fields,String orderby,OrderByType orderByType){
        return preSearchByParams(mustParams,null, maxCount, fromRecord, fields, orderby, orderByType);
    }
    private String preSearchByParams(List<SearchParam> mustParams,List<SearchParam> shouldParams,int maxCount,int fromRecord,List<String> fields,String orderby,OrderByType orderByType){
        int mustParamsCount = mustParams.size();
        int shldParamsCount = shouldParams.size();
        int indx            = 0;
        int fieldCount      = fields.size();
        int fieldIndx       = 0;
        StringBuilder qb = new StringBuilder();
        qb.append("{");
        qb.append(" \"query\": {");
        qb.append("     \"bool\": {");
        if(shldParamsCount>0){
            qb.append("         \"should\": [");
            for(SearchParam param:shouldParams){
                indx++;
                qb.append("{");
                if(param.getFilterType().equals(FilterType.WILDCARD)){
                    qb.append("\"wildcard\": {");
                    qb.append("\""+param.getParamName()+"\": "+convertJsonData(param.getValue()[0],param.getVarType()));
                    qb.append("}");
                }else if(param.getFilterType().equals(FilterType.TERM)){
                    qb.append("\"term\": {");
                    qb.append("\""+param.getParamName()+"\": "+convertJsonData(param.getValue()[0],param.getVarType()));
                    qb.append("}");
                }else if(param.getFilterType().equals(FilterType.RANGE) && param.getValue().length>1){
                    qb.append("\"range\": {");
                    qb.append("\""+param.getParamName()+"\": {");
                    qb.append("     \"from\": " + convertJsonData(param.getValue()[0],param.getVarType()) + ",");
                    qb.append("     \"to\": " +convertJsonData(param.getValue()[1],param.getVarType()));
                    qb.append("     }");
                    qb.append("}");
                }
                qb.append("}");
                if(mustParamsCount>indx){
                    qb.append(",");
                }
            }
            qb.append("            ],");
        }
        indx = 0;
        if(mustParamsCount>0){
            qb.append("         \"must\": [");
            for(SearchParam param:mustParams){
                indx++;
                qb.append("{");
                if(param.getFilterType().equals(FilterType.WILDCARD)){
                    qb.append("\"wildcard\": {");
                    qb.append("\""+param.getParamName()+"\": "+convertJsonData(param.getValue()[0],param.getVarType()));
                    qb.append("}");
                }else if(param.getFilterType().equals(FilterType.TERM)){
                    qb.append("\"term\": {");
                    qb.append("\""+param.getParamName()+"\": "+convertJsonData(param.getValue()[0],param.getVarType()));
                    qb.append("}");
                }else if(param.getFilterType().equals(FilterType.RANGE) && param.getValue().length>1){
                    qb.append("\"range\": {");
                    qb.append("\""+param.getParamName()+"\": {");
                    qb.append("     \"from\": " + convertJsonData(param.getValue()[0],param.getVarType()) + ",");
                    qb.append("     \"to\": " +convertJsonData(param.getValue()[1],param.getVarType()));
                    qb.append("     }");
                    qb.append("}");
                }
                qb.append("}");
                if(mustParamsCount>indx){
                    qb.append(",");
                }
            }
            qb.append("            ],");
        }
        qb.append("         \"minimum_should_match\": 1,");
        qb.append("         \"boost\": 1.0");
        qb.append("      }");
        qb.append("  },");
        qb.append("    \"fields\": [ ");
        for(String field:fields){
            fieldIndx++;
            qb.append(" \"" + field + "\"");
            if((fieldIndx+1)<=fieldCount){
                qb.append(",");
            }
        }
        qb.append("],");
        qb.append("\"from\": " + fromRecord +",");
        qb.append("\"size\": "+ maxCount +",");        
        if(orderby!=null && orderByType!=null){
        qb.append("\"sort\": {");
        qb.append("\""+orderby+"\": {");
        qb.append(" \"order\": \""+orderByType.getValue()+"\"");
        qb.append("    }");
        qb.append("  },");
        }
        qb.append("    \"explain\": false");
        qb.append("}");
        
        return qb.toString();
    }
    public HttpResponseData search(List<SearchParam> params,int maxCount,int fromRecord,List<String> fields,String orderby,OrderByType orderByType){
        return search(params,new ArrayList<SearchParam>(), maxCount, fromRecord, fields, orderby, orderByType);
    }
    public HttpResponseData search(List<SearchParam> params,List<SearchParam> shouldParams,int maxCount,int fromRecord,List<String> fields,String orderby,OrderByType orderByType){
        String json = preSearchByParams(params,shouldParams,maxCount,fromRecord, fields, orderby,orderByType);
        HttpProcess http = new HttpProcess();
        http.setSettings(setting);
        http.setParameter("_search?pretty");
        HttpResponseData respData =  http.send(RequestMethod.POST, ContentType.JSON,json);
        if(setting.isDebugMode()){
            System.out.println("SearchQuery..:" + json);
            System.out.println(respData.toString());
            System.out.println("----------------------------------------------");
        }
        return respData;
    }
    public HttpResponseData search(String searchText,int maxCount,int fromRecord,List<String> fields,String orderby){
        return search(searchText, maxCount, fromRecord, fields, orderby, OrderByType.ASC);
    }
    public HttpResponseData search(String searchText,int maxCount,int fromRecord,List<String> fields,String orderby,OrderByType orderByType){
        String json = preSearch(searchText,maxCount,fromRecord, fields, orderby,orderByType);
        HttpProcess http = new HttpProcess();
        http.setSettings(setting);
        http.setParameter("_search?pretty");
        HttpResponseData respData =  http.send(RequestMethod.POST, ContentType.JSON,json);
        if(setting.isDebugMode()){
            System.out.println("SearchQuery..:" + json);
            System.out.println(respData.toString());
            System.out.println("----------------------------------------------");
        }
        return respData;
    }
    
    public HttpResponseData groupBy(String fieldName){
        HttpResponseData response = new HttpResponseData();
        StringBuilder sql = new StringBuilder();
        sql.append("{");
        sql.append("    \"size\": 0,");
        sql.append("    \"aggs\": {");
        sql.append("      \"group_by_state\": {");
        sql.append("        \"terms\": {");
        sql.append("          \"field\": \""+fieldName+"\"");
        sql.append("        }");
        sql.append("      }");
        sql.append("    }");
        sql.append("}");
        
        HttpProcess http = new HttpProcess();
        http.setSettings(setting);
        http.setParameter("_search?pretty");
        response = http.send(ContentType.JSON, sql.toString(),null,String.class);
        if(setting.isDebugMode()){
            System.out.println("groupBy..:" + sql.toString());
            System.out.println(response.toString());
            System.out.println("----------------------------------------------");
        }
        return response;
    }
}
