package com.migu.tsg.microservice.atomicservice.composite.clientservice.inspection;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.migu.tsg.microservice.atomicservice.rbac.service.OrgsService;

/**
 * 用户接口客户端
 * <p>
 * 项目名称: mirror平台 包: com.migu.tsg.microservice.atomicservice.composite.clientservice.inspection 类名称: UserServiceClient.java 类描述:
 * 用户接口客户端 创建人: JinSu 创建时间: 2018/8/18 16:24 版本: v1.0
 */
@FeignClient(value = "rbac")
public interface UserServiceClient extends OrgsService {
}
