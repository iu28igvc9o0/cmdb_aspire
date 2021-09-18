package com.aspire.cdn.esdatawrap.config.model;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix="matric-alert")
public class MetricAlertConfigProps {
	private String businessSource;
	private String serviceUrl;
}
