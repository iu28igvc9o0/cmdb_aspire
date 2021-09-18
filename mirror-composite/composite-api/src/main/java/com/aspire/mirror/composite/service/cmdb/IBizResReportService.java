package com.aspire.mirror.composite.service.cmdb;

import com.aspire.mirror.composite.service.inspection.payload.Result;
import com.aspire.ums.cmdb.report.playload.BizResReport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 项目名称:
 * 包: com.aspire.mirror.composite.service.cmdb
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/6/14 16:44
 * 版本: v1.0
 */
@Api(value = "资源报表")
@RequestMapping("${version}/cmdb/bizResReport")
public interface IBizResReportService {
    
    @GetMapping(value = "/list")
    @ApiOperation(value = "查询业务资源报表列表", notes = "查询业务资源报表列表", tags = {"CMDB Report API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<BizResReport> getAllBizResReportData(@RequestParam(value = "pageNum", required = false) int pageNum,
                                                @RequestParam(value = "pageSize", required = false) int pageSize,
                                                @RequestParam(value = "bizSystem", required = false) String bizSystem,
                                                @RequestParam(value = "idcType", required = false) String idcType,
                                                @RequestParam(value = "department1", required = false) String department1,
                                                @RequestParam(value = "department2", required = false) String department2,
                                                @RequestParam(value = "deviceType", required = false) String deviceType,
                                                @RequestParam(value = "createTime1", required = false) String createTime1,
                                                @RequestParam(value = "createTime2", required = false) String createTime2);
    
    @RequestMapping(value = "/exportBizRes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "导出业务资源报表数据", notes = "导出业务资源报表数据", tags = {"CMDB Report API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    void exportBizRes(@RequestParam(value = "bizSystem", required = false) String bizSystem,
                      @RequestParam(value = "idcType", required = false) String idcType,
                      @RequestParam(value = "department1", required = false) String department1,
                      @RequestParam(value = "department2", required = false) String department2,
                      @RequestParam(value = "deviceType", required = false) String deviceType,
                      @RequestParam(value = "createTime1", required = false) String createTime1,
                      @RequestParam(value = "createTime2", required = false) String createTime2);
}
