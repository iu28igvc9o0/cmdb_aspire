package com.aspire.mirror.elasticsearch.api.dto;


import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class EsHistoryReq implements Serializable {

	private static final long serialVersionUID = 1L;

	//查询条件 
	private String idcType;
	private String item;
	private float value;
	private String deviceClass;
	//统计方式
	private String bizSystem;
	//统计字段:value
	private long hostid;
	//间隔时间(选时间间隔)
	private String department2;
	//间隔单位(选时间间隔)  year,month,day,hour,week,minute
	private String host;
	private String roomId;

	private Date datetime;
	private String deviceType;
	private String department1;
	private long itemid;
	private long clock;
	private Date timestamp;
	private String podName;

}