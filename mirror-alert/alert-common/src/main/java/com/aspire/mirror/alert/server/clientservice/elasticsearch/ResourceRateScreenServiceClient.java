package com.aspire.mirror.alert.server.clientservice.elasticsearch;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.aspire.mirror.elasticsearch.api.service.zabbix.IResourceRateScreenService;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.server.clientservice.elasticsearch
 * @Author: baiwenping
 * @CreateTime: 2020-04-22 19:21
 * @Description: ${Description}
 */
@FeignClient("ELASTICSEARCH-SERVICE")
public interface ResourceRateScreenServiceClient extends IResourceRateScreenService {
}
