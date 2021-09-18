package com.aspire.mirror.alert.server.clientservice.elasticsearch;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.aspire.mirror.elasticsearch.api.service.monitor.IMonitorKpiService;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.server.clientservice.elasticsearch
 * @Author: baiwenping
 * @CreateTime: 2020-04-22 19:21
 * @Description: ${Description}
 */
@FeignClient(value = "ELASTICSEARCH-SERVICE", url = "http://10.1.203.100:28103")
public interface MonitorKpiServiceClient extends IMonitorKpiService {
}
