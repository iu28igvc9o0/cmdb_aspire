package com.migu.tsg.microservice.atomicservice.composite.clientservice.alert;

import com.aspire.mirror.alert.api.service.alert.AlertStatisticService;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "${mirror.feign.alert.value}", path = "${mirror.feign.alert.path:}")
public interface AlertsStatisticServiceClient extends AlertStatisticService {
}
