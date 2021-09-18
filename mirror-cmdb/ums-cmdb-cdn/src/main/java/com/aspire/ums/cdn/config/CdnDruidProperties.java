package com.aspire.ums.cdn.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="cdn.dbConfig")
public class CdnDruidProperties extends AbstractDruidProperties {
	
}