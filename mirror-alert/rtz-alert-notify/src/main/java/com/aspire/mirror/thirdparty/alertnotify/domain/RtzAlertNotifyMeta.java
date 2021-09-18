package com.aspire.mirror.thirdparty.alertnotify.domain;

import lombok.Data;

@Data
public class RtzAlertNotifyMeta {
	private String kafkaServers;
	private String kafkaTopic;
	private boolean isAsyc;
}
