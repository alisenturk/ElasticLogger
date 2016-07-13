package com.alisenturk.elasticlogger.data;

import java.io.Serializable;

/**
 *
 * @author alisenturk
 */
public class Aggregation implements Serializable{
    
    private GroupByState    group_by_state;

    public GroupByState getGroup_by_state() {
        return group_by_state;
    }

    public void setGroup_by_state(GroupByState group_by_state) {
        this.group_by_state = group_by_state;
    }
    
    
}
