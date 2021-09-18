package com.migu.tsg.microservice.atomicservice.composite.clientservice.network;


import com.aspire.mirror.alert.api.service.network.AlertsNetworkService;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @author hewang
 * @date 2021-03-03
 * @version 1.0
 */
@FeignClient(value = "${mirror.feign.alert-app.value}", path = "${mirror.feign.alert-app.path:}")
public interface AlertNetworkServiceClient extends AlertsNetworkService {

}
