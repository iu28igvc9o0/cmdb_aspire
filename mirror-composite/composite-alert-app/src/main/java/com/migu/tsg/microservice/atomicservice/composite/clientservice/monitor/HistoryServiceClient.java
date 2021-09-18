package com.migu.tsg.microservice.atomicservice.composite.clientservice.monitor;

import com.aspire.mirror.elasticsearch.api.service.zabbix.IZabbixHistoryService;
import org.springframework.cloud.netflix.feign.FeignClient;

import java.util.Map;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.clientservice.es
 * 类名称:    HistoryServiceClient.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/10/24 19:56
 * 版本:      v1.0
 */
@FeignClient("ELASTICSEARCH-SERVICE")
public interface HistoryServiceClient extends IZabbixHistoryService {
}
