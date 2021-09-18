package com.aspire.mirror.alert.server.util;

import com.google.common.collect.Lists;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtils {
    public static final String SHORT_DATE_PATTERN = "yyyyMMdd";
    public static final String DATE_TIME_FORMAT_DAY = "yyyy-MM-dd";
    
    public final static String DEFAULT_DATETIME_FMT = "yyyy-MM-dd HH:mm:ss";
    private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);

    
    public static String datetimeToString(String fmt, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(fmt);
        return sdf.format(date);
    }
    
    public static Date stringToDate(String fmt, String date) {
    	 SimpleDateFormat sdf = new SimpleDateFormat(fmt);
         try {
			return sdf.parse(date);
		} catch (ParseException e) {
			
			logger.error(e.getMessage());
		}
         return null;
    }
    
    // 获得当天0点时间
    public static Date getTimesmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    // 获得当天23点时间
    public static Date getTodayEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        return todayEnd.getTime();
    }

    // 获得当天近7天时间
    public static Date getWeekFromNow() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(getTimesmorning().getTime()-3600*24*1000*7);
        return cal.getTime();
    }

    // 获得本月第一天0点时间
    public static Date getTimesMonthmorning() {
        Calendar cal = Calendar.getInstance();
        Locale.setDefault(Locale.CHINA);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }
    
 // 获得指定日期上月第一天0点时间
    public static Date getTimesLastMonthmorning(Date date) {
        Calendar cal = Calendar.getInstance();
        if(null!=date) {
        	cal.setTime(date);
        }
        Locale.setDefault(Locale.CHINA);
        cal.add(Calendar.MONTH, -1);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }
    
    // 获得指定日期下月第一天0点时间
    public static Date getTimesNextMonthmorning(Date date) {
        Calendar cal = Calendar.getInstance();
        if(null!=date) {
        	cal.setTime(date);
        }
        Locale.setDefault(Locale.CHINA);
        cal.add(Calendar.MONTH, +1);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }
    //上周一0点时间
    public static Date getLaskWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        if(null!=date) {
        	cal.setTime(date);
        }
        int n = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (n == 0) {
            n = 7;
        }
        cal.add(Calendar.DATE, -(7 + (n - 1)));// 上周一的日期
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        //cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }
    
  //下周一0点时间
    public static Date getNextWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        if(null!=date) {
        	cal.setTime(date);
        }
        int n = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (n == 0) {
            n = 7;
        }
        cal.add(Calendar.DATE, (7 - (n - 1)));// 上周一的日期
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        //cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    /**
     * 本季度开始点时间
     * @return
     */
    public static Date getCurrentQuarterStartTime() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                c.set(Calendar.MONTH, 0);
            else if (currentMonth >= 4 && currentMonth <= 6)
                c.set(Calendar.MONTH, 3);
            else if (currentMonth >= 7 && currentMonth <= 9)
                c.set(Calendar.MONTH, 4);
            else if (currentMonth >= 10 && currentMonth <= 12)
                c.set(Calendar.MONTH, 9);
            c.set(Calendar.DATE, 1);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
        } catch (Exception e) {
        	logger.error(e.getMessage());
        }
        return now;
    }

    /**
     * 本年开始点时间
     * @return
     */
    public static Date getCurrentYearStartTime() {
        Calendar cal = Calendar.getInstance();
//        cal.setTime(new Date());
        // 设置月和日都为1，即为开始时间（注：月份是从0开始;日中0表示上个月最后一天，1表示本月开始第一天）
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    /**
     * 根据时间跨度获取开始时间
     * @param span
     * @return
     */
    public static Date getDateBySpan(String span) {
        Date startDate = null;
        switch (span.toLowerCase()) {
            case "day":
                startDate = getTimesmorning();
                break;
            case "week":
                startDate = DateUtils.getWeekFromNow();
                break;
            case "month":
                startDate = DateUtils.getTimesMonthmorning();
                break;
            case "season":
                startDate = DateUtils.getCurrentQuarterStartTime();
                break;
            case "year":
                startDate = DateUtils.getCurrentYearStartTime();
            default:
                break;
        }
        return startDate;
    }

    /**
     *  指定日期获取当月的每一天日期
     */
    public static List<String> getDateList(String specifiedDay){
        try {
            List<String> res = Lists.newArrayList();
            Calendar calendar = Calendar.getInstance();
            Date date = new SimpleDateFormat("yyyy-MM").parse(specifiedDay);
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            String month = calendar.get(Calendar.MONTH) + 1 < 10 ?
                    "0"+ (calendar.get(Calendar.MONTH) + 1) : String.valueOf((calendar.get(Calendar.MONTH) + 1));//月份
            int day = calendar.getActualMaximum(Calendar.DATE);
            for (int i = 1; i <= day; i++) {
                String str = i < 10 ? "0" + i : String.valueOf(i);
                String aDate = String.valueOf(year) + "-" + month + "-" + str;
                res.add(aDate);
            }
            return res;
        } catch (ParseException e) {
        	logger.error(e.getMessage());
        }
        return null;
    }

    /**
     *  指定日期的周几
     */
    public static String getSpecifiedWeek(String specifiedDay){
        try {
            Calendar calendar = Calendar.getInstance();
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
            calendar.setTime(date);
            String []arr = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
            //1.数组下标从0开始；2.老外的第一天是从星期日开始的
            return specifiedDay + " " + arr[calendar.get(Calendar.DAY_OF_WEEK)-1];
        } catch (ParseException e) {
        	logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * 获得指定日期的后一天
     */
    public static String getSpecifiedDayAfter(String specifiedDay,int num){

        try {
            Calendar c = Calendar.getInstance();
            Date date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
            c.setTime(date);
            int day=c.get(Calendar.DATE);
            c.set(Calendar.DATE,day+num);

            String dayAfter = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
            return dayAfter;
        } catch (Exception e) {
        	logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * 获取当前时间间隔几分钟的日期
     */
    public static Date getSpecifiedDay(Date date, int num){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, num);
        return calendar.getTime();
    }


    /**
     * 获取过去 第 past 天的日期
     * @param past
     * @return
     */
    public static String getPastDate(Date date, int past, SimpleDateFormat simpleDateFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, past);
        return simpleDateFormat.format(calendar.getTime());
    }


    public static String getTime(Integer integer) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar beforeTime = Calendar.getInstance();
        beforeTime.add(Calendar.MINUTE, -integer);
        Date beforeD = beforeTime.getTime();
        return sdf.format(beforeD);
    }
    
    public static String getLastMonth (String month) throws ParseException {
    	 Calendar cal = Calendar.getInstance();  
    	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
    	if(StringUtils.isNotBlank(month)) {
    	        Date sourceDate = sdf.parse(month);  
    	        cal.setTime(sourceDate);  
    	        cal.add(Calendar.MONTH, -1);  
    	       
    	}else {
    		 cal.add(Calendar.MONTH, -1);  
    	}
    	 return sdf.format(cal.getTime());
    }
    
    //获取一个月之前的一天
    public static String getMonthDay (String day) throws ParseException {
    	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	 Date dayDat = sdf.parse(day);
         Calendar cal = Calendar.getInstance();  
         cal.setTime(dayDat);  
         cal.add(Calendar.MONTH, -1);  
         return sdf.format(cal.getTime());
    }
}
