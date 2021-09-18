package com.migu.tsg.microservice.atomicservice.composite.clientservice.monitorHttp;

import com.aspire.mirror.alert.api.service.monitorHttp.MonitorHttpManageService;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "${mirror.feign.alert-app.value}", path = "${mirror.feign.alert-app.path:}")
public interface MonitorHttpManageClient extends MonitorHttpManageService {
}
