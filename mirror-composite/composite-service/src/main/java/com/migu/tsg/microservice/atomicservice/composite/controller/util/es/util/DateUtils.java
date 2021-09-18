package com.migu.tsg.microservice.atomicservice.composite.controller.util.es.util;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.FastDateFormat;

/**
* 类说明:时间处理公用类
* 项目名称:  微服务
* 包:       com.migu.tsg.microservice.monitor.log.util
* 类名称:    DateUtils.java
* 类描述:    时间处理公用类
* 创建人:    jiangfuyi
* 创建时间:  2017年7月27日
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
	 * yyyy-MM-dd
	 */
	public static final String DATA_PATTERN_DATE_NORMAL = "yyyy-MM-dd";
	/**
	 * HH:mm:ss
	 */
	public static final String DATA_PATTERN_TIME_NORMAL = "HH:mm:ss";

	/**
	 * 根据时间格式返回时间字符串 〈功能详细描述〉
	 * @param date [时间]
	 * @param pattern [格式]
	 * @return [返回日期字符串]
	 */
	public static String getDateStr(Date date, String pattern) {
	    FastDateFormat fdf = FastDateFormat.getInstance(pattern);
		return fdf.format(date);
	}
	
	public static Date parseDate(String dateStr, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
	}

	/**
	 * 时间计算
	 * @param date 基准时间,如果为空，将按照当前时间计算
	 * @param year  加减的年数
	 * @param month  加减的月数
	 * @param day  加减的天数
	 * @param hour 加减的小时数
	 * @param minute 加减分钟
	 * @param second 加减秒数
	 * @return 计算后的时间
	 */
	public static Date getDate(Date date, int year, int month, int day, int hour, int minute, int second) {
		if (date == null) {
			date = new Date();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, year);
		cal.add(Calendar.MONTH, month);
		cal.add(Calendar.DATE, day);
		cal.add(Calendar.HOUR_OF_DAY, hour);
		cal.add(Calendar.MINUTE, minute);
		cal.add(Calendar.SECOND, second);
		return cal.getTime();
	}

	/**
	 * 校验时间字符串是否符合格式要求
	 * @param str 时间字符串
	 * @param pattern 时间格式
	 * @return 符合格式要求返回true，否则返回false
	 */
	public static boolean allowPatternString(String str, String pattern) {
	    final FastDateFormat dateFormat = FastDateFormat.getInstance(pattern);
		try {
			dateFormat.parseObject(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 转换时间格式
	 * @param date 需要转换的 时间
	 * @return 指定格式的时间格式
	 */
	public static String getTimestamp(Date date) {
		return MessageFormat.format("{0}T{1}+000000",
				FastDateFormat.getInstance(DATA_PATTERN_DATE_NORMAL).format(date),
				FastDateFormat.getInstance(DATA_PATTERN_TIME_NORMAL).format(date));
	}

	/**
	 * 时间戳转换成日期格式字符串
	 * @param seconds 精确到秒的字符串
	 * @param format 格式
	 * @return
	 */
	public static String timeStamp2Date(String seconds,String format) {
		if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
			return "";
		}
		if(format == null || format.isEmpty()){
			format = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(Long.valueOf(seconds)));
	}

	public static String timeStamp2Date(String seconds) {
		return timeStamp2Date(seconds,"yyyy-MM-dd HH:mm:ss");
	}

}
