package com.aspire.mirror.inspection.api.dto.constant;
/** 
* @author ZhangSheng 
* @version 2018年8月23日 下午7:31:46 
* @describe 
*/
public enum TaskStatus {
	
	ON("1","ON"),//开启
	OFF("2","OFF");//禁用
	
	private TaskStatus(String key,String value){
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
