package com.migu.tsg.microservice.atomicservice.composite.clientservice.zabbix;

import com.aspire.mirror.elasticsearch.api.service.zabbix.IZabbixTriggerService;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @author baiwp
 * @title: ZabbixTriggerServiceClient
 * @projectName msp-composite
 * @description: TODO
 * @date 2019/6/2411:17
 */
@FeignClient("ELASTICSEARCH-SERVICE")
public interface ZabbixTriggerServiceClient extends IZabbixTriggerService {
}
