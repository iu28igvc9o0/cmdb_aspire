package com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.migu.tsg.microservice.atomicservice.rbac.service.ModuleInfoService;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc
 * 类名称:    ModuleInfoClient.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/10/17 16:00
 * 版本:      v1.0
 */
@FeignClient(value = "rbac")
public interface ModuleInfoClient extends ModuleInfoService {
}
