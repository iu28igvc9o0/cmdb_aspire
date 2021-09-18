package com.aspire.mirror.alert.server.clientservice;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.aspire.mirror.elasticsearch.api.service.zabbix.IMonthReportService;

/**
 * @author baiwp
 * @title: ZabbixItemServiceClient
 * @projectName msp-composite
 * @description: TODO
 * @date 2019/6/2410:44
 */
@FeignClient(value = "ELASTICSEARCH-SERVICE", url = "http://10.1.203.100:28103")
public interface MonthReportServiceClient extends IMonthReportService{
}
