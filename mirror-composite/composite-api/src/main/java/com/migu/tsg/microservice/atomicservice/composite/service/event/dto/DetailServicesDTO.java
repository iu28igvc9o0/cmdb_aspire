package com.migu.tsg.microservice.atomicservice.composite.service.event.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 事件明细服务信息
 * 项目名称: 微服务运维平台（log-api 模块）
 * 包: com.migu.tsg.microservice.monitor.log.dto.model
 * 类名称: DetailServicesDTO.java
 * 类描述: 事件明细服务信息
 * 创建人: sunke
 * 创建时间: 2017年8月11日 上午10:37:39
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(Include.NON_NULL)
public class DetailServicesDTO {

    @JsonProperty("region_id")
	private String regionId;

    @JsonProperty("service_name")
	private String serviceName;

    @JsonProperty("unique_name")
	private String uniqueName;

	private String uuid;

}
