package com.aspire.ums.cmdb.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.springframework.util.Assert;

public class DateUtilsNew {

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

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String TIME_FORMAT = "HH:mm:ss";

    public static final String DATE_TIME_FORMAT2 = DATE_FORMAT + " " + TIME_FORMAT;

    /**
     * 将时间字符串格式化后，返回时间戳：秒
     *
     * @param dateStr
     *            dateStr
     * @param pattern
     *            pattern
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
     * @Author ZhangSheng
     * @Description 将HH:ss格式的字符串转换成时间格式
     * @Date 16:29 2019/5/27
     * @Param [dateStr]
     * @return java.util.Date
     **/
    public static Date formatDateStrToDate(final String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        try {
            return format.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取格式化后的日期.
     *
     * @param
     * @return
     */
    public static Date parseStrToDate(String dateStr, String datePattern) {
        SimpleDateFormat format = new SimpleDateFormat(datePattern);
        try {
            return format.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将当前时间格式化
     *
     * @param pattern
     *            pattern
     * @return String
     */
    public static String formatDate(final String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(new Date());
    }

    /**
     * 比较两个日期(以毫秒比较)的大小
     *
     * @param d1
     *            第一个日期
     * @param d2
     *            第二个日期
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
     * 如：指定时间为2012-12-18 12:14:19，天数为2 输出 2012-12-20 12:14:19 指定时间为2012-12-18 12:14:19，天数为-2 输出 2012-12-16 12:14:19
     * </p>
     *
     * @param date
     *            指定时间，不能为<code>NULL</code>
     * @param inteval
     *            指定天数，为整数则比指定时间晚，为负数则比指定时间早
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
     * 将日期转换为日时分秒
     *
     * @param date
     * @return
     */
    public static String dateToTime(String date, String dateStyle) {
        SimpleDateFormat format = new SimpleDateFormat(dateStyle);
        try {
            Date oldDate = format.parse(date);
            long time = oldDate.getTime(); // 输入日期转换为毫秒数
            long nowTime = System.currentTimeMillis(); // 当前时间毫秒数
            long second = nowTime - time; // 二者相差多少毫秒
            second = second / 1000; // 毫秒转换为妙
            long days = second / 86400;
            second = second % 86400;
            long hours = second / 3600;
            second = second % 3600;
            long minutes = second / 60;
            second = second % 60;
            // if (days > 0) {
            return days + "d " + hours + "h " + minutes + "m " + second + "s";
            // } else {
            // return hours + "h " + minutes + "m " + second + "s";
            // }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 转换为时间（天,时:分:秒.毫秒）
     *
     * @param timeMillis
     * @return
     */
    public static String formatDateTime(long timeMillis) {
        long day = timeMillis / (24 * 60 * 60 * 1000);
        long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
        long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
        return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "." + sss;
    }

    @Test
    public void testDateToTime() {
        String dateStr = dateToTime("2019-01-11 10:50:23", "yyyy-MM-dd HH:mm:ss");
        System.out.println(dateStr);
    }

    /**
     * @description 计算时间段内的天数
     * @param startTimeStr
     *            开始时间
     * @param endTimeStr
     *            结束时间
     * @param datePattern
     *            时间格式
     * @return 时间天数
     */
    public static int getDaysBetween(String startTimeStr, String endTimeStr, String datePattern) {
        LocalDate startTime = LocalDate.parse(startTimeStr, DateTimeFormatter.ofPattern(datePattern));
        LocalDate endTime = LocalDate.parse(endTimeStr, DateTimeFormatter.ofPattern(datePattern));
        return (int) ChronoUnit.DAYS.between(startTime, endTime);
    }

    /**
     * 根据输入的format格式，以及format字符串，返回对应的日期
     *
     * @param pattern
     * @param dateFormatStr
     * @return
     * @throws ParseException
     */
    public static Date getDataByFormatString(String pattern, String dateFormatStr) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = simpleDateFormat.parse(dateFormatStr);
        return date;
    }

    /**
     *
     * @param pattern
     * @param date
     * @return
     */
    public static String getFormatStrByPatternAndDate(String pattern, Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String formatStr = simpleDateFormat.format(date);
        return formatStr;
    }

    /**
     * 获取今天的开始时间
     *
     * @param
     * @return
     */
    public static String getTodayStartTime() {
        LocalDateTime today_start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        return today_start.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT2));
    }

    /**
     * 获取今天的结束时间
     *
     * @param
     * @return
     */
    public static String getTodayEndTime() {
        LocalDateTime today_start = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        return today_start.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT2));
    }

    /**
     * 获取月第一天的开始时间.
     *
     * @param
     * @return
     */
    public static String getMonthStartTime(int year, int month) {
        // 上月的第一天
        LocalDate firstday = LocalDate.of(year, month, 1);
        LocalDateTime today_start = LocalDateTime.of(firstday, LocalTime.MIN);
        return today_start.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT2));
    }

    /**
     * 最后一天的结束时间.
     *
     * @param
     * @return
     */
    public static String getMonthEndTime(int year, int month) {
        LocalDate localDate = LocalDate.of(year, month, 1);
        // 上月的最后一天
        LocalDate lastDay = localDate.with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime today_start = LocalDateTime.of(lastDay, LocalTime.MAX);
        return today_start.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT2));
    }

    /**
     * 获取本月第一天的开始时间.
     *
     * @param
     * @return
     */
    public static String getThisMonthStartTime() {
        LocalDate localDate = LocalDate.now();
        // 上月的第一天
        LocalDate firstday = LocalDate.of(localDate.getYear(), localDate.getMonth(), 1);
        LocalDateTime today_start = LocalDateTime.of(firstday, LocalTime.MIN);
        return today_start.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT2));
    }

    /**
     * 本月最后一天的结束时间.
     *
     * @param
     * @return
     */
    public static String getThisMonthEndTime() {
        LocalDate localDate = LocalDate.now();
        // 上月的最后一天
        LocalDate lastDay = localDate.with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime today_start = LocalDateTime.of(lastDay, LocalTime.MAX);
        return today_start.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT2));
    }

    /**
     * 获取一年后的结束时间
     *
     * @param
     * @return
     */
    public static String getNextYearEndTime() {
        LocalDate nextYear = LocalDate.now().plusYears(1);
        LocalDateTime today_start = LocalDateTime.of(nextYear, LocalTime.MAX);
        return today_start.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT2));
    }

    public static String getTodayStartDate() {
        LocalDateTime today_start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        return today_start.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public static String getTodayEndDate() {
        LocalDateTime today_start = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        return today_start.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    /**
     * 将字符串转日期成Long类型的时间戳，格式为：yyyy-MM-dd HH:mm:ss
     */
    public static Long convertTimeToLong(String time) {
        Assert.notNull(time, "time is null");
        DateTimeFormatter ftf = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT2);
        LocalDateTime parse = LocalDateTime.parse(time, ftf);
        return LocalDateTime.from(parse).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static String getYesterdayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date time = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(time);
    }

    /**
     * 获取两个日期间隔的所有日期
     * 
     * @param start
     *            开始时间
     * @param end
     *            结束时间
     * @return
     */
    public static List<String> getBetweenDate(String start, String end, String datePattern) {
        List<String> list = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        LocalDate startDate = LocalDate.parse(start, formatter);
        LocalDate endDate = LocalDate.parse(end, formatter);

        long distance = ChronoUnit.DAYS.between(startDate, endDate);
        if (distance < 1) {
            return list;
        }
        Stream.iterate(startDate, d -> {
            return d.plusDays(1);
        }).limit(distance + 1).forEach(f -> {
            list.add(f.toString());
        });
        return list;
    }

    /**
     * 获取两个日期间隔的所有月份
     *
     * @param start
     *            开始时间
     * @param end
     *            结束时间
     * @return
     */
    public static List<String> getMonthBetweenDate(String start, String end, String datePattern) {
        List<String> list = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        LocalDate startDate = LocalDate.parse(start, formatter);
        LocalDate endDate = LocalDate.parse(end, formatter);

        long distance = ChronoUnit.MONTHS.between(startDate, endDate);
        if (distance < 1) {
            return list;
        }
        Stream.iterate(startDate, d -> {
            return d.plusMonths(1);
        }).limit(distance + 1).forEach(f -> {
            list.add(f.getYear() + "-" + f.getMonthValue());
        });
        return list;
    }

    private static final ConcurrentMap<String, DateTimeFormatter> FORMATTER_CACHE = new ConcurrentHashMap<>();

    private static final int PATTERN_CACHE_SIZE = 500;

    /**
     * Date转换为格式化时间
     * 
     * @param date
     *            date
     * @param pattern
     *            格式
     * @return
     */
    public static String format(Date date, String pattern) {
        return format(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()), pattern);
    }

    /**
     * localDateTime转换为格式化时间
     * 
     * @param localDateTime
     *            localDateTime
     * @param pattern
     *            格式
     * @return
     */
    public static String format(LocalDateTime localDateTime, String pattern) {
        DateTimeFormatter formatter = createCacheFormatter(pattern);
        return localDateTime.format(formatter);
    }

    /**
     * 格式化字符串转为Date
     * 
     * @param time
     *            格式化时间
     * @param pattern
     *            格式
     * @return
     */
    public static Date parseDate(String time, String pattern) {
        return Date.from(parseLocalDateTime(time, pattern).atZone(ZoneId.systemDefault()).toInstant());

    }

    /**
     * 格式化字符串转为LocalDateTime
     * 
     * @param time
     *            格式化时间
     * @param pattern
     *            格式
     * @return
     */
    public static LocalDateTime parseLocalDateTime(String time, String pattern) {
        DateTimeFormatter formatter = createCacheFormatter(pattern);
        return LocalDateTime.parse(time, formatter);
    }

    /**
     * 在缓存中创建DateTimeFormatter
     * 
     * @param pattern
     *            格式
     * @return
     */
    private static DateTimeFormatter createCacheFormatter(String pattern) {
        if (pattern == null || pattern.length() == 0) {
            throw new IllegalArgumentException("Invalid pattern specification");
        }
        DateTimeFormatter formatter = FORMATTER_CACHE.get(pattern);
        if (formatter == null) {
            if (FORMATTER_CACHE.size() < PATTERN_CACHE_SIZE) {
                formatter = DateTimeFormatter.ofPattern(pattern);
                DateTimeFormatter oldFormatter = FORMATTER_CACHE.putIfAbsent(pattern, formatter);
                if (oldFormatter != null) {
                    formatter = oldFormatter;
                }
            }
        }

        return formatter;
    }

    /**
     * Date -> LocalDate
     * 
     * @param date
     * @return
     */
    public static LocalDate transDateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Date -> LocalDateTime
     * 
     * @param date
     * @return
     */
    public static LocalDateTime transDateToLocalDateTime(Date date) {
        // return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * Date -> LocalTime
     * 
     * @param date
     * @return
     */
    public static LocalTime transDateToLocalTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }

    /**
     * LocalDate->Date
     * 
     * @param localDate
     * @return
     */
    public static Date transLocalDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalDateTime -> Date
     * 
     * @param localDateTime
     * @return
     */
    public static Date transLocalDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalTime -> Date
     * 
     * @param localTime
     * @return
     */
    public static Date transLocalTimeToDate(LocalTime localTime) {
        return Date.from(LocalDateTime.of(LocalDate.now(), localTime).atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取当前时间，格式化：yyyy-MM-dd HH:mm:ss
     * 
     * @param
     * @return
     */
    public static String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT2));
    }

    public static LocalDate parseStr2LocalDate(String str) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parse = LocalDate.parse(str, dtf);
        return parse;
    }

    public static LocalDateTime parseStr2LocalDateTime(String str) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parse = LocalDateTime.parse(str, dtf);
        return parse;
    }

    public static void main(String[] args) {
        // System.out.println(getTodayStartTime());
        // System.out.println(getTodayEndTime());
        // String endTime = getNextYearEndTime();
        // Long time = convertTimeToLong(endTime);
        // System.out.println(time);
        // System.out.println(new Date(time));

        // System.out.println(getMonthStartTime(2000, 2));
        // System.out.println(getMonthEndTime(2000, 2));
        String minTime = "2019-11-01 00:24:12";
        String maxTime = "2019-12-04 13:35:40";
        // System.out.println(getMonthBetweenDate(startTime,endTime,DATE_TIME_FORMAT2));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate startDate = LocalDate.parse(minTime, formatter);
        LocalDate endDate = LocalDate.parse(maxTime, formatter);
        List<String> monthList = getMonthBetweenDate(minTime, maxTime, DATE_TIME_FORMAT2);
        String startYearMonth = startDate.getYear() + "-" + startDate.getMonthValue();
        String endYearMonth = endDate.getYear() + "-" + endDate.getMonthValue();
        if (CollectionUtils.isEmpty(monthList) || !monthList.contains(startYearMonth)) {
            monthList.add(startYearMonth);
        }
        if (CollectionUtils.isEmpty(monthList) || !monthList.contains(endYearMonth)) {
            monthList.add(endYearMonth);
        }
        if (CollectionUtils.isEmpty(monthList)) {
            return;
        }
        int i = 0;
        for (String yearMonth : monthList) {
            String yearStr = yearMonth.split("-")[0];
            String monthStr = yearMonth.split("-")[1];
            monthStr = org.apache.commons.lang3.StringUtils.leftPad(monthStr, 2, "0");
            int year = Integer.parseInt(yearStr);
            int month = Integer.parseInt(monthStr);
            System.out.println(year);
            System.out.println(month);
        }
    }
}
