package com.migu.tsg.microservice.atomicservice.composite.clientservice.notify;

import com.aspire.mirror.alert.api.service.notify.SmsService;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @author menglinjie
 */
@FeignClient(value = "${mirror.feign.alert.value}", path = "${mirror.feign.alert.path:}")
public interface SmsServiceClient extends SmsService {
}
