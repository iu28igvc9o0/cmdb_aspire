package com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.migu.tsg.microservice.atomicservice.rbac.service.OrgsService;


/**
* 调用rbac中成员相关操作接口
* Project Name:composite-service
* File Name:OrgsSubAccountClient.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc
* ClassName: OrgsSubAccountClient <br/>
* date: 2017年9月9日 上午10:38:50 <br/>
* 继承了rbac接口中的OrgsService接口，实现成员相关增删改查
* @author yangshilei
* @version 
* @since JDK 1.6
*/
    
@FeignClient(value = "rbac")
public interface OrgsSubAccountClient extends OrgsService{

}
