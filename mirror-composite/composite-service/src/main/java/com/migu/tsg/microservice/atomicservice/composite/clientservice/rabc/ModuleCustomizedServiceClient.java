package com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc;

import com.migu.tsg.microservice.atomicservice.rbac.service.ModuleCustomizedService;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value="rbac")
public interface ModuleCustomizedServiceClient extends ModuleCustomizedService {

}
