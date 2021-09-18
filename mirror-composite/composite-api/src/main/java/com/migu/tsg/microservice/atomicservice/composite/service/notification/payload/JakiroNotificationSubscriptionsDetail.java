package com.migu.tsg.microservice.atomicservice.composite.service.notification.payload;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JakiroNotificationSubscriptionsDetail{
	// email/webhook/sms 三种
	@JsonProperty("method")
	private String method;
	
	// 收件人
	@JsonProperty("recipient")
	private String recipient;
	
	// 链接到notification的外键
    @JSONField(name = "notification_id")
    private String notificationId;
    
    // 备注
	@JsonProperty("remark")
	private String remark;
	
	// 密码webhook 有时会需要
	@JsonProperty("secret")
	private String secret;
	
}