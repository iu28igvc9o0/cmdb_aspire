package com.migu.tsg.microservice.atomicservice.composite.clientservice.derive;

import com.aspire.mirror.alert.api.service.derive.AlertDeriveService;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "${mirror.feign.alert.value}", path = "${mirror.feign.alert.path:}")
public interface AlertDeriveServiceClient extends AlertDeriveService {
}
