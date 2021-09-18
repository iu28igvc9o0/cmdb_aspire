package com.migu.tsg.microservice.atomicservice.composite.clientservice.template;

import com.aspire.mirror.template.api.service.TemplateObjectService;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * 模板关联对象客户端
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.clientservice.template
 * 类名称:    TemplateObjectServiceClient.java
 * 类描述:    模板关联对象客户端
 * 创建人:    JinSu
 * 创建时间:  2018/9/10 9:27
 * 版本:      v1.0
 */
@FeignClient(value = "${mirror.feign.template.value}", path = "${mirror.feign.template.path:}")
public interface TemplateObjectServiceClient extends TemplateObjectService {
}
