package com.migu.tsg.microservice.atomicservice.composite.clientservice.bpm;

import com.aspire.mirror.alert.api.service.bpm.AlertBpmService;
import org.springframework.cloud.netflix.feign.FeignClient;


@FeignClient(value = "${mirror.feign.alert.value}", path = "${mirror.feign.alert.path:}")
public interface AlertBpmServiceClient extends AlertBpmService {

}
