package com.migu.tsg.microservice.atomicservice.composite.clientservice.alert;

import com.aspire.mirror.alert.api.service.alert.AlertsService;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * 告警服务客户端
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.clientservice.alert
 * 类名称:    AlertsServiceClient.java
 * 类描述:    告警服务客户端
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 16:08
 * 版本:      v1.0
 */
@FeignClient(value = "${mirror.feign.alert.value}", path = "${mirror.feign.alert.path:}")
public interface AlertsServiceClient extends AlertsService {
}
