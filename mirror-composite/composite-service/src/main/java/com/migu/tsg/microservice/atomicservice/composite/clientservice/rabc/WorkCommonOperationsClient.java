package com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.migu.tsg.microservice.atomicservice.rbac.service.WorkCommonOperationsService;

@FeignClient(value = "rbac")
public interface WorkCommonOperationsClient extends WorkCommonOperationsService {

}
