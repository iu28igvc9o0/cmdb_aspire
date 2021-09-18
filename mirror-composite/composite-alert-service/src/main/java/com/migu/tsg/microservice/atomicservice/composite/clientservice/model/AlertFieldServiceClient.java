package com.migu.tsg.microservice.atomicservice.composite.clientservice.model;

import com.aspire.mirror.alert.api.service.model.AlertsFieldService;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "${mirror.feign.alert.value}", path = "${mirror.feign.alert.path:}")
public interface AlertFieldServiceClient extends AlertsFieldService {
}
