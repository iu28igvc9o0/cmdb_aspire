package com.migu.tsg.microservice.atomicservice.composite.clientservice.log;

import com.aspire.mirror.log.api.service.IConfigCompareService;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @author baiwenping
 * @Title: IConfigCompareServiceClient
 * @Package com.migu.tsg.microservice.atomicservice.composite.clientservice.log
 * @Description: TODO
 * @date 2020/12/23 16:26
 */
@FeignClient("logService")
public interface IConfigCompareServiceClient extends IConfigCompareService {
}
