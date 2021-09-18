package com.migu.tsg.microservice.atomicservice.composite.clientservice.cabinetAlert;

import com.aspire.mirror.alert.api.service.cabinetAlert.AlertCabinetColumnService;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "${mirror.feign.alert-app.value}", path = "${mirror.feign.alert-app.path:}")
public interface AlertCabinetColumnClient extends AlertCabinetColumnService {
}
