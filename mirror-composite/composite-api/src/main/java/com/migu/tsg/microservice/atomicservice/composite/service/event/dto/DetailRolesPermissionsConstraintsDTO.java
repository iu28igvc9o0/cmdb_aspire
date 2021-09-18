package com.migu.tsg.microservice.atomicservice.composite.service.event.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@JsonInclude(Include.NON_NULL)
public class DetailRolesPermissionsConstraintsDTO {

    @JsonProperty("res_cluster")
	private String resCluster;

    @JsonProperty("res_project")
	private String resProject;

    @JsonProperty("res_registry")
	private String resRegistry;

    @JsonProperty("res_registry_project")
	private String resRegistryProject;

    @JsonProperty("res_space")
	private String resSpace;

}
