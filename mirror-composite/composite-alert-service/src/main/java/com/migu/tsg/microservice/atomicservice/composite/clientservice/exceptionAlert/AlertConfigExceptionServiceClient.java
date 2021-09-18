package com.migu.tsg.microservice.atomicservice.composite.clientservice.exceptionAlert;

import com.aspire.mirror.alert.api.service.exceptionAlert.AlertConfigExceptionService;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @BelongsProject: msp-composite
 * @BelongsPackage: com.migu.tsg.microservice.atomicservice.composite.clientservice.alert.v2.strategy
 * @Author: baiwenping
 * @CreateTime: 2020-06-22 20:26
 * @Description: ${Description}
 */
@FeignClient(value = "${mirror.feign.alert.value}", path = "${mirror.feign.alert.path:}")
public interface AlertConfigExceptionServiceClient extends AlertConfigExceptionService {
}
