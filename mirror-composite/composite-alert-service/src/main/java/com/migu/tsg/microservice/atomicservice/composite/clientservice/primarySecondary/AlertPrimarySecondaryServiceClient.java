package com.migu.tsg.microservice.atomicservice.composite.clientservice.primarySecondary;

import com.aspire.mirror.alert.api.service.primarySecondary.AlertPrimarySecondaryService;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "${mirror.feign.alert.value}", path = "${mirror.feign.alert.path:}")
public interface AlertPrimarySecondaryServiceClient extends AlertPrimarySecondaryService {
}
