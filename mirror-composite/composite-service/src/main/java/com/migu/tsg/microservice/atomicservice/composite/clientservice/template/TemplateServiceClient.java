package com.migu.tsg.microservice.atomicservice.composite.clientservice.template;

import com.aspire.mirror.template.api.service.TemplateService;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * 模板客户端
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.clientservice.template
 * 类名称:    TemplateServiceClient.java
 * 类描述:    模板客户端
 * 创建人:    JinSu
 * 创建时间:  2018/8/3 16:30
 * 版本:      v1.0
 */
@FeignClient(value = "${mirror.feign.template.value}", path = "${mirror.feign.template.path:}")
public interface TemplateServiceClient extends TemplateService {

}
