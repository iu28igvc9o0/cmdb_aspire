package com.aspire.mirror.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期工具类
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.common.util
 * 类名称:    DateUtil.java
 * 类描述:    日期工具类
 * 创建人:    JinSu
 * 创建时间:  2018/7/30 16:17
 * 版本:      v1.0
 */
public class DateUtil {

    /** es日期格式 */
    public static final String ELASTIC_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    /** es配置文件日志时间 */
    public static final String ELASTIC_CONFIG_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public static final String DATE_TIME_CH_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 简单格式日期
     */
    public static final String DATE_PLAIN_FORMAT = "yyyyMMdd";

    /**
     * 简单格式时间
     */
    public static final String TIME_PLAIN_FORMAT = "HHmmss";

    public static final String TIME_SSS = "SSS";
    /**
     * 日期格式
     */
    public static final String DATE_TIME_PLAIN_FORMAT = DATE_PLAIN_FORMAT + "." + TIME_PLAIN_FORMAT;

    /**
     * 日期格式
     */
    public static final String DATE_TIME_FORMAT = DATE_PLAIN_FORMAT + TIME_PLAIN_FORMAT;
    /**
     * 日期格式
     */
    public static final String DATE_TIME_MS_FORMAT = DATE_PLAIN_FORMAT + TIME_PLAIN_FORMAT + TIME_SSS;
    /***
     * 分到秒基数
     */
    public static final long DATE_MINUTE_TO_SECONDS = 60 * 1000;

    /**
     * 将当前时间格式化
     *
     * @param date    date
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
     *
     * @param date    指定时间，不能为<code>NULL</code>
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

    /**
     *
     * @param endDate
     * @param nowDate
     * @return
     */
    public static String getDatePoor(Date endDate, Date nowDate) {

        if (endDate == null || nowDate == null) {
            return "";
        }
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        long sec = diff % nd % nh % nm / ns;
        return day + "d " + hour + "h " + min + "m " + sec + "s";
    }

    public static Date getDate(String time, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(time);
        } catch (ParseException e) {
            return null;
        }
    }
    public static Date getDate(String time, String pattern, String timeZone) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        df.setTimeZone(TimeZone.getTimeZone(timeZone));
        try {
            return df.parse(time);
        } catch (ParseException e) {
            return null;
        }
    }
    public static int getYear(Date d) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(d);
        return now.get(Calendar.YEAR);
    }
    public static int getMonth(Date d) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(d);
        return now.get(Calendar.MONTH) + 1;
    }

    public static Date getTime(String time) {

        if (isValid(time,"yyyyMMddHHmmss.SSS")) {
            return DateUtil.getDate(time, "yyyyMMddHHmmss.SSS");
        }
        else if (isValid(time, "yyyyMMddHHmmss")) {
            return DateUtil.getDate(time, "yyyyMMddHHmmss");
        }
        else if (isValid(time, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")) {
            return DateUtil.getDate(time, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "GMT");
        }
        else if (isValid(time, "yyyyMMddHHmm")) {
            return DateUtil.getDate(time, "yyyyMMddHHmm");
        } else if (isValid(time, "yyyyMMddHH")) {
            return DateUtil.getDate(time, "yyyyMMddHH");
        } else if (isValid(time, "yyyyMMdd")) {
            return DateUtil.getDate(time, "yyyyMMdd");
        }
        return null;
    }
    public static boolean isValid(String time, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            df.setLenient(false);
            df.parse(time);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(getTime("20181224065218.957"));
    }
}
