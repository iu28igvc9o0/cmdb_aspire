package com.migu.tsg.microservice.atomicservice.composite.clientservice.alert;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.aspire.mirror.alert.api.service.LeakScanService;

@FeignClient(name = "ALERT-SERVICE", url = "http://10.1.203.100:28130")
public interface SecurityLeakScanServiceClient extends LeakScanService {
}
