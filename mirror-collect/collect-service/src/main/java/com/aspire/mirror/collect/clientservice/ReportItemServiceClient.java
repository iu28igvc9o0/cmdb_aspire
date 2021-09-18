package com.aspire.mirror.collect.clientservice;

import com.aspire.mirror.inspection.api.dto.ReportItemBatchCreateRequest;
import com.aspire.mirror.inspection.api.dto.ReportItemCreateRequest;
import com.aspire.mirror.inspection.api.dto.ReportItemUpdateRequest;
import com.aspire.mirror.inspection.api.dto.vo.ReportItemVO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.collect.clientservice
 * 类名称:    ReportItemServiceClient.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/11/18 16:21
 * 版本:      v1.0
 */
@FeignClient("inspection-service")
public interface ReportItemServiceClient {
    /**
     * 修改报告值
     */
    @PostMapping(value = "/v1/report_item/update")
    int updateReportItem(@RequestBody ReportItemUpdateRequest reportItemVO);

    /**
     * 创建报告元素
     */
    @PostMapping(value = "/v1/report_item/create")
    int createReportItem(@RequestBody ReportItemCreateRequest reportItemVO);

    @PostMapping(value = "/v1/report_item/batchCreateReportItem")
    Integer batchCreateReportItem(@RequestBody ReportItemBatchCreateRequest reportItemCreateBatchRequest);
}
