package com.migu.tsg.microservice.atomicservice.composite.clientservice.es;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.aspire.mirror.elasticsearch.api.service.zabbix.IDashboardQueryService;

/**
 * @author baiwp
 * @title: ZabbixItemServiceClient
 * @projectName msp-composite
 * @description: TODO
 * @date 2019/6/2410:44
 */
@FeignClient(name = "ELASTICSEARCH-SERVICE", url = "http://10.1.203.100:28103")
public interface DashboardEsServiceClient extends IDashboardQueryService {
}
