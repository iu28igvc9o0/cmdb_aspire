package com.aspire.mirror.log.server.util;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.FastDateFormat;

import com.google.common.collect.Sets;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;

/**
 * 类说明:时间处理公用类 项目名称: 微服务 包: com.aspire.mirror.log.server.util 类名称: DateUtils.java 类描述: 时间处理公用类 创建人: jiangfuyi
 * 创建时间: 2017年7月27日
 */
public class DateUtils {

    /**
     * yyyyMMddHHmmss
     */
    public static final String DATA_PATTERN_WITHOUT_SYMBOL = "yyyyMMddHHmmss";
    /**
     * yyyyMMdd
     */
    public static final String DATA_PATTERN_DATE_SYMBOL = "yyyyMMdd";
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String DATA_PATTERN_FULL_SYMBOL = "yyyy-MM-dd HH:mm:ss";
    /**
     * yyyy-MM-dd HH:mm:ss.SSS
     */
    public static final String DATA_PATTERN_DATE_WITH_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    /**
     * yyyy-MM-dd
     */
    public static final String DATA_PATTERN_DATE_NORMAL = "yyyy-MM-dd";
    /**
     * HH:mm:ss
     */
    public static final String DATA_PATTERN_TIME_NORMAL = "HH:mm:ss";
    /**
     * 默认时区
     */
    private static final String DEFAULT_TIME_ZONE = "GMT+8";

    /**
     * 根据时间格式返回时间字符串
     * 
     * @param date
     *            [时间]
     * @param pattern
     *            [格式]
     * @return [返回日期字符串]
     */
    public static String getDateStr(Date date, String pattern) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        dateFormat.setTimeZone(TimeZone.getTimeZone(DEFAULT_TIME_ZONE));
        return dateFormat.format(date);
    }

    /**
     * 时间格式化
     * 
     * @param str
     *            需要转换的字符串
     * @param pattern
     *            时间格式
     * @return 转换后的日期
     * @throws ParseException
     *             ParseException
     */
    public static Date parseDate(String str, String pattern) throws ParseException {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        dateFormat.setTimeZone(TimeZone.getTimeZone(DEFAULT_TIME_ZONE));
        return dateFormat.parse(str);
    }

    /**
     * 时间计算
     * 
     * @param date
     *            基准时间,如果为空，将按照当前时间计算
     * @param year
     *            加减的年数
     * @param month
     *            加减的月数
     * @param day
     *            加减的天数
     * @param hour
     *            加减的小时数
     * @param minute
     *            加减分钟
     * @param second
     *            加减秒数
     * @return 计算后的时间
     */
    public static Date getDate(Date date, int year, int month, int day, int hour, int minute, int second) {
        if (date != null) {
            final Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.YEAR, year);
            cal.add(Calendar.MONTH, month);
            cal.add(Calendar.DATE, day);
            cal.add(Calendar.HOUR_OF_DAY, hour);
            cal.add(Calendar.MINUTE, minute);
            cal.add(Calendar.SECOND, second);
            return cal.getTime();
        } else {
            return new Date();
        }
    }

    /**
     * 校验时间字符串是否符合格式要求
     * 
     * @param str
     *            时间字符串
     * @param pattern
     *            时间格式
     * @return 符合格式要求返回true，否则返回false
     */
    public static boolean allowPatternString(String str, String pattern) {
        final FastDateFormat dateFormat = FastDateFormat.getInstance(pattern);
        try {
            dateFormat.parseObject(str);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * 转换时间格式
     * 
     * @param date
     *            需要转换的 时间
     * @return 指定格式的时间格式
     */
    public static String getTimestamp(Date date) {
        return MessageFormat.format("{0}T{1}+000000", FastDateFormat.getInstance(DATA_PATTERN_DATE_NORMAL)
                .format(date), FastDateFormat.getInstance(DATA_PATTERN_TIME_NORMAL).format(date));
    }

    /**
     * 计算两个日期之间相差的天数
     * 
     * @param smdate
     *            较小的时间
     * @param bdate
     *            较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone(DEFAULT_TIME_ZONE));
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        final Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        final long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        final long time2 = cal.getTimeInMillis();
        final long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 获取当前时间 格式yyyyMMddHHmmss
     * @param format 格式
     * @return [返回日期字符串]
     */
    public static String getNowDateStr(String format) {
        final LocalDateTime now = LocalDateTime.now();
        final ZoneId zone = ZoneId.of(ZoneId.SHORT_IDS.get("CTT"));
        final ZonedDateTime date = ZonedDateTime.of(now, zone);
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return formatter.format(date);
    }
    
    
    /**
    *
    * @param execObject
    * @param fromTime
    * @param toTime
    * @return
    */
    public static String getExecIndexs (String execObject, Date fromTime, Date toTime) {
        String execIndexs = execObject;
        if (execObject.matches("^.*\\{.+\\}.*$")) {
            String format = execObject.replaceAll("^.*\\{", "").replaceAll("\\}.*$", "");
            try {
                long duration = checkDuration(format, fromTime);
                Set<String> set = Sets.newHashSet();
                long time = fromTime.getTime();
                set.add(getExecIndex(execObject, time, format));
                long toTimeMs = toTime.getTime();
                set.add(getExecIndex(execObject, toTimeMs, format));
                time += duration;
                while (toTimeMs > time) {
                    set.add(getExecIndex(execObject, time, format));
                    time += duration;
                }
                execIndexs = org.apache.commons.lang.StringUtils.join(set, ",");
            } catch (Exception e) {
                execIndexs = execObject.replaceAll("\\{", "").replaceAll("\\}", "");
            }
        }
        return execIndexs;
    }

    /**
     * 生成执行索引
     * @param execObject
     * @param l
     * @param format
     * @return
     */
    public static String getExecIndex(String execObject, long l, String format) {
        String formatDate = DateFormatUtils.format(l, format);
        String execIndex = execObject.replaceAll("\\{" + format + "\\}", formatDate);
        return execIndex;
    }

    /**
     * 时间范围最小以1小时为单位
     * @param format
     * @param fromTime
     * @return
     */
    public static long checkDuration (String format, Date fromTime) {
        long duration = 1L;
        long time = fromTime.getTime();
        String formatDate1 = DateFormatUtils.format(fromTime, format);
        long time1 = time + duration * 3600 * 1000;
        String formatDate2 = DateFormatUtils.format(time1, format);
        if (!formatDate1.equals(formatDate2)) {
            time1 = time1 + duration * 3600 * 1000;
            String formatDate3 = DateFormatUtils.format(time1, format);
            if (!formatDate3.equals(formatDate2)) {
                return duration * 3600 * 1000;
            }
        }
        duration = 24L;
        time1 = time + duration * 3600 * 1000;
        String formatDate4 = DateFormatUtils.format(time1, format);
        if (!formatDate1.equals(formatDate4)) {
            time1 = time1 + duration * 3600 * 1000;
            String formatDate5 = DateFormatUtils.format(time1, format);
            if (!formatDate5.equals(formatDate4)) {
                return duration * 3600 * 1000;
            }
        }
        duration = 28L * 24;
        return duration * 3600 * 1000;
    }
}