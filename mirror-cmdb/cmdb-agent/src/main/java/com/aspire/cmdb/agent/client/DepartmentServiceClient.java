package com.aspire.cmdb.agent.client;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.migu.tsg.microservice.atomicservice.rbac.service.DepartmentService;

/**
 * <p>
 * 部门Feign接口
 * </p>
 * 
 * @title DepartmentServiceClient.java
 * @package com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc
 * @author 曾祥华
 * @version 0.1 2019年3月5日
 */
@FeignClient(value = "rbac", url = "http://10.1.203.100:28101")
public interface DepartmentServiceClient extends DepartmentService {

}
