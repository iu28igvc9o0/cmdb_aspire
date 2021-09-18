package com.aspire.mirror.alert.server.clientservice.payload;

import lombok.Data;

@Data
public class RtzAlertNotifyMeta {
	private String kafkaServers;
	private String kafkaTopic;
	private boolean isAsyc;
}
