package com.aspire.mirror.collect.api.payload;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
* 根据传入的条件, 查询模版下的监控项列表    <br/>
* Project Name:collect-api
* File Name:TemplateItemListRequest.java
* Package Name:com.aspire.mirror.collect.api.payload
* ClassName: TemplateItemListRequest <br/>
* date: 2018年9月4日 上午10:26:13 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Data
@JsonInclude(Include.NON_NULL)
public class TemplateItemListRequest {
	public static final String		PARAM_TEMPLATE_ID	= "template_id";
	public static final String		PARAM_ITEM_KEY		= "item_key";
	public static final String		PARAM_ITEM_NAME		= "item_name";

	@JsonProperty("apiServerConfig")
	private MonitorApiServerConfig	apiServerConfig;

	@JsonProperty
	private Map<String, Object>		params;
	
	public Object getParamValue(String paramName) {
		return params.get(paramName);
	}
}
