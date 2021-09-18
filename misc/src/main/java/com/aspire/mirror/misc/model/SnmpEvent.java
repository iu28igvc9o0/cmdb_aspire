package com.aspire.mirror.misc.model;

import java.util.Date;

import lombok.Data;

@Data
public class SnmpEvent {
	
	/**
	 * 告警日志ID
	 */
	private String alertId;
	
	/**
	 * 所属业务系统
	 */
	private String bussinessSystem;
	
	/**
	 * 监控对象
	 */
	private String moniObject;
	
	/**
	 * 监控指标
	 */
	private String moniIndex;
	
	/**
	 * 告警级别1、高，2、中，3、低
	 */
	private String alertLevel;
	
	/**
	 * 监控类型1、网络告警2、流量告警3、安全告警4、资源告警5、业务告警6
	 */
	private String moniType;
	
	/**
	 * 告警开始时间
	 */
	private Date alertStartTime;
	
	/**
	 * 告警清除时间
	 */
	private Date removeDate;
	
	/**
	 * 告警事件状态,1处理中，2已解除，3已清除
	 */
	private Integer eventStatus;
	
	/**
	 * 告警描述
	 */
	private String alertDesc;
	
	/**
	 * 所属设备
	 */
	private String device;
	
	/**
	 * 当前监测值
	 */
	private String curMoniValue;
	
	/**
	 * 网元编码
	 */
	private String neCode;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 告警数据类型1：生成告警,2：解除告警
	 */
	private String alertStatus;
	
	private String dataVersion;
	

	
}
