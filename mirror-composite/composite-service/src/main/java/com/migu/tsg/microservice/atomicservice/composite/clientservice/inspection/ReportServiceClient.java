package com.migu.tsg.microservice.atomicservice.composite.clientservice.inspection;

import com.aspire.mirror.inspection.api.service.ReportService;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * 报告客户端
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.clientservice.inspection
 * 类名称:    ReportServiceClient.java
 * 类描述:    报告客户端
 * 创建人:    JinSu
 * 创建时间:  2018/8/12 11:07
 * 版本:      v1.0
 */
@FeignClient(value = "INSPECTION-SERVICE")
public interface ReportServiceClient extends ReportService {
}
