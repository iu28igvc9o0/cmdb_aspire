package com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.migu.tsg.microservice.atomicservice.rbac.service.UserClassifyService;

/**
 * @author menglinjie
 */
@FeignClient(value = "rbac")
public interface UserClassifyServiceClient extends UserClassifyService {
}
