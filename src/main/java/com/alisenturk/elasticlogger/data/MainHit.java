package com.alisenturk.elasticlogger.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alisenturk
 */
public class MainHit implements Serializable{
    private int                 total;
    private String              max_score;
    
    private List<SubHit>     hits = new ArrayList<>();

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getMax_score() {
        return max_score;
    }

    public void setMax_score(String max_score) {
        this.max_score = max_score;
    }

    public List<SubHit> getHits() {
        return hits;
    }

    public void setHits(List<SubHit> hits) {
        this.hits = hits;
    }



    
    
    
}
