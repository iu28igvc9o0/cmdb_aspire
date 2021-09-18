package com.aspire.cdn.esdatawrap.config.model;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix="local.config.es-run-action.alert-weixin-notify")
public class AlertWeixinNotifyConfigProps {
	private Duration	runInterval;
}
