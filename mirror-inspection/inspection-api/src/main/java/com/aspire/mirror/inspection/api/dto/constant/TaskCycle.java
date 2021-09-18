package com.aspire.mirror.inspection.api.dto.constant;
/** 
* @author ZhangSheng 
* @version 2018年9月14日 下午4:10:49 
* @describe 任务周期
*/
public enum TaskCycle {
	
	YEAR("1","YEAR"),//年
	MONTN("2","MON"),//月
	WEEK("3","WEEK"),//周
	DAY("4","DAY"),//日
	HOUR("5","HOUR"),//小时
	MIN("6","MIN"),//分钟
	CUSTOM("7","DEFINE");//自定义
	
	private TaskCycle(String key,String value){
        this.key = value;
        this.value = value;
	}
	 private final String key;
	 private final String value;
	    
	 public String getKey() {
	        return key;
	 }
	 public String getValue() {
	        return value;
	 }
	
}
