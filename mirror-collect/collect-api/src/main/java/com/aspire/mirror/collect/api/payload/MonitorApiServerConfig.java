package com.aspire.mirror.collect.api.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
* 底层监控采集服务配置信息    <br/>
* Project Name:collect-api
* File Name:MonitorApiServerConfig.java
* Package Name:com.aspire.mirror.collect.api.payload
* ClassName: MonitorApiServerConfig <br/>
* date: 2018年9月4日 上午10:09:06 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Data
public class MonitorApiServerConfig {
	public static final String SERVER_TYPE_ZABBIX = "ZABBIX";
	private String url;
	private String username;
	private String password;
	private String room;
	@JsonProperty("server_type")
	private String serverType;
}
