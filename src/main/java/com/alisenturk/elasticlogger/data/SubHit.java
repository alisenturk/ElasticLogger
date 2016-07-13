package com.alisenturk.elasticlogger.data;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author alisenturk
 */
public class SubHit implements Serializable{
    
    private String      _index;
    private String      _type;
    private String      _id;
    private String      _score;
    
    private Map<String,Object>      fields;

    public String getIndex() {
        return _index;
    }

    public void setIndex(String _index) {
        this._index = _index;
    }

    public String getType() {
        return _type;
    }

    public void setType(String _type) {
        this._type = _type;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getScore() {
        return _score;
    }

    public void setScore(String _score) {
        this._score = _score;
    }

    public Map<String, Object> getFields() {
        return fields;
    }

    public void setFields(Map<String, Object> fields) {
        this.fields = fields;
    }

    
    
    
    
}
