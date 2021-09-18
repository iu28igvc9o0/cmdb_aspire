package com.migu.tsg.microservice.atomicservice.composite.clientservice.dashboard;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.aspire.mirror.elasticsearch.api.service.zabbix.IDashboardQueryService;

/**
 * @author baiwp
 * @title: ZabbixItemServiceClient
 * @projectName msp-composite
 * @description: TODO
 * @date 2019/6/2410:44
 */
@FeignClient("ELASTICSEARCH-SERVICE")
public interface DashboardEsServiceClient extends IDashboardQueryService {
}
