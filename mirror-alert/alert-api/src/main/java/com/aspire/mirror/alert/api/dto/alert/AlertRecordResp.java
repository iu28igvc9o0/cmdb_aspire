package com.aspire.mirror.alert.api.dto.alert;

 

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AlertRecordResp {
	
	 
    /**
     * 告警ID
     */
	@JsonProperty("alert_id")
    private String alertId;
    
    /**
     * 操作人名称
     */
	@JsonProperty("user_name")
    private String userName;
    
    /**
     * 操作类型
     */
	@JsonProperty("operation_type")
    private String operationType;
     
    /**
     * 操作时间
     */
	@JsonProperty("operation_time")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date operationTime;

    
    /**
     * 操作状态
     */
	@JsonProperty("operation_status")
    private String operationStatus;
    
    /**
     * 操作内容
     */
	@JsonProperty("content")
    private String content;

	 
	

}
