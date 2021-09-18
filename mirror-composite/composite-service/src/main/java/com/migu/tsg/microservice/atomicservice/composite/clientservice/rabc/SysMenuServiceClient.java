package com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.migu.tsg.microservice.atomicservice.rbac.service.SysMenuService;

/**
 * @ClassName SysMenuServiceClient
 * @Description: TODO
 * @Author baiwenping
 * @Date 2019/11/26
 * @Version V1.0
 **/
@FeignClient(value = "rbac")
public interface SysMenuServiceClient extends SysMenuService {
}
