package com.migu.tsg.microservice.atomicservice.composite.clientservice.kgEnv;

import com.aspire.mirror.alert.api.service.kgEnv.KGMonitorIndexService;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "${mirror.feign.alert-app.value}", path = "${mirror.feign.alert-app.path:}")
public interface KGMonitorIndexServiceClient extends KGMonitorIndexService {
}
