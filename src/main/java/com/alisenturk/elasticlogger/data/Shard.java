package com.alisenturk.elasticlogger.data;

import java.io.Serializable;

/**
 *
 * @author alisenturk
 */
public class Shard implements Serializable{
    private long    total;
    private int     successful;
    private int     failed;
}
