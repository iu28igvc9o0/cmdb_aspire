package com.aspire.mirror.indexproxy.util;

import java.util.Calendar;

/** 
 *
 * 项目名称: index-proxy 
 * <p/>
 * 
 * 类名: DateUtil
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年11月5日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
public class DateUtil {
	
	/** 
	 * 功能描述: 指定epochSecond的日期是否为工作日  
	 * <p>
	 * @param epochSecond
	 * @return
	 */
	public static boolean isWorkdayByClock(Integer epochSecond) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(epochSecond * 1000L);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		return dayOfWeek != Calendar.FRIDAY && dayOfWeek != Calendar.SUNDAY;
	}
}
