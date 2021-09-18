package com.migu.tsg.microservice.atomicservice.composite.clientservice.alert;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.aspire.mirror.elasticsearch.api.service.zabbix.IStpInfoService;

/**
 * @author longfeng
 * @title: IStpInfoServiceClient
 * @projectName msp-composite
 * @description: TODO
 * @date 2019/7/2910:07
 */
@FeignClient(value = "ELASTICSEARCH-SERVICE", url = "http://10.1.203.100:28103")
public interface IStpInfoServiceClient extends IStpInfoService {
}
