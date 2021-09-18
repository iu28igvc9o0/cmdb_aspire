package com.migu.tsg.microservice.atomicservice.composite.clientservice.notify;

import com.aspire.mirror.alert.api.service.notify.AlertNotifyConfigService;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "${mirror.feign.alert.value}", path = "${mirror.feign.alert.path:}")
public interface AlertNotifyConfigServiceClient extends AlertNotifyConfigService {
}
