package com.aspire.mirror.log.api.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

/**
 * 事件明细角色信息
 * 项目名称: 微服务运维平台（log-api 模块）
 * 包: com.migu.tsg.microservice.monitor.log.dto.model
 * 类名称: DetailRolesDTO.java
 * 类描述: 事件明细角色信息
 * 创建人: sunke
 * 创建时间: 2017年8月11日 上午10:32:27
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DetailRolesDTO {

    @JsonProperty(value = "created_at")
    @SerializedName("created_at")
	private String createdAt;

    @JsonProperty(value = "depends_on")
    @SerializedName("depends_on")
	private String dependsOn;

	private String id;

	private String name;

	private DetailRolesParentsDTO parents;

	private DetailRolesPermissionsDTO permissions;

	private String required;

	private String status;

	@JsonProperty(value = "updated_at")
	@SerializedName("updated_at")
	private String updatedAt;

}
