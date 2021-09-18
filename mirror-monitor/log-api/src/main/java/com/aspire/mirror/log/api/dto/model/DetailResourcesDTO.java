package com.aspire.mirror.log.api.dto.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 事件明细资源信息
 * 项目名称: 微服务运维平台（log-api 模块）
 * 包: com.migu.tsg.microservice.monitor.log.dto.model
 * 类名称: DetailResourcesDTO.java
 * 类描述: 事件明细资源信息
 * 创建人: sunke
 * 创建时间: 2017年8月11日 上午10:30:53
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DetailResourcesDTO {

    @JsonProperty(value = "created_at")
	@SerializedName("created_at")
	private String createdAt;

	private String id;

	private String name;

	@JsonProperty(value = "parent_resource")
	@SerializedName("parent_resource")
	private DetailResourcesParentDTO parentResource;

	private String required;

	private String status;

	private String type;

	@JsonProperty(value = "updated_at")
	@SerializedName("updated_at")
	private String updatedAt;

}
