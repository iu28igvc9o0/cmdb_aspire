package com.aspire.mirror.alert.server.clientservice.payload;

import lombok.Data;

@Data
public class AlertNotifyWrap {
	private String alertTitle;
	private String alertContent;
	private Object meta;
}
