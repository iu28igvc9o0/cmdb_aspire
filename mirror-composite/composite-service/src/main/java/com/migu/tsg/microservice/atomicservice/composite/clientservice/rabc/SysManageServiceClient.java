package com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.migu.tsg.microservice.atomicservice.rbac.service.SysManageService;

/**
 * @ClassName SysManageServiceClient
 * @Description: TODO
 * @Author baiwenping
 * @Date 2019/11/26
 * @Version V1.0
 **/
@FeignClient(value = "rbac")
public interface SysManageServiceClient extends SysManageService {
}
