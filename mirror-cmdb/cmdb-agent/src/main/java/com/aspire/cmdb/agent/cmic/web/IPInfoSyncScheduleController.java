package com.aspire.cmdb.agent.cmic.web;

import com.aspire.cmdb.agent.cmic.schedule.IPInfoSyncSchedule;
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
@RequestMapping("/IPInfo")
public class IPInfoSyncScheduleController {

    @Autowired
    IPInfoSyncSchedule ipInfoSyncSchedule;

    @RequestMapping(value = "/startCountSegmentIp", method = RequestMethod.POST)
    @ApiOperation(value = "执行定时任务统计网段中各状态ip", notes = "执行定时任务统计网段中各状态ip", tags = {"CMDB IPInfo API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public void startCountSegmentIp() {
        ipInfoSyncSchedule.startCountSegmentIp();
    }

    @RequestMapping(value = "/startSyncBusiness", method = RequestMethod.POST)
    @ApiOperation(value = "执行定时任务更新业务", notes = "执行定时任务更新业务", tags = {"CMDB IPInfo API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public void Schedule() {
        ipInfoSyncSchedule.startSyncBusiness();
    }
}
