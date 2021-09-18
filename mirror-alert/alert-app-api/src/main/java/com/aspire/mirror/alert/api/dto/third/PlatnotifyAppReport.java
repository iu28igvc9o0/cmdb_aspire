package com.aspire.mirror.alert.api.dto.third;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class PlatnotifyAppReport {
	
		@JsonProperty("device_ip")
	    private String device_ip;
	    //ADD  新增 DEL 消除
	    @JsonProperty("action")
	    private String action;
	    
	    //Gacc等设备类型
	    @JsonProperty("device_type")
	    private String device_type;

	    //告警中定义的主题
	    private String subject;  
	    
	    //告警内容
	    @JsonProperty("message")
	    private String message;   
	    
	    
	    //告警详情
	    @JsonProperty("detail")
	    private String detail;   
	    
	  //告警产生的时间,格式 yyyyMMdd HH:mm:ss
	    @JsonProperty("timestamp")
	    private String timestamp; 
}
