package com.aspire.mirror.log.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * 业务列表查询请求对象
 * 项目名称: 微服务运维平台（log-api 模块）
 * 包: com.migu.tsg.microservice.monitor.log.dto
 * 类名称: BusinessLogRequest.java
 * 类描述: 业务列表查询请求对象
 * 创建人: sunke
 * 创建时间: 2017年8月10日 上午10:13:31
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class BusinessLogRequest {

	@JsonProperty("start_time")
	private long startTime;

	@JsonProperty("end_time")
	private long endTime;

	private String pageno;

	private String size;

	//自定义检索值
	@JsonProperty("biz_id")
	private String bizId;
	// 日志级别
	@JsonProperty("biz_level")
	private String logLevel;
	//	日志代码
	@JsonProperty("biz_code")
	private String logCode;
	//日志内容
	@JsonProperty("biz_content")
	private String logContent;

	@JsonProperty("filename")
	private String path;

	@JsonProperty("app_id")
	private String appId;
	// 查询语句
	@JsonProperty("query_string")
	@SerializedName("query_string")
	private String queryString;
	@JsonProperty("region_id")
    @SerializedName("region_id")
    private String regionId;

}
