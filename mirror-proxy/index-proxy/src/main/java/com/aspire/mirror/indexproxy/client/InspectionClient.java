package com.aspire.mirror.indexproxy.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "INSPECTION-SERVICE")
public interface InspectionClient {
    @PostMapping(value = "/v1/report_data/itemdata_callback")
    void onReportItemDataCallBack(@RequestBody Object bizObj);
}
