package com.aspire.mirror.indexproxy.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * xxl-job config
 *
 * @author xuxueli 2017-04-28
 */
@Data
@ConfigurationProperties(prefix = "proxyIdentityConfig")
public class ProxyIdentityConfig {
	private String			id;
	private String			zabbixJdbcUrl;
	private String			zabbixJdbcUserName;
	private String			zabbixJdbcPasswd;
	private List<String>	zabbixMapperLocations;
	private String			zabbixUrl;
	private String			zabbixUrlName;
	private String			zabbixUrlPasswd;
	private Integer			zabbixInitPoolSize;
	private Integer			zabbixMaxPoolSize;
	private String			zabbixCmdbConditoinType;
	private String			zabbixConditoinValue;
	private String			pool;
	private String			pod;
	private String			collectRelflect;
	private String			redfishCmdbConditoinType;
	private String			redfishConditoinValue;
	private Integer			collectPoolSize;
}