package com.aspire.ums.cmdb.client;

import com.migu.tsg.microservice.atomicservice.rbac.service.DepartmentService;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * 说明: 创建Rbac的FeginClient
 * 工程: BPM
 * 作者: zhujuwang
 * 时间: 2021/1/6 15:01
 */
@FeignClient(value = "rbac")
public interface IRbacDepartmentClient extends DepartmentService {
}
