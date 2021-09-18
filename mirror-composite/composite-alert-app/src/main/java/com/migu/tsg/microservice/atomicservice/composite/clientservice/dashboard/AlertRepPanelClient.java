package com.migu.tsg.microservice.atomicservice.composite.clientservice.dashboard;

import com.aspire.mirror.alert.api.service.dashboard.AlertRepPanelService;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "${mirror.feign.alert-app.value}", path = "${mirror.feign.alert-app.path:}")
public interface AlertRepPanelClient extends AlertRepPanelService {
}
