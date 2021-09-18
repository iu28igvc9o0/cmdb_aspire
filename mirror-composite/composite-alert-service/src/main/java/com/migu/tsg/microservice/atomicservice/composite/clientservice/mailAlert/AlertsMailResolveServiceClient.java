package com.migu.tsg.microservice.atomicservice.composite.clientservice.mailAlert;

import com.aspire.mirror.alert.api.service.mailAlert.AlertsMailResolveService;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "${mirror.feign.alert.value}", path = "${mirror.feign.alert.path:}")
public interface AlertsMailResolveServiceClient extends AlertsMailResolveService {
}
