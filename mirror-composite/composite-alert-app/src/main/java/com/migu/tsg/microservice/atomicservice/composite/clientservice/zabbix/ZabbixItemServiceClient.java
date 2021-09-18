package com.migu.tsg.microservice.atomicservice.composite.clientservice.zabbix;

import com.aspire.mirror.elasticsearch.api.service.zabbix.IZabbixItemService;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @author baiwp
 * @title: ZabbixItemServiceClient
 * @projectName msp-composite
 * @description: TODO
 * @date 2019/6/2410:44
 */
@FeignClient("ELASTICSEARCH-SERVICE")
public interface ZabbixItemServiceClient extends IZabbixItemService {
}
