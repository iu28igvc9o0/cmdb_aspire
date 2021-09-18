package com.aspire.mirror.alert.server.clientservice.elasticsearch;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.aspire.mirror.elasticsearch.api.service.indexPage.IIndexPageService;

/**
 * @author baiwp
 * @title: ZabbixTriggerServiceClient
 * @projectName msp-composite
 * @description: TODO
 * @date 2019/6/2411:17
 */
@FeignClient("ELASTICSEARCH-SERVICE")
public interface EsIndexPageServiceClient extends IIndexPageService {
}
