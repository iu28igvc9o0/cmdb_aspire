package com.migu.tsg.microservice.atomicservice.composite.clientservice.filter;

import com.aspire.mirror.alert.api.service.filter.AlertFilterService;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * 告警历史客户端
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.clientservice.alert
 * 类名称:    AlertsHisServiceClient.java
 * 类描述:    告警历史客户端
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 16:59
 * 版本:      v1.0
 */
@FeignClient(value = "${mirror.feign.alert.value}", path = "${mirror.feign.alert.path:}")
public interface AlertsFilterServiceClient extends AlertFilterService {
}
