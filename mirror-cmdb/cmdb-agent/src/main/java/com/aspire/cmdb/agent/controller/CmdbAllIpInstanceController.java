package com.aspire.cmdb.agent.controller;

import com.aspire.cmdb.agent.collect.schedule.MonitorFromKafka;
import com.aspire.cmdb.agent.schedule.CmdbAllIpInstanceSchedule;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbAllIpInstanceController
 * Author:   hangfang
 * Date:     2020/7/27
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
public class CmdbAllIpInstanceController {

    @Autowired
    CmdbAllIpInstanceSchedule ipInstanceSchedule;

    @Autowired
    MonitorFromKafka monitorFromKafka;

    @RequestMapping(value = "/getAllIpInstance", method = RequestMethod.GET)
    @ApiOperation(value = "获取IP_CI表", notes = "获取IP_CI表", tags = {"CMDB IP INSTANCE API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public void getAllIpInstance() {
        ipInstanceSchedule.getAllIpInstance();
    }


    @RequestMapping(value = "/resetMonitor", method = RequestMethod.GET)
    @ApiOperation(value = "重置监控状态", notes = "重置监控状态", tags = {"CMDB MONITOR API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public void resetMonitor() {
        monitorFromKafka.resetMonitor();
    }

}
