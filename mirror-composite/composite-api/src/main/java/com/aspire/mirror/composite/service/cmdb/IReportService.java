package com.aspire.mirror.composite.service.cmdb;

import com.aspire.ums.cmdb.report.playload.Report;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
@RequestMapping("${version}/cmdb/report")
public interface IReportService {
    
    @GetMapping(value = "/listReportByBizSystem")
    @ApiOperation(value = "查询报表列表", notes = "查询报表列表", tags = {"CMDB Report API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Report> listReportByBizSystem(@RequestParam(value = "bizSystem",required = false) String bizSystem,
                                  @RequestParam(value = "idcType",required = false) String idcType,
                                  @RequestParam(value = "department1",required = false) String department1,
                                  @RequestParam(value = "department2",required = false) String department2);
    
    @RequestMapping(value = "/exportReportByBizSystem", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "导出报表数据", notes = "导出报表数据", tags = {"CMDB Report API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    void exportReportByBizSystem(@RequestParam(value = "bizSystem",required = false) String bizSystem,
                @RequestParam(value = "idcType",required = false) String idcType,
                @RequestParam(value = "department1",required = false) String department1,
                @RequestParam(value = "department2",required = false) String department2);

    @GetMapping("/listReportByDepartment")
    @ApiOperation(value = "获取报表统计列表", notes = "获取报表统计列表", tags = {"CMDB REPORT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Report> listReportByDepartment(@RequestParam(value = "idcType",required = false) String idcType,
                                        @RequestParam(value = "department1",required = false) String department1,
                                        @RequestParam(value = "department2",required = false) String department2);

    @RequestMapping(value = "/exportReportByDepartment", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "导出报表数据", notes = "导出报表数据", tags = {"CMDB Report API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    void exportReportByDepartment(@RequestParam(value = "idcType",required = false) String idcType,
                                                       @RequestParam(value = "department1",required = false) String department1,
                                                       @RequestParam(value = "department2",required = false) String department2);
}
