package com.aspire.mirror.composite.service.cmdb.report;

import com.aspire.ums.cmdb.report.playload.Cmdb31ProvinceInsertVO;
import com.aspire.ums.cmdb.report.playload.Cmdb31ProvinceReportReq;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.report
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/6/14 16:32
 * 版本: v1.0
 */
@RequestMapping("${version}/cmdb/province/report")
public interface IProvinceReportAPI {

    @PostMapping("/getSetting")
    @ApiOperation(value = "获取报表统计列表", notes = "获取报表统计列表", tags = {"CMDB REPORT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> getSetting(@RequestBody Map<String, String> queryParams);

    @PostMapping("/save")
    @ApiOperation(value = "保存上报省份数据", notes = "保存上报省份数据", tags = {"CMDB REPORT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> save(@RequestBody Map<String,Object> insertDataBox);

    @PostMapping("/getSettingData")
    @ApiOperation(value = "获取省份上报数据", notes = "获取省份上报数据", tags = {"CMDB REPORT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, List<Map<String, Object>>> getSettingData(@RequestBody Cmdb31ProvinceReportReq req);

    @PostMapping("/exportReport")
    @ApiOperation(value = "导出省份上报数据", notes = "导出省份上报数据", tags = {"CMDB REPORT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "导出成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> exportReport(@RequestBody Map<String, String> queryParams);

    @PostMapping("/exportReportTemplate")
    @ApiOperation(value = "根据资源归属和表名导出省份上报模板", notes = "根据资源归属和表名导出省份上报模板", tags = {"CMDB REPORT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "导出成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> exportReportTemplate(@RequestBody Map<String, String> queryParams, HttpServletResponse response);

    @PostMapping("/submitCheck")
    @ApiOperation(value = "上报数据提交核对", notes = "上报数据提交核对", tags = {"CMDB REPORT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> submitCheck(@RequestBody Map<String, String> queryParams);

    @PostMapping("/submitApprove")
    @ApiOperation(value = "上报数据审核", notes = "上报数据审核", tags = {"CMDB REPORT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> submitApprove(@RequestBody Map<String, String> queryParams);

    @PostMapping("/getProvinceStatus")
    @ApiOperation(value = "获取省份上报状态", notes = "获取省份上报状态", tags = {"CMDB REPORT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, String>> getProvinceStatus(@RequestBody Map<String, String> queryParam);
}
