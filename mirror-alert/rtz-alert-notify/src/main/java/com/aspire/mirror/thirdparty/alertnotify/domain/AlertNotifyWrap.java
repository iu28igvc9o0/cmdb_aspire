package com.aspire.mirror.thirdparty.alertnotify.domain;

import lombok.Data;

@Data
public class AlertNotifyWrap {
	private String alertTitle;
	private String alertContent;
	private Object meta;
}
