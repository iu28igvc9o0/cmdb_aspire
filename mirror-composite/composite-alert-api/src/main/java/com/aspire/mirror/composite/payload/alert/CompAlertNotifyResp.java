package com.aspire.mirror.composite.payload.alert;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@NoArgsConstructor
@Data
public class CompAlertNotifyResp {
	
	 
	 
    /**
     * 告警ID
     */
	@JsonProperty("alert_id")
    private String alertId;
    
    
    /**
     * 通知类型, 0:短信,1:邮件
     */
	@JsonProperty("report_type")
    private String reportType;
    
    
    /**
     * 短信/邮件 地址
     */
	@JsonProperty("destination")
    private String destination;
    
    
    /**
     * 发送状态
     * 0 失败，1 成功
     */
	@JsonProperty("status")
    private String status;
    
    /**
     * 发送时间
     */
	@JsonProperty("create_time")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    /**
     * 短信/邮件 地址
     */
    @JsonProperty("name")
    private String name;

}
