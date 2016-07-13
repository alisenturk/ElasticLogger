package com.alisenturk.elasticlogger.service;

import com.alisenturk.elasticlogger.data.ElasticData;
import com.alisenturk.elasticlogger.data.SubHit;
import com.google.gson.Gson;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author alisenturk
 */
public class DataReader<T>{
    
    public List<T> read(String jsonData,Class clzz){
        List<T> list = new ArrayList<>();
        T object = null;
        try{
             
            Field[]     classFields = null;
            Gson        gson        = new Gson();
            ElasticData elasticData = gson.fromJson(jsonData,new ElasticData().getClass());
            Object      dataVal     = null;
            DateFormat  dateFormat  = new SimpleDateFormat("EEE, MMM dd, yyyy HH:mm:ss a zzzz");
            Class<?>    theClass    = null;
            
            Map<String,Object> mapOfType = null;
            
            if(elasticData!=null){
                for(SubHit shint:elasticData.getHits().getHits()){
                    if(shint.getFields()!=null){
                        object = (T)clzz.newInstance();
                        mapOfType = shint.getFields();
                        
                        for(Map.Entry<String,Object> entry: mapOfType.entrySet()){
                            classFields = object.getClass().getDeclaredFields();
                            for(Field classField:classFields){
                                classField.setAccessible(true);
                                if(entry.getKey().equalsIgnoreCase(classField.getName())){
                                    dataVal = ((java.util.ArrayList)entry.getValue()).get(0);
                                    if(classField.getGenericType().getTypeName().indexOf("Date")>-1){
                                        dataVal = dateFormat.parse(String.valueOf(dataVal));
                                    }else{
                                        theClass = Class.forName(classField.getGenericType().getTypeName());
                                        dataVal = theClass.cast(dataVal);
                                    }                                    
                                    classField.set(object,dataVal );
                                }
                                if(classField.getName().equalsIgnoreCase("id")){
                                    try{
                                        if(classField.getGenericType().getTypeName().equals("java.lang.String")){
                                            classField.set(object,shint.getId());
                                        }else if(classField.getGenericType().getTypeName().indexOf("Long")>-1){
                                            classField.set(object,Long.valueOf(shint.getId()));
                                        } 
                                       
                                    }catch(NumberFormatException e){
                                        classField.set(object,shint.getId());
                                    }
                                }
                            }
                        }
                        
                        list.add(object);
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
