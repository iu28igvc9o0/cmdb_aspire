package com.aspire.mirror.misc.model;

import java.util.Date;

import lombok.Data;

@Data
public class SnmpEventHis {

	private String alertId;
	private Date createTime;
	private String alertStatus;
	
}
