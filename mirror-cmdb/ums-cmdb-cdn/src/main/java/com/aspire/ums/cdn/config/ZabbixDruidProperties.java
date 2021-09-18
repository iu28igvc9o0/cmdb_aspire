package com.aspire.ums.cdn.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="zabbix.dbConfig")
public class ZabbixDruidProperties extends AbstractDruidProperties {
	
}