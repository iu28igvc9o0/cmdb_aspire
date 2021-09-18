package com.aspire.mirror.elasticsearch.api.dto;


import java.io.Serializable;

import lombok.Data;

@Data
public class DataSetRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	
	private String instanceId;
	
	private String IdcType;
	
	private String itermType;
	
	private Long startTime;
	
	private Long endTime;
	
	
	private String deviceClass;
	
	private boolean countTypeFlag = true;

}