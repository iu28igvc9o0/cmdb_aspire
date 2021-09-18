package com.aspire.mirror.composite.service.inspection;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.service.inspection.payload.CompReportDetailResponse;
import com.aspire.mirror.composite.service.inspection.payload.CompReportItemDetailResponse;
import com.aspire.mirror.composite.service.inspection.payload.CompReportItemPageRequest;
import com.aspire.mirror.composite.service.inspection.payload.CompReportPageRequest;
import com.aspire.mirror.composite.service.inspection.payload.CompReportResponse;
import com.aspire.mirror.ops.api.domain.GeneralResponse;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.migu.tsg.microservice.atomicservice.composite.service.common.payload.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 * 任务对外暴露接口
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.inspection
 * 类名称:    ICompReportService.java
 * 类描述:    任务对外暴露接口
 * 创建人:    JinSu
 * 创建时间:  2018/8/12 10:36
 * 版本:      v1.0
 */
@Api(value = "任务管理")
@RequestMapping("${version}/report")
public interface ICompReportService {
    /**
     * 根据主键查找inspection_report详情信息
     * @param reportId 巡检报告主键
     * @return ReportVO 巡检报告详情响应对象
     */
    @GetMapping(value = "/{report_id}")
    @ApiOperation(value = "详情", notes = "根据reportId获取inspection_report详情", tags = {"Report API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = CompReportResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CompReportResponse findByPrimaryKey(@PathVariable("report_id") String reportId);

    /**
     * 根据主键查找报告基本信息
     * @param reportId 巡检报告主键
     * @return ReportVO 巡检报告详情响应对象
     */
    @GetMapping(value = "/getBaseInfo/{report_id}")
    @ApiOperation(value = "详情", notes = "根据reportId获取inspection_report详情", tags = {"Report API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = CompReportResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CompReportResponse getBaseInfo(@PathVariable("report_id") String reportId);

    /**
     * 根据任务主键获取该任务下的所有巡检报告
     * @param reportPageRequest:巡检任务查询对象
     * @return PageResponse<ReportDetailResponse>:页面响应对象
     */
    @PostMapping(value="/list")
    @ApiOperation(value="列表",notes="列表", tags = {"Report API"})
    @ApiResponses(value = {@ApiResponse(code = 200,message = "成功"),
            @ApiResponse(code = 500,message = "内部错误")})
    PageResponse<CompReportDetailResponse> list(@RequestBody CompReportPageRequest reportPageRequest);

    @PostMapping(value="/reportItem/pageList")
    @ApiOperation(value="报告元素列表",notes="报告元素列表", tags = {"Report API"})
    @ApiResponses(value = {@ApiResponse(code = 200,message = "成功"),
            @ApiResponse(code = 500,message = "内部错误")})
    PageResponse<CompReportItemDetailResponse> listReportItem(@RequestBody CompReportItemPageRequest reportPageRequest);
    /**
     * 导出巡检报告
     * @param reportId 巡检报告主键
     * @return ReportVO 巡检报告详情响应对象
     */
    @GetMapping(value = "/export/{type}/{report_id}")
    @ApiOperation(value = "导出报告", notes = "导出报告", tags = {"Report API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    void export(@PathVariable("report_id") String reportId, @PathVariable("type") String type,  HttpServletResponse response) throws FileNotFoundException, Exception;

    @GetMapping(value = "/regenerate/{report_id}")
    @ApiOperation(value = "重新生成", notes = "重新生成报告", tags = {"Report API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    BaseResponse regenerate(@PathVariable("report_id") String reportId);

    @PostMapping(value = "/uploadReport", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "上传报告", notes = "上传报告", response = GeneralResponse.class, tags = {"Report API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse uploadReport(@RequestParam("report_id") String reportId, @RequestParam("file") MultipartFile file);

    @PostMapping(value = "/downloadReport", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "下载报告", notes = "下载报告", response = GeneralResponse.class, tags = {"Report API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    void downloadReport(@RequestBody Map<String, String> downParam);
}
