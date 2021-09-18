package com.aspire.mirror.log.api.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

/**
 * 事件明细角色权限约束信息
 * 项目名称: 微服务运维平台（log-api 模块）
 * 包: com.migu.tsg.microservice.monitor.log.dto.model
 * 类名称: DetailRolesPermissionsConstraintsDTO.java
 * 类描述: 事件明细角色权限约束信息
 * 创建人: sunke
 * 创建时间: 2017年8月11日 上午10:33:15
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DetailRolesPermissionsConstraintsDTO {

    @JsonProperty(value = "res_cluster")
    @SerializedName("res_cluster")
	private String resCluster;

	@JsonProperty(value = "res_project")
	@SerializedName("res_project")
	private String resProject;

	@JsonProperty(value = "res_registry")
	@SerializedName("res_registry")
	private String resRegistry;

	@JsonProperty(value = "res_registry_project")
	@SerializedName("res_registry_project")
	private String resRegistryProject;

	@JsonProperty(value = "res_space")
	@SerializedName("res_space")
	private String resSpace;

}
