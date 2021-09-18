package com.migu.tsg.microservice.atomicservice.composite.clientservice.inspectionDaily;

import com.aspire.mirror.alert.api.service.inspectionDaily.AlertResourcePoolService;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "${mirror.feign.alert-app.value}", path = "${mirror.feign.alert-app.path:}")
public interface AlertResourcePoolClient extends AlertResourcePoolService {
}
