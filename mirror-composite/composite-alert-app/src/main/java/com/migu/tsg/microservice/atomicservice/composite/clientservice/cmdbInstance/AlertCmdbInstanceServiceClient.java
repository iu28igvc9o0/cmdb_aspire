package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdbInstance;

import com.aspire.mirror.alert.api.service.cmdbInstance.IAlertCmdbInstanceService;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "${mirror.feign.alert-app.value}", path = "${mirror.feign.alert-app.path:}")
public interface AlertCmdbInstanceServiceClient extends IAlertCmdbInstanceService {
}
