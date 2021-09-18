package com.aspire.mirror.log.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 日志标签查询请求
 * 项目名称: 微服务运维平台（log-api 模块）
 * 包: com.migu.tsg.microservice.monitor.log.dto
 * 类名称: LogTagListRequest.java
 * 类描述: 日志标签查询请求
 * 创建人: sunke
 * 创建时间: 2017年7月29日 下午5:44:22
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class LogTagListRequest {

	//名称空间
	private String namespace;

}
