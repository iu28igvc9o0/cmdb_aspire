package com.aspire.cdn.esdatawrap.config.model;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix="ops-report")
public class OpsReportConfigProps {
	private Map<String, String> customReportDefineList;
	private Map<String, String> domainReportDefineList;
}
