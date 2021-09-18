package com.migu.tsg.microservice.atomicservice.composite.clientservice.third;

import com.aspire.mirror.alert.api.service.third.AlertTodayWaringMessageService;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "${mirror.feign.alert-app.value}", path = "${mirror.feign.alert-app.path:}")
public interface AlertTodayWaringMessageServiceClient extends AlertTodayWaringMessageService {
}
