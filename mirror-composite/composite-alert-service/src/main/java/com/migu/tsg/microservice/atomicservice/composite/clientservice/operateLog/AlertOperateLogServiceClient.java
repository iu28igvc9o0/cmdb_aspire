package com.migu.tsg.microservice.atomicservice.composite.clientservice.operateLog;

import com.aspire.mirror.alert.api.service.operateLog.AlertOperateLogService;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "${mirror.feign.alert.value}", path = "${mirror.feign.alert.path:}")
public interface AlertOperateLogServiceClient extends AlertOperateLogService {
}
