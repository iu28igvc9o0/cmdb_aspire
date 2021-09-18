package com.aspire.mirror.log.api.dto;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 日志查询请求
 * 项目名称: 微服务运维平台（log-api 模块）
 * 包: com.migu.tsg.microservice.monitor.log.dto
 * 类名称: LogListRequest.java
 * 类描述: 日志查询请求
 * 创建人: sunke
 * 创建时间: 2017年7月29日 下午5:41:32
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class LogQueryRequest {

	// 查询开始时间 日志查询开始时间的时间戳
	@SerializedName("start_time")
	private long startTime;

	// 查询结束时间 日志查询结束时间的时间戳
	@SerializedName("end_time")
	private long endTime;

	// 当前页 查询列表当前页
	@SerializedName("pageno")
	private String pageNo;

	// 每页显示记录 每页显示多少条记录
	private String size;

	// 名称空间 名称空间
	private String namespace;

	// 路径 日志路径
	private String paths;

	//域标签列表
	private String clusters;

	//域标签列表
	private String services;

	//节点标签列表
	private String nodes;
	//集群ID
	@SerializedName("region_id")
	private String regionId;

}
