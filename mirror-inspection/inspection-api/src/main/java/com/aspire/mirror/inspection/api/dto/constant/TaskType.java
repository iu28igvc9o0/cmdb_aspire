package com.aspire.mirror.inspection.api.dto.constant;
/** 
* @author ZhangSheng 
* @version 2018年9月14日 下午4:06:29 
* @describe 任务类型
*/
public enum TaskType {
	
	HANDLED("1","手动"),
	AUTOMATIC("2","自动");
	
	private TaskType(String key,String value){
        this.key = key;
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
