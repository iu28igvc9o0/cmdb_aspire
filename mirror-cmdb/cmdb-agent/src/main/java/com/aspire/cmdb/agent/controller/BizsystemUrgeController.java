package com.aspire.cmdb.agent.controller;

import com.aspire.cmdb.agent.collect.schedule.MonitorFromKafka;
import com.aspire.cmdb.agent.schedule.BizsystemUrgeSchedule;
import com.aspire.cmdb.agent.schedule.CheckApproveRemind;
import com.aspire.cmdb.agent.schedule.SyncModuleData;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: BizsystemUrgeController
 * Author:   hangfang
 * Date:     2020/11/2
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@RequestMapping("/")
@Slf4j
public class BizsystemUrgeController {

    @Autowired
    BizsystemUrgeSchedule urgeSchedule;
    @Autowired
    CheckApproveRemind checkApproveRemind;
    @Autowired
    MonitorFromKafka monitorFromKafka;
    @Autowired
    SyncModuleData syncModuleData;

    @RequestMapping(value = "/scanBizToUrge", method = RequestMethod.GET)
    @ApiOperation(value = "业务系统催办", notes = "业务系统催办", tags = {"CMDB BIZ URGE API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public void scanBizToUrge() {
        urgeSchedule.scanToUrgeBizsystem();
    }

    @RequestMapping(value = "/check/approve", method = RequestMethod.GET)
    @ApiOperation(value = "配置审核工单入口", notes = "配置审核工单入口", tags = {"CMDB CHECK APPROVE API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public void checkApprove() {
        checkApproveRemind.checkApprove();
    }

    @RequestMapping(value = "/sync/testSyncBizLevel", method = RequestMethod.POST)
    @ApiOperation(value = "测试业务系统配额入口", notes = "测试数据同步入口", tags = {"CMDB SYNC BIZ LEVEL API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public void syncBiz(@RequestBody Map<String, Object> params) {
//        Map<String, Object> syncInfo = new HashMap<>();
//        syncInfo.put("code","sync_business_level");
        syncModuleData.startToSync(params);
    }
}
