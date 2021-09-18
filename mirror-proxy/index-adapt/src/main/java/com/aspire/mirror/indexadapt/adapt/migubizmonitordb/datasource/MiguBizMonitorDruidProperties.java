package com.aspire.mirror.indexadapt.adapt.migubizmonitordb.datasource;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "indexAdapter.migubizmonitorDb.dbConfig")
public class MiguBizMonitorDruidProperties {
	private String url;
	private String username;
	private String password;

	private int initialSize;
	private int minIdle;
	private int maxActive;
	private long maxWait;

	private String validationQuery;

	private boolean testOnBorrow;
	
	private List<String> mapperLocations;
}