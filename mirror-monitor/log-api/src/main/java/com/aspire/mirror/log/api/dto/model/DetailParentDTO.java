package com.aspire.mirror.log.api.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

/**
 * 事件资源上级资源信息
 * 项目名称: 微服务运维平台（log-api 模块）
 * 包: com.migu.tsg.microservice.monitor.log.dto.model
 * 类名称: DetailParentDTO.java
 * 类描述:  事件资源上级资源信息
 * 创建人: sunke
 * 创建时间: 2017年8月11日 上午10:30:13
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DetailParentDTO {

	@JsonProperty(value = "resource_id")
	@SerializedName("resource_id")
	private String resourceId;

	@JsonProperty(value = "resource_name")
	@SerializedName("resource_name")
	private String resourceName;

	@JsonProperty(value = "resource_type")
	@SerializedName("resource_type")
	private String resourceType;

}
