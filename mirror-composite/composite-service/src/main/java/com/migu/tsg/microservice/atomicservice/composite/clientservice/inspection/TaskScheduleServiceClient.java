package com.migu.tsg.microservice.atomicservice.composite.clientservice.inspection;

import com.aspire.mirror.inspection.api.service.TaskClientService;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * 任务调度客户端
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.clientservice.inspection
 * 类名称:    TaskScheduleServiceClient.java
 * 类描述:    任务调度客户端
 * 创建人:    JinSu
 * 创建时间:  2018/8/21 15:04
 * 版本:      v1.0
 */
@FeignClient(value = "INSPECTION-SERVICE")
public interface TaskScheduleServiceClient extends TaskClientService {
}
