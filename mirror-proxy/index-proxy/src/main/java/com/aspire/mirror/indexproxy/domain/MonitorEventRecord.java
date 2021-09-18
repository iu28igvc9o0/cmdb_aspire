package com.aspire.mirror.indexproxy.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class MonitorEventRecord extends BasicDataOperateAware implements Serializable {
	private static final long serialVersionUID = 3905883554589933246L;

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
	
	private String value;	// 0  正常 1 异常   参考  com.aspire.mirror.indexproxy.indexprocess.Consts.EVENT_VALUE
	private String acknowledged;
	private Integer clock;
	private Integer ns;
}
