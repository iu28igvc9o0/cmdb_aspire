package com.aspire.mirror.log.api.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 日志时间分布图数据结构
 * 项目名称: 微服务运维平台（log-api 模块）
 * 包: com.migu.tsg.microservice.monitor.log.dto.model
 * 类名称: BucketInfo.java
 * 类描述: 日志时间分布图数据结构
 * 创建人: sunke
 * 创建时间: 2017年7月28日 上午9:29:41
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class BucketInfoDTO {

	//时间点的日志数量
	private Long count;

	//统计时间点
	private String time;

}
