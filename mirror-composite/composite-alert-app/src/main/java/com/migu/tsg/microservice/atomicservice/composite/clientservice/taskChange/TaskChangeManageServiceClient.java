package com.migu.tsg.microservice.atomicservice.composite.clientservice.taskChange;

import com.aspire.mirror.alert.api.service.taskChange.TaskChangeManageService;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "${mirror.feign.alert-app.value}", path = "${mirror.feign.alert-app.path:}")
public interface TaskChangeManageServiceClient extends TaskChangeManageService {
}
