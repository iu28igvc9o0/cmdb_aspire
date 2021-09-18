package com.migu.tsg.microservice.atomicservice.composite.service.event.dto;

import com.google.gson.annotations.SerializedName;

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
public class BusinessLogDTO {

	private long time;

	@SerializedName("biz_date")
	private String createDate;

	//自定义检索值
	@SerializedName("biz_id")
	private String traceId;

	@SerializedName("biz_level")
	private String logLevel;

	@SerializedName("biz_code")
	private String logCode;

	@SerializedName("biz_content")
	private String logContent;

	@SerializedName("biz_file")
	private String path;
}
