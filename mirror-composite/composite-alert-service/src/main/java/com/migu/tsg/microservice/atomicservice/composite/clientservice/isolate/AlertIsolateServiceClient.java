package com.migu.tsg.microservice.atomicservice.composite.clientservice.isolate;

import com.aspire.mirror.alert.api.service.isolate.AlertIsolateService;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "${mirror.feign.alert.value}", path = "${mirror.feign.alert.path:}")
public interface AlertIsolateServiceClient extends AlertIsolateService {
}
