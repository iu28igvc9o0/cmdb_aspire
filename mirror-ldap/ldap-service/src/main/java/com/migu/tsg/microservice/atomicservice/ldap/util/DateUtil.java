package com.migu.tsg.microservice.atomicservice.ldap.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 * <p>
 * 项目名称:  咪咕微服务运营平台-CICD
 * 包:       com.migu.tsg.msp.microservice.atomicservice.cicd.commom.util
 * 类名称:    DateUtil.java
 * 类描述:    时间工具类
 * 创建人:    WuFan
 * 创建时间:  2017/08/28 18:52
 * 版本:      v1.0
 */
public class DateUtil {

    /**
     * 简单格式日期
     */
    public static final String DATE_PLAIN_FORMAT = "yyyyMMdd";

    /**
     * 简单格式时间
     */
    public static final String TIME_PLAIN_FORMAT = "HHmmss";

    /**
     * 日期格式
     */
    public static final String DATE_TIME_PLAIN_FORMAT = DATE_PLAIN_FORMAT + "." + TIME_PLAIN_FORMAT;

    /**
     * 日期格式
     */
    public static final String DATE_TIME_FORMAT = DATE_PLAIN_FORMAT + TIME_PLAIN_FORMAT;

    /***
     * 分到秒基数
     */
    public static final long DATE_MINUTE_TO_SECONDS = 60 * 1000;

    /**
     * 将当前时间格式化
     *
     * @param date date
     * @param pattern pattern
     * @return String
     */
    public static String format(final Date date, final String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    /**
     * 将时间字符串格式化后，返回时间戳：秒
     *
     * @param dateStr dateStr
     * @param pattern pattern
     * @return Long 秒
     */
    public static Long formatDate2Second(final String dateStr, final String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            return 0L;
        }
        return date.getTime() / 1000;
    }

    /**
     * 将当前时间格式化
     *
     * @param pattern pattern
     * @return String
     */
    public static String formatDate(final String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(new Date());
    }

    /**
     * 比较两个日期(以毫秒比较)的大小
     *
     * @param d1 第一个日期
     * @param d2 第二个日期
     * @return 第一个大则返回1，第二个小则返回-1，相等返回0
     */
    public static int compare(Date d1, Date d2) {
        if (d1.getTime() > d2.getTime()) {
            return 1;
        } else if (d1.getTime() < d2.getTime()) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * 获取与指定时间相差指定N秒的时间
     * <p>
     * 如：指定时间为2012-12-18 12:14:19，天数为2 输出 2012-12-20 12:14:19
     * 指定时间为2012-12-18 12:14:19，天数为-2 输出 2012-12-16 12:14:19
     * </p>
     * @param date 指定时间，不能为<code>NULL</code>
     * @param inteval 指定天数，为整数则比指定时间晚，为负数则比指定时间早
     * @return 与指定时间相差指定天数*24小时的时间
     */
    public static Date getIntevalOfDate(Date date, long inteval) {
        if (inteval == 0) {
            return date;
        } else {
            long time = date.getTime();
            time = time + inteval;
            return new Date(time);
        }
    }

}