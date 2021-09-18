package com.migu.tsg.microservice.atomicservice.composite.clientservice.theme;

import com.aspire.mirror.theme.api.service.BizThemeService;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * 业务主题客户端
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.clientservice.theme
 * 类名称:    BizThemeServiceClient.java
 * 类描述:    业务主题客户端
 * 创建人:    JinSu
 * 创建时间:  2018/10/24 19:44
 * 版本:      v1.0
 */
@FeignClient(value = "${mirror.feign.theme.value}", path = "${mirror.feign.theme.path:}")
public interface BizThemeServiceClient extends BizThemeService {
}
