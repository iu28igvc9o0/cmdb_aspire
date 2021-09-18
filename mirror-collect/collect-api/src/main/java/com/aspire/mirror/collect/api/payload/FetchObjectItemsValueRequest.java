package com.aspire.mirror.collect.api.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* 设备监控项值抓取请求对象    <br/>
* Project Name:collect-api
* File Name:FetchObjectItemsValueRequest.java
* Package Name:com.aspire.mirror.collect.api.payload
* ClassName: FetchObjectItemsValueRequest <br/>
* date: 2018年9月4日 上午11:10:35 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Data
@JsonInclude(Include.NON_EMPTY)
public class FetchObjectItemsValueRequest {

	@JsonProperty("apiServerConfigList")
	@ApiModelProperty
	private List<MonitorApiServerConfig>	apiServerConfigList;

	@JsonProperty("objectItemList")
	@ApiModelProperty
	private List<ObjectItemInfo>			objectItemList;
}
