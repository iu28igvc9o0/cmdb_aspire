package com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.migu.tsg.microservice.atomicservice.rbac.service.TokenService;

/**
 * @BelongsProject: msp-composite
 * @BelongsPackage: com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc
 * @Author: baiwenping
 * @CreateTime: 2020-03-18 18:18
 * @Description: ${Description}
 */
@FeignClient(value = "rbac")
public interface TokenServiceClient extends TokenService {
}
