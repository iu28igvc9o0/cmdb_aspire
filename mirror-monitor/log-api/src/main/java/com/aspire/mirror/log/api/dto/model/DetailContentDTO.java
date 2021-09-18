package com.aspire.mirror.log.api.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 事件明细content属性对象
 * 项目名称: 微服务运维平台（log-api 模块）
 * 包: com.migu.tsg.microservice.monitor.log.dto.model
 * 类名称: DetailContentDTO.java
 * 类描述: 事件明细content属性对象
 * 创建人: sunke
 * 创建时间: 2017年8月11日 上午10:29:42
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DetailContentDTO {

	private String key;

	private String type;

	private String value;
}
