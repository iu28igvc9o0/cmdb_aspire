package com.migu.tsg.microservice.atomicservice.composite.clientservice.log;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.aspire.mirror.log.api.service.IConfigDataService;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.clientservice.log
 * 类名称:    IConfigDataServiceClient.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/6/19 21:26
 * 版本:      v1.0
 */
@FeignClient(value = "logService",url = "http://10.1.203.100:8150")
public interface IConfigDataServiceClient extends IConfigDataService {
}
