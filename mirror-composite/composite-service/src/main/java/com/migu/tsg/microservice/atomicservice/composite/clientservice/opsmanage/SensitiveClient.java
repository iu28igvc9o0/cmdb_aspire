package com.migu.tsg.microservice.atomicservice.composite.clientservice.opsmanage;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.aspire.mirror.ops.api.service.ISensitiveService;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.clientservice.opsmanage
 * 类名称:    SensitiveClient.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/2/11 13:55
 * 版本:      v1.0
 */
@FeignClient(value = "ops-service", url = "http://10.1.203.100:30303")
public interface SensitiveClient extends ISensitiveService {
}
