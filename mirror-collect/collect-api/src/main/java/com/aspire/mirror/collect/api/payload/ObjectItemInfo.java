package com.aspire.mirror.collect.api.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.io.Serializable;

/**
* 针对指定设备的指定模版下的监控项的封装对象    <br/>
* Project Name:collect-api
* File Name:ObjectItemInfo.java
* Package Name:com.aspire.mirror.collect.api.payload
* ClassName: ObjectItemInfo <br/>
* date: 2018年9月4日 上午11:00:29 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Data
public class ObjectItemInfo implements Serializable {
	public static final String	API_SERVER_ZABBIX		= "ZABBIX";
	public static final String	API_SERVER_SCRIPT		= "SCRIPT";
	public static final String	API_SERVER_MIRROR		= "MIRROR";
	public static final String	API_SERVER_THEME		= "THEME";
	public static final String	API_SERVER_OPERNFALCON	= "OPENFALCON";
	public static final String	API_SERVER_PROMETHEUS	= "PROMETHEUS";

	public static final String	OBJECT_TYPE_DEVICE		= "1";
	public static final String	OBJECT_TYPE_BIZ			= "2";
	
//	@JsonProperty("device_id")
//	private String deviceId;
	@JsonProperty("report_id")
	private String reportId;

	@JsonProperty("object_id")
	private String objectId;
	
	@JsonProperty("object_type")
	private String objectType;

	@JsonProperty("extend_obj")
	private String extendObj;		// 扩展字段
	
	@JsonProperty("room")
	private String room;
	
	@JsonProperty("sys_item_id")
	private String itemId;
	
	@JsonProperty("item_name")
	private String itemName;
	
	@JsonProperty("item_key")
	private String itemKey;

	@JsonProperty("script_param")
	private String scriptParam;

	@JsonProperty("customize_param")
	private String customizeParam;

	@JsonProperty("api_server_type")
	private String apiServerType;		// MIRROR, ZABBIX, OPENFALCON, PROMETHEUS
	
	// other props for specified API server implementation.
	@JsonIgnore
	private Integer hostId;
}
