/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alisenturk.elasticloggerv2.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author alisenturk
 */
public class Helper {

    public static String getCreationDate(){
        Date postDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return sdf.format(postDate);
    }
}
