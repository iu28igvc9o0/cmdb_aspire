package com.aspire.mirror.composite.service.inspection;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.service.inspection.payload.CompReportDetailResponse;
import com.aspire.mirror.composite.service.inspection.payload.CompReportPageRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 设备对外服务
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.inspection
 * 类名称:    ICompDeviceService.java
 * 类描述:    设备对外服务
 * 创建人:    JinSu
 * 创建时间:  2018/8/13 15:59
 * 版本:      v1.0
 */
@Api(value = "设备管理")
@RequestMapping("${version}/device")
public interface ICompDeviceService {
//    /**
//     * 根据任务主键获取该任务下的所有巡检报告
//     * @param taskId:巡检任务id
//     * @return PageResponse<ReportDetailResponse>:页面响应对象
//     */
//    @PostMapping(value="/list")
//    @ApiOperation(value="列表",notes="列表", tags = {"Device API"})
//    @ApiResponses(value = {@ApiResponse(code = 200,message = "成功"),
//            @ApiResponse(code = 500,message = "内部错误")})
//    List<CompDeviceDetailResponse> list(@RequestBody CompReportPageRequest reportPageRequest);
}
