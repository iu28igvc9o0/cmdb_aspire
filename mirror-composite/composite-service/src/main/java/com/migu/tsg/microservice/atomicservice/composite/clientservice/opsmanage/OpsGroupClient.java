package com.migu.tsg.microservice.atomicservice.composite.clientservice.opsmanage;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.aspire.mirror.ops.api.service.IOpsGroupService;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.clientservice.opsmanage
 * 类名称:    OpsGroupClient.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/3/9 17:26
 * 版本:      v1.0
 */
@FeignClient(value = "ops-service", url = "http://10.1.203.100:30303")
public interface OpsGroupClient extends IOpsGroupService {
}
