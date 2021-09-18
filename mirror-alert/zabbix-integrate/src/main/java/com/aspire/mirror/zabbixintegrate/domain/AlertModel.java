package com.aspire.mirror.zabbixintegrate.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
* 完整告警对象    <br/>
* Project Name:zabbix-integrate
* File Name:AlertModel.java
* Package Name:com.aspire.mirror.zabbixintegrate.domain
* ClassName: AlertModel <br/>
* date: 2018年10月19日 下午1:44:22 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Data
@JsonInclude(Include.NON_NULL)
public class AlertModel {
	public static final String MONI_RESULT_ACTIVE = "1";
	public static final String MONI_RESULT_REVOKE = "2";
	
	private Integer	indexNum;		// 当前批次中的序号
	private String	moniResult;		// 指标监测结果 1: 新增告警     2: 解除告警

	@JsonProperty("alert_id")
	private String alertId;
//	@JsonProperty("z_alert_Id")
//	private String	zbxAlertId;		// zabbix中告警id
//
//	@JsonProperty("eventId")
//	private String	umsAlertId;		// ums告警id
	
//	@JsonProperty("z_event_Id")
//	private String	zbxEventId;		// zabbix中eventId
//	private String oldEventId;
//	private String	monitorSource;	// 第三方监控系统编码, 在第三方的告警中，当作proxyName
	private String	deviceIP;		// 所属设备
	private String	servSystem;		// 所属业务系统
//	private String	monitorRoom;	// 监控机房
	private String	monitorObject;	// 监控对象
	private String	monitorIndex;	// 监控指标
	private String	moniIndexValue;
	private String	alertLevel;		// 告警级别
	private String	alertDesc;		// 告警描述
	private String	curMoniTime;	// 当前监测时间 yyyy-MM-dd HH:mm:ss
	private String	curMoniValue;	// 当前监测值
	private String	businessSystem;	// 在Zabbix系统告警中，当作proxyName
//	private String	itemCategory;
//	private String	hostGroupId;
//	private String	hostId;
	private String	hostName;
	private String 	subject;
	@JsonProperty("z_itemId")
	private Integer	zbxItemId;
	private String source;
	/**
	 * 告警开始时间
	 */
	private String alertStartTime;
//	/**
//	 * 告警前缀，用于区分不同zabbix
//	 */
//	private String prefix;
	/**
	 * 监控对象
	 */
	private String	itemKey;
	private String keyComment;
}
