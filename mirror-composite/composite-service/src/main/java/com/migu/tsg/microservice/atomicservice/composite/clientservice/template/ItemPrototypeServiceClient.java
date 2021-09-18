package com.migu.tsg.microservice.atomicservice.composite.clientservice.template;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.aspire.mirror.template.api.service.IMonitorItemPrototypeService;

/**
 * 监控项客户端
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.clientservice.template
 * 类名称:    ItemsServiceClient.java
 * 类描述:    监控项客户端
 * 创建人:    JinSu
 * 创建时间:  2018/8/7 19:32
 * 版本:      v1.0
 */
@FeignClient(value = "${mirror.feign.template.value}", path = "${mirror.feign.template.path:}")
//@FeignClient(value="TEMPLATE-SERVICE-PGH22", url="http://127.0.0.1:8127")
public interface ItemPrototypeServiceClient extends IMonitorItemPrototypeService {
	
}
