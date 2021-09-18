package com.migu.tsg.microservice.atomicservice.composite.clientservice.alert;

import com.aspire.mirror.alert.api.service.alert.AlertsV2Service;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @BelongsProject: msp-composite
 * @BelongsPackage: com.migu.tsg.microservice.atomicservice.composite.clientservice.alert.v2.alert
 * @Author: baiwenping
 * @CreateTime: 2020-03-06 16:53
 * @Description: ${Description}
 */
@FeignClient(value = "${mirror.feign.alert.value}", path = "${mirror.feign.alert.path:}")
public interface AlertsV2ServiceClient extends AlertsV2Service {
}
