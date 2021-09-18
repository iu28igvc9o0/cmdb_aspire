package com.aspire.mirror.inspection.api.service ;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.inspection.api.dto.ReportItemBatchCreateRequest;
import com.aspire.mirror.inspection.api.dto.ReportItemCreateRequest;
import com.aspire.mirror.inspection.api.dto.ReportItemCreateResponse;
import com.aspire.mirror.inspection.api.dto.ReportItemDetailResponse;
import com.aspire.mirror.inspection.api.dto.ReportItemPageRequest;
import com.aspire.mirror.inspection.api.dto.ReportItemUpdateRequest;
import com.aspire.mirror.inspection.api.dto.vo.ReportItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/*
 * inspection_report_item对外暴露接口
 * 项目名称:   微服务运维平台
 * 包:       com.aspire.mirror.inspection.api.service
 * 类名称:    ReportItemService.java
 * 类描述:    inspection_report_item对外暴露接口层
 * 创建人:    ZhangSheng
 * 创建时间:  2018-07-27 13:48:08
 */
@Api(value = "inspection_report_item")
public interface ReportItemService{
	
    /**
     * 根据reportId查询ReportItem集合信息
     * @param reportId: 巡检报告查询ids
     * @return ReportItemDetailResponse: ReportItem查询响应对象
     */
    @GetMapping(value = "/v1/report_item/get/{report_id}")
    @ApiOperation(value = "查询", notes = "根据reportId获取ReportItem详情", tags = {"/v1/report_item"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = ReportItemDetailResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    PageResponse<ReportItemDetailResponse> listByReportId(@PathVariable("report_id") String reportId);

    /**
     * 修改
     */
    @PostMapping(value = "/v1/report_item/update")
    @ApiOperation(value = "修改报告Item", notes = "修改报告Item", tags = {"/v1/report_item"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    int updateReportItem(@RequestBody ReportItemUpdateRequest reportItemVO);

    /**
     * 创建
     */
    @PostMapping(value = "/v1/report_item/create")
    @ApiOperation(value = "创建报告Item", notes = "创建报告Item", tags = {"/v1/report_item"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    ReportItemCreateResponse createReportItem(@RequestBody ReportItemCreateRequest reportItemVO);

    @PostMapping(value = "/v1/report_item/batchCreateReportItem")
    @ApiOperation(value = "批量创建报告Item", notes = "批量创建报告Item", tags = {"/v1/report_item"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    Integer batchCreateReportItem(@RequestBody ReportItemBatchCreateRequest reportItemCreateBatchRequest);

    @PostMapping(value = "/v1/report_item/pageList")
    PageResponse<ReportItemDetailResponse> pageList(@RequestBody ReportItemPageRequest pageRequest);
}
