package com.aspire.mirror.elasticsearch.api.dto;


import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class DataSetNew implements Serializable {

	private static final long serialVersionUID = 1L;


	private List<String> itemList;
	private String item;
	//private String subItemId;
	
	private List<String> countType;
	//是否统计
	private boolean countTypeFlag;
	
	private String type;

	//间隔时间(选时间间隔)
	private String intervalTime;
	//间隔单位(选时间间隔)  year,month,day,hour,week,minute
	private String intervalUnit;
	
	private Long startTime;
	
	private Long endTime;
	
	private String suffixItem;
	private String instanceId;
	
	private String mointerIndex;
	
	private String operator;
	
	private String itemType;
	
	private long operatorValue;
	

}