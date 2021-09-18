package com.aspire.aiops.utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author ：Vincent Hu
 * @date ：Created in 6/19/2019 13:28
 * @description：${description}
 * @modified By：
 * @version: $version$
 */
public class TimeUtil {

    //private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String seconds2Time(Long seconds) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(seconds*1000);
        String dateString = simpleDateFormat.format(date);
        return dateString;
    }

    public static long time2Seconds(String time){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            calendar.setTime(simpleDateFormat.parse(time.replace(".","-")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.getTimeInMillis()/1000;
    }
}
