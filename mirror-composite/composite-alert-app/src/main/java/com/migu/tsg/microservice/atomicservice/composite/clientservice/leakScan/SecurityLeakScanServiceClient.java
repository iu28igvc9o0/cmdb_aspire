package com.migu.tsg.microservice.atomicservice.composite.clientservice.leakScan;

import com.aspire.mirror.alert.api.service.leakScan.LeakScanService;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "${mirror.feign.alert-app.value}", path = "${mirror.feign.alert-app.path:}")
public interface SecurityLeakScanServiceClient extends LeakScanService {
}
