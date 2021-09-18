package com.aspire.mirror.composite.service.cmdb.restful.bpm;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: IIdcMonthReportAPI
 * Author:   zhu.juwang
 * Date:     2020/5/14 11:22
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("${version}/cmdb/idctenant")
public interface IIdcMonthReportAPI {

    @GetMapping("/bpm/monthlyOperationReport/getOrderReport")
    @ApiOperation(value = "运营月报大屏工单统计", notes = "运营月报大屏工单统计", tags = {"Cmdb To IDCTenant BPM Restful API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Object getOrderReport(@RequestParam("month") String month, @RequestParam("idcType") String pool);

    @GetMapping("/bpm/monthlyOperationReport/getAlarmErrorReport")
    @ApiOperation(value = "运营月报大屏故障统计", notes = "运营月报大屏故障统计", tags = {"Cmdb To IDCTenant BPM Restful API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Object getAlertErrorReport(@RequestParam("month") String month, @RequestParam("idcType") String pool);
}
