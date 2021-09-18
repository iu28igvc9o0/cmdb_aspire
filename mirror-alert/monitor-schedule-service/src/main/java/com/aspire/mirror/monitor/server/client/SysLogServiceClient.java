package com.aspire.mirror.monitor.server.client;

import com.aspire.mirror.log.api.service.ISysLogService;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "logService")
public interface SysLogServiceClient extends ISysLogService {
}
