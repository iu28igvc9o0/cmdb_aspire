package com.aspire.mirror.alert.server.clientservice;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.aspire.mirror.elasticsearch.api.service.zabbix.IZabbixTrendsService;

/**
 * @author baiwp
 * @title: ZabbixTrendsServiceClient
 * @projectName msp-composite
 * @description: TODO
 * @date 2019/6/2411:17
 */
@FeignClient(value = "ELASTICSEARCH-SERVICE", url = "http://10.1.203.100:28103")
public interface ZabbixTrendsServiceClient extends IZabbixTrendsService {
}
