package com.aspire.mirror.indexadapt.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MonitorEventRecord {
	@JsonProperty("event_id")
	private String eventId;
	
	@JsonProperty("source_type")
	private String sourceType;
	
	@JsonProperty("source_id")
	private String sourceId;
	
	@JsonProperty
	private String source;
	
	@JsonProperty("object_type")
	private String objectType;
	
	@JsonProperty("object_id")
	private String objectId;
	
	@JsonProperty
	private String object;	// 内容格式为json的字符串, 具体业务需要的数据可以存放到这里
	
	private String value;	// 0  正常 1 异常   参考  com.aspire.mirror.indexadapt.indexprocess.Consts.EVENT_VALUE
	private String acknowledged;
	private Integer clock;
	private Integer ns;
}
