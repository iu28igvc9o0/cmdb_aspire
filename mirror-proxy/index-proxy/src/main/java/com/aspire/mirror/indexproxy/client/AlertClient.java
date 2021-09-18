package com.aspire.mirror.indexproxy.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "ALERT-SERVICE")
public interface AlertClient {
    @PostMapping(value = "/v1/alerts/itemMonitorEvent/callback")
    void onItemMonitorEventCallBack(@RequestBody Object monitorEvent);
}
