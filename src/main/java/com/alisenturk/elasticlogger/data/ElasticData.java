package com.alisenturk.elasticlogger.data;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alisenturk
 */
public class ElasticData implements Serializable{
    private int                 took;
    private boolean             timedOut;
    private List<Shard>         shards          =   new ArrayList<Shard>();
    private MainHit             hits            = new MainHit();
    private Aggregation         aggregations;
    
    public int getTook() {
        return took;
    }

    public void setTook(int took) {
        this.took = took;
    }

    @SerializedName("timed_out")
    public boolean isTimedOut() {
        return timedOut;
    }

    public void setTimedOut(boolean timedOut) {
        this.timedOut = timedOut;
    }

    @SerializedName("_shards")
    public List<Shard> getShards() {
        return shards;
    }

    public void setShards(List<Shard> shards) {
        this.shards = shards;
    }

    public MainHit getHits() {
        return hits;
    }

    public void setHits(MainHit hits) {
        this.hits = hits;
    }

    public Aggregation getAggregations() {
        return aggregations;
    }

    public void setAggregations(Aggregation aggregations) {
        this.aggregations = aggregations;
    }

    
    
}
