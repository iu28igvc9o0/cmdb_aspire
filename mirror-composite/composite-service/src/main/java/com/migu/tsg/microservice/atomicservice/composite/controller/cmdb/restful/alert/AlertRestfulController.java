package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.restful.alert;

import com.aspire.mirror.composite.service.cmdb.restful.alert.IAlertRestfulAPI;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.restful.alert.IAlertRestfulClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: AlertRestfulController
 * Author:   zhu.juwang
 * Date:     2019/7/29 17:10
 * Description: 该接口类用来专门提供接口给告警模块使用
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class AlertRestfulController implements IAlertRestfulAPI {

    @Autowired
    private IAlertRestfulClient alertRestfulClient;

    @Override
    public Map<String, String> getDepartmentInfoByBizSystem(@RequestParam("bizSystem") String bizSystem) {
        log.info("Request AlertRestfulController.getDepartmentInfoByBizSystem bisSystem -> {}", bizSystem);
        return alertRestfulClient.getDepartmentInfoByBizSystem(bizSystem);
    }

    @Override
    public List<Map<String, String>> getProjectNameByIdcType(String idcType) {
        log.info("Request AlertRestfulController.getProjectNameByIdcType idcType -> {}", idcType);
        return alertRestfulClient.getProjectNameByIdcType(idcType);
    }

    @Override
    public List<Map<String, String>> statisticsDeviceByIdcType(@RequestBody Map<String, Object> params) {
        return alertRestfulClient.statisticsDeviceByIdcType(params);
    }

    @Override
    public List<Map<String, String>> statisticsDeviceByDepartment1(@RequestBody Map<String, Object> params) {
        return alertRestfulClient.statisticsDeviceByDepartment1(params);
    }

    @Override
    public List<Map<String, String>> statisticsDeviceByBizSystem(@RequestBody Map<String, Object> params) {
        return alertRestfulClient.statisticsDeviceByBizSystem(params);
    }

    @Override
    public List<Map<String, Object>> statisticsDepartmentForAvailability(@RequestParam(value = "department1", required = false) String department1) {
        return alertRestfulClient.statisticsDepartmentForAvailability(department1);
    }
}
