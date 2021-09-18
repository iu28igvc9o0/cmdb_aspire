package com.aspire.mirror.zabbixintegrate.domain;

import lombok.Data;

@Data
public class RawAlert {
	private String				alertId;

	private String				eventId;

	private String				subject;
	
	private Integer				clock;			

	private String				message;

	private String actionId;
}
