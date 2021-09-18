package com.migu.tsg.microservice.atomicservice.composite.service.logs.payload;
import java.util.List;

import com.migu.tsg.microservice.atomicservice.composite.service.logs.dto.BucketInfoDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 日志图表查询响应
 * 项目名称: 微服务运维平台（log-api 模块）
 * 包: com.migu.tsg.microservice.monitor.log.dto
 * 类名称: LogChartListResponse.java
 * 类描述: 日志图表查询响应
 * 创建人: sunke
 * 创建时间: 2017年7月29日 下午5:47:58
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LogChartListResponse {
	
    //返回日志按时间维度的统计
	private List<BucketInfoDTO> buckets;
}
