package com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.migu.tsg.microservice.atomicservice.rbac.service.DepartmentService;



/**   
 * <p>
 * 部门Feign接口
 * </p>
 * @title DepartmentServiceClient.java
 * @package com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc 
 * @author 曾祥华
 * @version 0.1 2019年3月5日
 */
@FeignClient(value = "rbac")
public interface DepartmentServiceClient extends  DepartmentService {

}
