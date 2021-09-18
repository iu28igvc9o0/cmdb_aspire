package com.aspire.cdn.esdatawrap.config.model;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix="local.config.es-run-action.province-es-unlink")
public class ProvinceEsUnlinkConfigProps {
	private Integer 	intervalMinutes 	= 3;	// 扫描间隔
}
