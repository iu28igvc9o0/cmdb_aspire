package com.aspire.fileCheck.util;

import org.apache.commons.lang.time.DateFormatUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jw.zhu
 * Date: 2018/11/27
 * 软探针异常指标监控系统
 * com.aspire.fileCheck.util.TimeUtil
 */
public class TimeUtil {
	public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
	public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_YYMMDD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	public static final String DATE_FORMAT_YYYYMMDDHHMM = "yyyyMMddHHmm";
    public static final String DATE_FORMAT_YYYYMMDDHH = "yyyyMMddHH";
    /**
     * 获取指定时间所属时间段
     * @param date
     * @return
     */
    public static String formatDateToPeriod(Date date) {
        String start = DateFormatUtils.format(date, FileConst.PERIOD_PATTERN);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, 1);
        String end = DateFormatUtils.format(calendar.getTime(), FileConst.PERIOD_PATTERN);
        return start + "-" + end;
    }

    /**
     * 获取指定时间, 需要计算的时间段
     * @param date
     * @return
     */
    public static String getCalcPeriod(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, - FileConst.PERIOD_LAZY_HOUR);
        calendar.add(Calendar.HOUR, - FileConst.CALC_LAZY_HOUR);
        return formatDateToPeriod(calendar.getTime());
    }

    /**
     * 获取昨天的当前日期
     * @param date
     * @return
     */
    public static String getCalcData(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, - FileConst.PERIOD_LAZY_HOUR);
        calendar.add(Calendar.HOUR, - FileConst.CALC_LAZY_HOUR);
        String start = DateFormatUtils.format(calendar.getTime(), "yyyyMMdd");
        calendar.add(Calendar.HOUR, FileConst.CALC_LAZY_HOUR);
        return start;
    }

    /**
     * 获取需要计算的时间段
     * @param date
     * @return
     */
    public static List<String> getNeedCalcPeriod(Date date) {
        List<String> periodList = new ArrayList<String>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, - FileConst.PERIOD_LAZY_HOUR);
        calendar.add(Calendar.HOUR, - FileConst.CALC_LAZY_HOUR);
        periodList.add(0, DateFormatUtils.format(calendar.getTime(), DATE_FORMAT_YYYYMMDDHH));
        for (int i=1; ;) {
            if (calendar.get(Calendar.HOUR_OF_DAY) - i >= 0) {
                calendar.add(Calendar.HOUR, - i);
                periodList.add(0, DateFormatUtils.format(calendar.getTime(), DATE_FORMAT_YYYYMMDDHH));
            } else {
                break;
            }
        }
        return periodList;
    }

    /**
     * 获取指定小时数的毫秒数
     * @param hours
     * @return
     */
    public static long getHourInMillis(Integer hours) {
        return hours * 60 * 60 * 1000;
    }

    public static void main(String[] a) {
        System.out.println(getNeedCalcPeriod(new Date()));
    }

    public static String getMinutePeriod(String hour, int minute) {
        if (minute > 0 && minute <= 15) {
            return hour + ":00-" + hour + ":15";
        }
        if (minute > 15 && minute <= 30) {
            return hour + ":15-" + hour + ":30";
        }
        if (minute > 30 && minute <= 45) {
            return hour + ":30-" + hour + ":45";
        }
        int nextHour = Integer.parseInt(hour) + 1;
        String nextHourS = nextHour < 10 ? ("0" + nextHour) : ("" + nextHour);
        return hour + ":45-" + nextHourS + ":00";
    }

    public static String[] getHourPeriod(String hour) {
        int nextHour = Integer.parseInt(hour) + 1;
        String nextHourS = nextHour < 10 ? ("0" + nextHour) : ("" + nextHour);
        return new String[] {
                hour + ":00-" + hour + ":15",
                hour + ":15-" + hour + ":30",
                hour + ":30-" + hour + ":45",
                hour + ":45-" + nextHourS + ":00"
        };
    }
}
