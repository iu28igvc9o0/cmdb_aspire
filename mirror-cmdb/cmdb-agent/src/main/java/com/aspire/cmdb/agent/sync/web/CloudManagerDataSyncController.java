package com.aspire.cmdb.agent.sync.web;

import com.aspire.cmdb.agent.sync.schedule.CloudManagerDataSyncSchedule;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CloudManagerDataSyncController
 * Author:   hangfang
 * Date:     2020/4/2
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@RequestMapping("/CloudManageDataSync")
public class CloudManagerDataSyncController {
    @Autowired
    CloudManagerDataSyncSchedule managerDataSyncSchedule;

    @RequestMapping(value = "/syncPhysicalData", method = RequestMethod.POST)
    @ApiOperation(value = "同步物理机数据", notes = "同步物理机数据", tags = {"CMDB DataSync API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "同步成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public void syncPhysicalData() {
        managerDataSyncSchedule.syncPhysicalData();

    }
    @RequestMapping(value = "/syncVmData", method = RequestMethod.POST)
    @ApiOperation(value = "同步云主机数据", notes = "同步云主机数据", tags = {"CMDB DataSync API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "同步成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public void syncVmData() {
        managerDataSyncSchedule.syncVmData();
    }

    @RequestMapping(value = "/stop", method = RequestMethod.POST)
    @ApiOperation(value = "停止同步", notes = "停止同步", tags = {"CMDB DataSync API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "同步成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public void stop() {
        managerDataSyncSchedule.stop();
    }
}
