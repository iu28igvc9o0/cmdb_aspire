package com.migu.tsg.microservice.atomicservice.composite.clientservice.log;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.aspire.mirror.log.api.service.ILogService;


@FeignClient(value = "logService",url = "http://10.1.203.100:8150")
public interface LogViewServiceClient extends ILogService {
	
}
