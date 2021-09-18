package com.migu.tsg.microservice.atomicservice.composite.clientservice.monthReport;

import com.aspire.mirror.alert.api.service.monthReport.AlertsMonReportService;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "${mirror.feign.alert-app.value}", path = "${mirror.feign.alert-app.path:}")
public interface AlertMonReportServiceClient extends AlertsMonReportService {
}
