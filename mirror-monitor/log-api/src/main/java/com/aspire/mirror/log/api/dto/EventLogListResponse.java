package com.aspire.mirror.log.api.dto;

import com.aspire.mirror.log.api.dto.model.EventLogListDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 事件日志分页查询响应实体
 * 项目名称: 微服务运维平台（log-api 模块）
 * 包: com.migu.tsg.microservice.monitor.log.dto
 * 类名称: EventLogListResponse.java
 * 类描述: 事件日志分页查询响应实体
 * 创建人: sunke
 * 创建时间: 2017年7月28日 上午11:10:47
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class EventLogListResponse {

    private EventLogListDTO events;

}
