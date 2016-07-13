package com.alisenturk.elasticlogger.data;

import java.io.Serializable;

/**
 *
 * @author alisenturk
 */
public class Count implements Serializable{
    
    private long    count;
    private Shard   _shards;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public Shard getShards() {
        return _shards;
    }

    public void setShards(Shard _shards) {
        this._shards = _shards;
    }
    
    
}
