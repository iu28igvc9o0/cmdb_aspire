package com.aspire.cdn.esdatawrap.config.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix="local.config.es-run-action")
public class FocusDomainConfigProps {
	private final List<String> 	focusPriorityDomainList = new ArrayList<>();	// 强制匹配“严重”告警级别的域名列表
}
