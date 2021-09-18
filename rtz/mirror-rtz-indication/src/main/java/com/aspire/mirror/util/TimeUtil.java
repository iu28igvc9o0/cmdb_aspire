package com.aspire.mirror.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class TimeUtil {

    private static final String DEFAULT_PATTERN = "yyyy-MM-dd";
    private static final String DEFAULT_PATTERN_MINU = "yyyy-MM-dd HH:mm";
    private static SimpleDateFormat sdf_yyyymmdd = new SimpleDateFormat("yyyyMMdd");

    public static String getToday(String rePattern) {
        return getSepcailTime(Calendar.DAY_OF_YEAR, null, 0, null, rePattern);
    }
    public static String getSepcailTime(int calendarType,String originTime,int scope,String pattern,String rePattern){
        String result = "";
        if(StringUtils.isBlank(pattern)) {
            pattern = DEFAULT_PATTERN;
        }

        if(StringUtils.isBlank(rePattern)) {
            rePattern = DEFAULT_PATTERN;
        }

        DateFormat orginFormat = new SimpleDateFormat(pattern);
        try {

            Date _originTime = null;
            if(StringUtils.isBlank(originTime)) {
                _originTime = new Date();
            }else{
                _originTime = orginFormat.parse(originTime);
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(_originTime);
            calendar.add(calendarType, scope);


            orginFormat = new SimpleDateFormat(rePattern);
            result = orginFormat.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public static String clearDateStrPattern(String date){
        return clearDateStrPattern(date, null, null,null);
    }

    public static String clearDateStrPattern(String date, String oldPattern, String newPattern, Integer pos) {
        String result = "";
        if(StringUtils.isBlank(date)) {
            return null;
        }
        oldPattern = StringUtils.isBlank(oldPattern) ? "[^0-9]" : oldPattern;
        newPattern = StringUtils.isBlank(newPattern) ? "" : newPattern;
        result = date.replaceAll(oldPattern, newPattern).trim();
        result = (null == pos ? result : result.substring(pos));
        return result;
    }

    public static List<String> add24hoursForOneDay(String dateTime){
        List<String> list = new ArrayList<String>();
        for(int i =0 ; i<24 ; i++){
            if(i<10){
                list.add(dateTime+"0"+i);
            }else{
                list.add(dateTime+""+i);
            }
        }
        return list;
    }
    public static List<String> get24hours(String date,String originPattern,String rePattern) {
        return getNhours(24, null,date, originPattern, null, rePattern);
    }

    public static List<String> getNhours(Integer hoursNum,String date) {
        return getNhours(hoursNum, null,date, null, null, null);
    }

    public static List<String> getNhours(Integer hoursNum,Integer _housrNum,String date,String originPattern,Integer scope,String rePattern) {
        hoursNum = (null == hoursNum ? 24 : hoursNum);
        _housrNum = (null == _housrNum ? 0 : _housrNum);
        List<String> dateList = new ArrayList<String>();
        for(int i = hoursNum; i > _housrNum; i--) {
            dateList.add(addNhour(date, originPattern, -i, rePattern));
        }
        return dateList;
    }
    public static String addNhour(String date,String originPattern,Integer scope,String rePattern) {
        originPattern = (StringUtils.isBlank(originPattern) ? "yyyyMMddHH" : originPattern);
        scope = (null == scope ? -1 : scope);
        rePattern = (StringUtils.isBlank(rePattern) ? "yyyyMMddHH" : rePattern);
        String result = "";
        try {
            DateFormat orginFormat = new SimpleDateFormat(originPattern);
            Date _originTime = null;
            if(StringUtils.isBlank(date)) {
                _originTime = new Date();
            }else{
                _originTime = orginFormat.parse(date);
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(_originTime);
            calendar.add(Calendar.HOUR_OF_DAY, scope);

            orginFormat = new SimpleDateFormat(rePattern);
            result = orginFormat.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public static List<String> getWholeDayOfMonth(String monthtime,String returnPattern){
        List<String> fullDayList = new ArrayList<String>();
        if(StringUtils.isBlank(monthtime)){
            return null;
        }
        int year = 0 ;
        int month = 0;

        year = Integer.valueOf(monthtime.substring(0, 4));
        month = Integer.valueOf(monthtime.substring(4, 6));

        SimpleDateFormat returnFormat = new SimpleDateFormat(returnPattern);

        int day = 1;
        Calendar cal = Calendar.getInstance();// 获得当前日期对象
        cal.clear();// 清除信息
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);// 1月从0开始
        cal.set(Calendar.DAY_OF_MONTH, day);// 设置为1号,当前日期既为本月第一天
        int count = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int j = 0; j <= (count-1);) {
//			if(sdf_yyyymmdd.format(cal.getTime()).equals(getLastDay(year, month)))
//				break;
            cal.add(Calendar.DAY_OF_MONTH, j == 0 ? +0 : +1);
            j++;
            fullDayList.add(returnFormat.format(cal.getTime()));
        }
        return fullDayList;
    }
    public static List<String> getDayListBetween_minu(String beginDate, String endDate, String originPattern, Integer scope, String returnPattern) {
        //获得总天数
        int totalDayNum = getTotalMinuteNumBetween(beginDate, endDate, originPattern);
        if((null == scope) || (totalDayNum <= scope)) {
            scope = totalDayNum;
        }
        if((0 >= scope) || (StringUtils.isBlank(beginDate)) || (StringUtils.isBlank(endDate))) {
            return Collections.emptyList();
        }

        List<String> dayListBetween = new ArrayList<String>(0);
        beginDate = getFormatDay(beginDate, originPattern, "yyyy-MM-dd HH:mm");
        endDate = getFormatDay(endDate, originPattern, "yyyy-MM-dd HH:mm");
        getBalanceDateList_minu(dayListBetween,beginDate, endDate,0, returnPattern,scope);
        Collections.sort(dayListBetween);
        return dayListBetween;
    }
    public static int getTotalMinuteNumBetween(String beginDate, String endDate,String pattern){
        if(StringUtils.isBlank(pattern)) {
            pattern = "yyyyMMddHHmm";
        }
        int totalDayNum = 0;
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            Calendar cal = Calendar.getInstance();
            cal.setTime(format.parse(beginDate));
            long beginTime = cal.getTimeInMillis();
            cal.setTime(format.parse(endDate));
            long endTime = cal.getTimeInMillis();
            long between_days = (endTime - beginTime) / (1000 * 60);
            totalDayNum = Integer.parseInt(String.valueOf(between_days)) + 1;
        }
        catch(NumberFormatException e) {
            e.printStackTrace();
        }
        catch(ParseException e) {
            e.printStackTrace();
        }
        return totalDayNum;
    }
    public static String getFormatDay(String dateTime,String originPattern) {
        return getFormatDay(dateTime, originPattern, null);
    }

    /**
     *
     * @方法名：getFormatDay
     * @描述：转换日期格式
     * @param dateTime
     * @param originPattern
     * @param returnPattern
     * @return
     * @输出：String
     * @作者：lhw
     *
     */
    public static String getFormatDay(String dateTime,String originPattern,String returnPattern) {
        if(StringUtils.isBlank(dateTime)) {
            return "";
        }
        if(StringUtils.isBlank(returnPattern)) {
            returnPattern = DEFAULT_PATTERN;
        }
        SimpleDateFormat format = new SimpleDateFormat(returnPattern);
        return format.format(getStrToDate(dateTime,originPattern));
    }
    public static Date getStrToDate(String dateTime,String pattern){
        Date strToDate = null;
        try {
            if(StringUtils.isBlank(dateTime)) {
                return new Date();
            }
            if(StringUtils.isBlank(pattern)) {
                pattern = DEFAULT_PATTERN;
            }
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            strToDate = format.parse(dateTime);
        }
        catch(ParseException e) {
            e.printStackTrace();
        }
        return strToDate;
    }
    private static void getBalanceDateList_minu(List<String> dayListBetween, String beginDate, String endDate,int step, String returnPattern,
                                                Integer scope) {
        if(scope > dayListBetween.size()) {
            int _scope = scope-dayListBetween.size() - 1;
            String _beginDate = getSepcialDay_miu(beginDate, step);
            dayListBetween.add(getFormatDay(_beginDate, DEFAULT_PATTERN_MINU, returnPattern));

            if(scope > dayListBetween.size()) {
                String _endDate = getSepcialDay_miu(endDate,-step);
                dayListBetween.add(getFormatDay(_endDate, DEFAULT_PATTERN_MINU, returnPattern));

                int _totalDayNum = getTotalMinuteNumBetween(_beginDate, _endDate, DEFAULT_PATTERN_MINU) - 1;
                if((_totalDayNum > 0) && (_scope > 0)) {
                    //获取递增度
                    int _step = _totalDayNum /_scope;
                    getBalanceDateList_minu(dayListBetween, _beginDate, _endDate, _step, returnPattern, scope);
                }
            }
        }
    }
    public static String getSepcialDay_miu(String originTime,int scope) {
        return getSepcailTime(Calendar.MINUTE, originTime, scope, "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm");
    }

    public static List<String> getAllMonth(String yearTime, String returnPattern){
        List<String> monthList = new ArrayList<String>();
        if(StringUtils.isBlank(yearTime)){
            return null;
        }
        int year = Integer.valueOf(yearTime) ;

        SimpleDateFormat returnFormat = new SimpleDateFormat(returnPattern);

        Calendar cal = Calendar.getInstance();// 获得当前日期对象
        cal.clear();// 清除信息
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, -1);
        for (int j = 0; j <12;j++) {
            cal.add(Calendar.MONTH, +1);
            monthList.add(returnFormat.format(cal.getTime()));
        }
        return monthList;
    }
    /**
     * 获取指定时间所属时间段
     * @param date
     * @return
     */
    public static String formatDateToPeriod(Date date) {
        String start = DateFormatUtils.format(date, "HH:00");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, 1);
        String end = DateFormatUtils.format(calendar.getTime(), "HH:00");
        return start + "-" + end;
    }

    public static String getFormatDayCN(String dateString) {
        try {
            Date date = DateUtils.parseDate(dateString, new String[] {IndicationConst.DATE_PATTERN});
            return DateFormatUtils.format(date, IndicationConst.DATE_PATTERN_CN);
        } catch (ParseException e) {
            log.error("Parse date string {} error.", dateString, e);
            return null;
        }
    }
    public static String getSepcialDay(String originTime,String pattern,int scope,String rePattern) {
        return getSepcailTime(Calendar.DAY_OF_YEAR, originTime, scope, pattern, rePattern);
    }
}
