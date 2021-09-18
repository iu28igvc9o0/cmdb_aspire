package com.aspire.mirror.log.api.dto.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 事件日志分页数据结构
 * 项目名称: 微服务运维平台（log-api 模块）
 * 包: com.migu.tsg.microservice.monitor.log.dto.model
 * 类名称: EventLogListDTO.java
 * 类描述: 事件日志分页数据结构
 * 创建人: sunke
 * 创建时间: 2017年7月28日 上午11:11:21
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventLogListDTO {

    @JsonProperty(value = "total_items")
	@SerializedName("total_items")
	private int totalItems;
    
    @JsonProperty(value = "total_pages")
	@SerializedName("total_pages")
	private int totalPages;

	private List<EventInfoDTO> results;
}
