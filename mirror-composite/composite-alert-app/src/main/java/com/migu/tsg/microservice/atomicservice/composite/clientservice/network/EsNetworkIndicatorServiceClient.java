package com.migu.tsg.microservice.atomicservice.composite.clientservice.network;

import com.aspire.mirror.elasticsearch.api.service.network.IEsNetworkIndicatorService;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @author hewang
 * @version 1.0
 * @date 2021/3/12 18:01
 */
@FeignClient("elasticsearch-service")
public interface EsNetworkIndicatorServiceClient extends IEsNetworkIndicatorService {
}
