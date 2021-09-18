package com.migu.tsg.microservice.atomicservice.composite.clientservice.template;

import com.aspire.mirror.template.api.service.ApiServerConfigService;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.clientservice.template
 * 类名称:    ApiServerConfigServiceCient.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2018/9/6 13:59
 * 版本:      v1.0
 */
@FeignClient(value = "${mirror.feign.template.value}", path = "${mirror.feign.template.path:}")
public interface ApiServerConfigServiceCient extends ApiServerConfigService {
}
