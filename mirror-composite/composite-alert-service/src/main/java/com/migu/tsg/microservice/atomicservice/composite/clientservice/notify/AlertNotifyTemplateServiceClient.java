package com.migu.tsg.microservice.atomicservice.composite.clientservice.notify;

import com.aspire.mirror.alert.api.service.notify.AlertNotifyTemplateService;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @BelongsProject: msp-composite
 * @BelongsPackage: com.migu.tsg.microservice.atomicservice.composite.clientservice.alert.v2
 * @Author: baiwenping
 * @CreateTime: 2020-03-06 16:03
 * @Description: ${Description}
 */
@FeignClient(value = "${mirror.feign.alert.value}", path = "${mirror.feign.alert.path:}")
public interface AlertNotifyTemplateServiceClient extends AlertNotifyTemplateService {

}
