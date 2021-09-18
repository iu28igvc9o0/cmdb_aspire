package com.migu.tsg.microservice.atomicservice.composite.clientservice.third;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.aspire.mirror.alert.api.service.third.AlertMonitorService;

@FeignClient(value = "${mirror.feign.alert-app.value}", path = "${mirror.feign.alert-app.path:}")
public interface AlertThirdMonitorServiceClient extends AlertMonitorService {
}
