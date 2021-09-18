package com.migu.tsg.microservice.atomicservice.composite.clientservice.logWork;

import com.aspire.mirror.alert.api.service.logWork.AlertLogWorkService;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "${mirror.feign.alert-app.value}", path = "${mirror.feign.alert-app.path:}")
public interface AlertWorkLogServiceClient extends AlertLogWorkService {
}
