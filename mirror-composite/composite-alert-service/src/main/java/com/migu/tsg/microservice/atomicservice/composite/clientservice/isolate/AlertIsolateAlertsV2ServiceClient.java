package com.migu.tsg.microservice.atomicservice.composite.clientservice.isolate;

import com.aspire.mirror.alert.api.service.isolate.AlertIsolateAlertsV2Service;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @BelongsProject: msp-composite
 * @BelongsPackage: com.migu.tsg.microservice.atomicservice.composite.clientservice.alert.v2
 * @Author: baiwenping
 * @CreateTime: 2020-03-03 15:22
 * @Description: ${Description}
 */
@FeignClient(value = "${mirror.feign.alert.value}", path = "${mirror.feign.alert.path:}")
public interface AlertIsolateAlertsV2ServiceClient extends AlertIsolateAlertsV2Service {
}
