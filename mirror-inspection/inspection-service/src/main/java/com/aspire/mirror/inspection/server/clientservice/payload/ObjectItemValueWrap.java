package com.aspire.mirror.inspection.server.clientservice.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
* 针对指定设备的指定模版下的监控项的封装对象    <br/>
* Project Name:collect-api
* File Name:ObjectItemValueWrap.java
* Package Name:com.aspire.mirror.collect.api.payload
* ClassName: ObjectItemValueWrap <br/>
* date: 2018年9月4日 上午11:00:29 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Data
@JsonInclude(Include.NON_EMPTY)
public class ObjectItemValueWrap {
//	@JsonProperty("device_id")
//	private String objectId;
	
	@JsonProperty("object_id")
	private String objectId;
	
	@JsonProperty("object_type")
	private String objectType;
	
	@JsonProperty("sys_item_id")
	private String itemId;				// mirror服务中的监控项id, 注意这里不是zabbix等底层系统中的itemid
	
	@JsonProperty("key_")
	private String itemKey;
	
	@JsonProperty("lastclock")
	private Integer clock;	
	
	@JsonProperty("lastns")
	private Integer ns;
	
	@JsonProperty("prevvalue")
	private String preValue;
	
	@JsonProperty("lastvalue")
	private String value;
}
