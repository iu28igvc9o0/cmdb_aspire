package com.aspire.mirror.log.api.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

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
public class DetailServicesDTO {

    @JsonProperty(value = "region_id")
    @SerializedName("region_id")
	private String regionId;

    @JsonProperty(value = "service_name")
    @SerializedName("service_name")
	private String serviceName;

    @JsonProperty(value = "unique_name")
    @SerializedName("unique_name")
	private String uniqueName;

	private String uuid;

}
