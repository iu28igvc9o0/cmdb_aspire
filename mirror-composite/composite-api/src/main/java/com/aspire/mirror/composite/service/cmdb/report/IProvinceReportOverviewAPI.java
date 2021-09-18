package com.aspire.mirror.composite.service.cmdb.report;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.report.playload.Cmdb31ProvinceTable;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IProvinceReportOverviewAPI
 * Author:   hangfang
 * Date:     2020/5/8
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("${version}/cmdb/province/report")
public interface IProvinceReportOverviewAPI {
    @GetMapping("/getReportOverview")
    @ApiOperation(value = "根据表名月份获取总览表数据", notes = "根据表名月份获取总览表数据", tags = {"CMDB REPORT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> getReportOverview(@RequestParam("tableId") String tableId,
                                          @RequestParam("month") String month,
                                          @RequestParam("type") String type);


    @GetMapping("/table/listByOwnerAndPage")
    @ApiOperation(value = "根据归属和类型返回表", notes = "根据类型返回表", tags = {"CMDB REPORT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Cmdb31ProvinceTable> listByOwnerAndPage(@RequestParam(value = "resourceOwner", required = false) String resourceOwner,
                                                 @RequestParam("type") String type);

    @GetMapping("/exportReportOverview")
    @ApiOperation(value = "导出总览表数据", notes = "导出总览表数据", tags = {"CMDB REPORT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    void exportReportOverview(@RequestParam("tableId") String tableId,
                              @RequestParam("month") String month,
                              @RequestParam("type") String type,
                              HttpServletResponse response);

    @GetMapping("/exportAll")
    @ApiOperation(value = "导出所有报表数据", notes = "导出所有报表数据", tags = {"CMDB REPORT API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject exportAllReport(@RequestParam("month") String month,
                               HttpServletResponse response);
}
