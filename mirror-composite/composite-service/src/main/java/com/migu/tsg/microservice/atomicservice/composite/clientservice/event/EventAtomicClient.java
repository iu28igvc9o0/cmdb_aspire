package com.migu.tsg.microservice.atomicservice.composite.clientservice.event;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.aspire.mirror.log.api.service.IEventService;

/**
 * 
* 原子层调用接口
* Project Name:composite-service
* File Name:EventAtomicClient.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.clientservice.event
* ClassName: EventAtomicClient <br/>
* date: 2017年10月8日 下午6:42:28 <br/>
* 调用原子层服务实现编排层功能
* @author zhangqing
* @version 
* @since JDK 1.6
 */
@FeignClient(value = "logService", url = "http://10.1.203.100:8150")
public interface EventAtomicClient extends IEventService {

}
