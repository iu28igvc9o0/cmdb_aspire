package com.aspire.mirror.indexadapt.adapt.inspectiondb.datasource;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
@ConfigurationProperties(prefix = "indexAdapter.inspectionDb.dbConfig")
@Data
public class InspectionDruidProperties {
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