package com.aspire.cdn.esdatawrap.biz.generaloperate.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/** 
 *
 * 项目名称: bpm-mobile 
 * <p/>
 * 
 * 类名: AppClientInfo
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年11月23日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Data
public class AppClientInfo {
	
	@JsonProperty("client_id")
	private String clientId;		// 客户端id
	
	@JsonProperty("client_platform")
	private String clientPlatform;	// 手机平台 :   IOS | Android
	
	@JsonProperty("account_name")
	private String accountName;		// 账号名
	
	@JsonProperty("refresh_time")
	private Long   refreshTime;		// 上报时间
}
