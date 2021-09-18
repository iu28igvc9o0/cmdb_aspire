package com.migu.tsg.microservice.atomicservice.composite.clientservice.faultReport;

import com.aspire.mirror.alert.api.service.faultReport.FaultReportManageService;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "${mirror.feign.alert-app.value}", path = "${mirror.feign.alert-app.path:}")
public interface FaultReportManageClient extends FaultReportManageService {
}
