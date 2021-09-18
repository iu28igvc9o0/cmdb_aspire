package com.aspire.ums.cmdb.sync.client;

import com.aspire.mirror.common.configuration.FeignHttpClientConfiguration;
import com.migu.tsg.microservice.atomicservice.rbac.service.DepartmentService;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "rbac", configuration = FeignHttpClientConfiguration.class)
public interface DepartmentServiceClient extends DepartmentService {
}
