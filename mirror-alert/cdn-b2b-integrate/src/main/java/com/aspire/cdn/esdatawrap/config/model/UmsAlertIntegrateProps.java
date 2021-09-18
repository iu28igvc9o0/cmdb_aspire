package com.aspire.cdn.esdatawrap.config.model;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix="local.config.es-run-action.ums-alert-integrate")
public class UmsAlertIntegrateProps {
	private Duration	runInterval;
	private String		serviceUrl;
}
