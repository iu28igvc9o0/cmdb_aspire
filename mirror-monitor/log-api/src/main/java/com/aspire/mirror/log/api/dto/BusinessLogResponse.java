package com.aspire.mirror.log.api.dto;

import java.util.List;

import com.aspire.mirror.log.api.dto.model.BusinessLogDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 业务列表查询响应对象
 * 项目名称: 微服务运维平台（log-api 模块）
 * 包: com.migu.tsg.microservice.monitor.log.dto
 * 类名称: BusinessLogResponse.java
 * 类描述: 业务列表查询响应对象
 * 创建人: sunke
 * 创建时间: 2017年8月10日 上午10:13:31
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class BusinessLogResponse {

	@JsonProperty("total_items")
	private int totalItems;

	@JsonProperty("total_page")
	private int totalPage;

	private List<BusinessLogDTO> list;

}
