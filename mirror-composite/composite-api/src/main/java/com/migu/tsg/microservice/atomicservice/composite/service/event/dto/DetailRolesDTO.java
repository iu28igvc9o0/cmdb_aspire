package com.migu.tsg.microservice.atomicservice.composite.service.event.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@JsonInclude(Include.NON_NULL)
public class DetailRolesDTO {

    @JsonProperty("created_at")
	private String createdAt;

    @JsonProperty("depends_on")
	private String dependsOn;

	private String id;

	private String name;

	private DetailRolesParentsDTO parents;

	private DetailRolesPermissionsDTO permissions;

	private String required;

	private String status;

	@JsonProperty("updated_at")
	private String updatedAt;

}
