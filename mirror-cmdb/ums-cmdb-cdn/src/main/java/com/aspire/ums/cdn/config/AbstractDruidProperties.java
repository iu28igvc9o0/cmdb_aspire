package com.aspire.ums.cdn.config;

import java.util.List;

import lombok.Data;

@Data
public abstract class AbstractDruidProperties {
	private String			url;
	private String			username;
	private String			password;

	private int				initialSize;
	private int				minIdle;
	private int				maxActive;
	private long			maxWait;

	private String			validationQuery;

	private boolean			testOnBorrow;

	private List<String>	mapperLocations;
}