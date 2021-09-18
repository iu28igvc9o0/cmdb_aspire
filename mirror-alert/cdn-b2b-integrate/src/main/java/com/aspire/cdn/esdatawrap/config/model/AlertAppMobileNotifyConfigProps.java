package com.aspire.cdn.esdatawrap.config.model;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix="local.config.es-run-action.alert-app-mobile-notify")
public class AlertAppMobileNotifyConfigProps {
	private Duration	runInterval;
}
