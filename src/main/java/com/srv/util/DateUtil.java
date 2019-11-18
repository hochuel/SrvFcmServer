package com.srv.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String getDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        return dateFormat.format(new Date(System.currentTimeMillis()));
    }


    public static String getDate(String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date(System.currentTimeMillis()));
    }
}
