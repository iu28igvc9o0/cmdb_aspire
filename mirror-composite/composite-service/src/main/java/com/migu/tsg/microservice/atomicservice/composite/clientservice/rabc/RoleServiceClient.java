package com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.migu.tsg.microservice.atomicservice.rbac.service.RoleService;


/**
*  HmRoleService接口
* Project Name:composite-service
* File Name:RoleServiceClient.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc
* ClassName: RoleServiceClient <br/>
* date: 2017年9月1日 下午5:30:48 <br/>
*  详细描述这个类的功能等
* @author yangshilei
* @version 
* @since JDK 1.6
*/
// @FeignClient(value = "rbac")
@FeignClient(value = "rbac")
public interface RoleServiceClient extends  RoleService {

}
