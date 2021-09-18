package com.migu.tsg.microservice.atomicservice.composite.clientservice.log;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.aspire.mirror.log.api.service.IEventService;
//import org.springframework.stereotype.Component;


/**
* LogClientService接口继承IEventService接口
* Project Name:composite-service
* File Name:LogClientService.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.clientService.log
* ClassName: LogClientService <br/>
* date: 2017年9月1日 下午1:42:06 <br/>
* 调用rbac接口的接口
* @author pengguihua
* @version
* @since JDK 1.6
*/
@FeignClient(value = "logService",url = "http://10.1.203.100:8150")
public interface LogClientService extends IEventService {

}
