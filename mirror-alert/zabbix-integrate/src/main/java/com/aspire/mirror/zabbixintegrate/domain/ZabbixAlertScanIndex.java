package com.aspire.mirror.zabbixintegrate.domain;

import java.sql.Timestamp;

import lombok.Data;

/**
* 监控事件扫描游标对象    <br/>
* Project Name:zabbix-integrate
* File Name:ZabbixAlertScanIndex.java
* Package Name:com.aspire.mirror.zabbixintegrate.domain
* ClassName: ZabbixAlertScanIndex <br/>
* date: 2018年10月18日 下午7:07:57 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Data
public class ZabbixAlertScanIndex {
	public static final Integer	ALERT_ID		= 1;
	public static final Integer	ITEM_ID		= 2;
	public static final Integer	TRIGGER_ID		= 3;
	public static final Integer	TRENDS_ID		= 4;
	private Integer				id;	// 记录id, 由于只存在一条记录, 固化为1
	private Long				 scanIndex;				// 扫描游标
	private Timestamp			updateTime;				// 更新时间
}
