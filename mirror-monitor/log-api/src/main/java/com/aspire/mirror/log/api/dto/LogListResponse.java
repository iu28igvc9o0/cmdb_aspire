package com.aspire.mirror.log.api.dto;

import java.util.List;

import com.aspire.mirror.log.api.dto.model.LogInfoDTO;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 日志查询响应对象
 * 项目名称: 微服务运维平台（log-api 模块）
 * 包: com.migu.tsg.microservice.monitor.log.dto
 * 类名称: LogListResponse.java
 * 类描述: 日志查询响应对象
 * 创建人: sunke
 * 创建时间: 2017年7月29日 下午5:19:08
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class LogListResponse {

	// 返回日志总记录数
	@SerializedName("total_items")
	private String totalItems;

	// 返回日志总页数
	@SerializedName("total_page")
	private String totalPage;

	private List<LogInfoDTO> logs;

}
