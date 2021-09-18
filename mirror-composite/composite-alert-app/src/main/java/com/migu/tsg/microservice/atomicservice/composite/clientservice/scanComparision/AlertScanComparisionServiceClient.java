package com.migu.tsg.microservice.atomicservice.composite.clientservice.scanComparision;

import com.aspire.mirror.alert.api.service.scanComparision.AlertScanComparisionService;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "${mirror.feign.alert-app.value}", path = "${mirror.feign.alert-app.path:}")
public interface AlertScanComparisionServiceClient extends AlertScanComparisionService {
}
