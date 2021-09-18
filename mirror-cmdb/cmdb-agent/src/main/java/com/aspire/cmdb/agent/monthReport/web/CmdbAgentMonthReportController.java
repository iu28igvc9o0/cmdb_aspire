package com.aspire.cmdb.agent.monthReport.web;

import com.aspire.cmdb.agent.schedule.CmdbMonthReportSchedule;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: RepairEventImportFactory
 * Author:   hangfang
 * Date:     2020/3/3
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@RequestMapping("/monthReport")
public class CmdbAgentMonthReportController {

    @Autowired
    CmdbMonthReportSchedule monthReportSchedule;

    @RequestMapping(value = "/monthReportSchedule", method = RequestMethod.POST)
    @ApiOperation(value = "获取码表信息列表", notes = "获取码表信息列表", tags = {"CMDB Month Report API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public void monthReportSchedule() {

        monthReportSchedule.countMonthReport();
    }
}
