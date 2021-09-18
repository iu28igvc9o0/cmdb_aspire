package com.aspire.mirror.indexproxy.domain;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
* 模版下监控项的详情响应对象    <br/>
* Project Name:collect-api
* File Name:MonitorItemDetailResponse.java
* Package Name:com.aspire.mirror.collect.api.payload
* ClassName: MonitorItemDetailResponse <br/>
* date: 2018年9月4日 上午11:18:51 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Data
@JsonInclude(Include.NON_EMPTY)
public class MonitorItemDetailResponse {
	@JsonProperty("name")
	private String itemName;
	
	@JsonProperty("type")
	private String type;
	
	@JsonProperty("key_")
	private String itemKey;
	
	@JsonProperty("delay")
	private String delay;
	
	@JsonProperty("history")
	private Integer history;

	@JsonProperty("flags")
	private Integer flags;
	
	@JsonProperty("value_type")
	private String valueType;
	
	@JsonProperty("units")
	private String units;
	
	@JsonProperty("data_type")
	private String dataType;

	@JsonProperty("itemid")
	private String itemId;

	@JsonProperty("templateid")
	private String templateId;

	@JsonProperty("itemDiscovery")
	private Object itemDiscovery;

	@Override
	public boolean equals(Object obj) {
		if (!this.getClass().isInstance(obj)) {
			return false;
		}
		MonitorItemDetailResponse other = this.getClass().cast(obj);
		if (this.getItemKey() == null) {
			return false;
		}
		return this.getItemKey().equals(other.getItemKey());
	}
	
	@Override
	public int hashCode() {
		int hash = 17;
		if (this.getItemKey() != null) {
			hash += 13 * this.getItemKey().hashCode(); 
		}
		return hash;
	}
}
