package com.aspire.mirror.inspection.server.util;


import org.springframework.util.StringUtils;

/** 
* @author ZhangSheng 
* @version 2018年8月26日 下午2:29:40 
* @describe LTS cron表达式转换类
*/
public class CronUtils {
	/**
	 * 获取Cron表达式
	 * @param type:周期类型
	 * @param date:时间字符串  (例如：1+17:30)
	 * @return
	 */
    public static String getCorn(String type,String date) {
    	StringBuilder cron =new StringBuilder();
    	if("MON".equals(type)) {
    		String [] date1 =makeDate(date);
			cron.append("0 ").append(date1[2]).append(" ")
				.append(date1[1]).append(" ").append(date1[0]).append(" * ?");
    	}else if ("WEEK".equals(type)) {
    		String [] date2 =makeDate(date);
			cron.append("0 ").append(date2[2]).append(" ").append(date2[1])
				.append(" ? *").append(" ").append(date2[0]);
		}else if ("DAY".equals(type)) {
			String [] date3=makeDate(date);
			cron.append("0 ").append(date3[1]).append(" ")
				.append(date3[0]).append(" * * ?");
		}else if ("HOUR".equals(type)) {
			cron.append("0 ").append(date).append(" * * * ?");
		}else if ("MIN".equals(type)) {
			cron.append(date).append(" * * * * ?");
		}
    	return cron.toString();
    }
     
    /**
     * 处理task 字段执行时间 字符串  
     * @param date
     * @return
     */
    public static String [] makeDate(String date) {
    	String arg []= new String[3];
    	if (!StringUtils.isEmpty(date)) {
    			arg =date.split("[\\+:]");
    	}
    	return arg;
    }
}
