package com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.migu.tsg.microservice.atomicservice.rbac.service.ResourceSchemaService;


/**
* ResourceSchemaService调用client
* Project Name:composite-service
* File Name:ResourceSchemaServiceClient.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc
* ClassName: ResourceSchemaServiceClient <br/>
* date: 2017年9月19日 下午8:42:06 <br/>
* ResourceSchemaService调用client
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@FeignClient(value = "rbac")
public interface ResourceSchemaServiceClient extends ResourceSchemaService {

}
