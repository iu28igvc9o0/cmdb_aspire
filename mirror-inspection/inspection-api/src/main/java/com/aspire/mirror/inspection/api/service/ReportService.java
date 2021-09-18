package com.aspire.mirror.inspection.api.service ;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.aspire.mirror.inspection.api.dto.vo.ReportVO;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.inspection.api.dto.ReportDetailResponse;
import com.aspire.mirror.inspection.api.dto.ReportPageRequest;
import com.aspire.mirror.inspection.api.dto.ReportTaskDetailResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * inspection_report对外暴露接口
 *
 * 项目名称:  微服务运维平台
 * 包:       com.aspire.mirror.inspection.api.service
 * 类名称:    ReportService.java
 * 类描述:    inspection_report对外暴露接口层
 * 创建人:    ZhangSheng
 * 创建时间:  2018-07-27 13:48:08
 */
@Api(value = "inspection_report")
public interface ReportService{
	/**
     * 根据主键查找inspection_report详情信息
     * @param reportId 巡检报告主键
     * @return ReportVO 巡检报告详情响应对象
     */
    @GetMapping(value = "/v1/report/get/{report_id}")
    @ApiOperation(value = "详情", notes = "根据reportId获取inspection_report详情", tags = {"/v1/report"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = ReportVO.class),
    @ApiResponse(code = 500, message = "内部错误")})
    ReportDetailResponse findByPrimaryKey(@PathVariable("report_id") String reportId);
    
    /**
     * 根据任务主键获取该任务下的所有巡检报告
     * @param taskId:巡检任务id
     * @return PageResponse<ReportDetailResponse>:页面响应对象
     */
    @PostMapping(value="/v1/report/list")
    @ApiOperation(value="列表",notes="列表", tags = {"/v1/report"})
    @ApiResponses(value = {@ApiResponse(code = 200,message = "成功"),
    @ApiResponse(code = 500,message = "内部错误")})
    PageResponse<ReportTaskDetailResponse>  list(@Validated @RequestBody ReportPageRequest reportPageRequest);

    @PutMapping(value = "/v1/report/regenerate/{report_id}")
    @ApiOperation(value = "详情", notes = "根据reportId获取inspection_report详情", tags = {"/v1/report"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = ReportVO.class),
            @ApiResponse(code = 500, message = "内部错误")})
    void regenerate(@PathVariable("report_id") String reportId, @RequestBody Map<String, String> triggerMap);

    @GetMapping(value = "/v1/report/updateReportFilePath")
    void updateReportFilePath(@RequestParam("report_id") String reportId, @RequestParam("file_path") String filePath);
}
