package com.aspire.mirror.indexadapt.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "druid")
public class DruidProperties {
	private String			url;
	private String			username;
	private String			password;
	private int				initialSize;
	private int				minIdle;
	private int				maxActive;
	private long			maxWait;
	private String			validationQuery;
	private boolean			testOnBorrow;
	private boolean 		useSSL;
	private List<String>	mapperLocations;
}