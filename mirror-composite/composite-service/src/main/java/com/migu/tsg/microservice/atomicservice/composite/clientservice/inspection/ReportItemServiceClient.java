package com.migu.tsg.microservice.atomicservice.composite.clientservice.inspection;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.inspection.api.dto.ReportItemDetailResponse;
import com.aspire.mirror.inspection.api.dto.ReportItemPageRequest;
import com.aspire.mirror.inspection.api.service.ReportItemService;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * 报告元素客户端
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.clientservice.inspection
 * 类名称:    ReportItemServiceClient.java
 * 类描述:    报告元素客户端
 * 创建人:    JinSu
 * 创建时间:  2018/8/13 17:21
 * 版本:      v1.0
 */
@FeignClient(value = "INSPECTION-SERVICE")
public interface ReportItemServiceClient extends ReportItemService {
}
