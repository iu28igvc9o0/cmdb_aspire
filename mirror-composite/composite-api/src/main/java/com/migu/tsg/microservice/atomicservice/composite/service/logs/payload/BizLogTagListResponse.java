package com.migu.tsg.microservice.atomicservice.composite.service.logs.payload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 日志标签查询响应
 * 项目名称: 微服务运维平台（log-api 模块）
 * 包: com.migu.tsg.microservice.monitor.log.dto
 * 类名称: LogTagListResponse.java
 * 类描述: 日志标签查询响应
 * 创建人: jiangfuyi
 * 创建时间: 2017年12月29日 下午5:45:04
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class BizLogTagListResponse {

	//域标签列表
	private List<String> clusters;

	//域标签列表
	private List<String> services;

	//节点标签列表
	private List<String> nodes;

	//输出标签列表
	private List<String> levels;

}
