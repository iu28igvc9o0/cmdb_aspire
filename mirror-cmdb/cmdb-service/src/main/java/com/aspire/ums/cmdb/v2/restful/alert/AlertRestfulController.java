package com.aspire.ums.cmdb.v2.restful.alert;

import com.aspire.ums.cmdb.restful.alert.IAlertRestfulAPI;
import com.aspire.ums.cmdb.systemManager.service.BizSystemService;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v2.restful.service.IAlertRestfulService;
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
    private BizSystemService bizSystemService;
    @Autowired
    private ICmdbInstanceService cmdbInstanceService;
    @Autowired
    private IAlertRestfulService restfulService;

    @Override
    public Map<String, String> getDepartmentInfoByBizSystem(@RequestParam("bizSystem") String bizSystem) {
        log.info("Request AlertRestfulController.getDepartmentInfoByBizSystem bisSystem -> {}", bizSystem);
        return bizSystemService.getDepartmentInfoByBizSystem(bizSystem);
    }

    @Override
    public List<Map<String, String>> getProjectNameByIdcType(String idcType) {
        log.info("Request AlertRestfulController.getProjectNameByIdcType idcType -> {}", idcType);
        return cmdbInstanceService.getProjectNameByIdcType(idcType);
    }

    @Override
    public List<Map<String, Object>> statisticsDeviceByIdcType(@RequestBody Map<String, Object> params) {
        return restfulService.statisticsDeviceByIdcType(params);
    }

    @Override
    public List<Map<String, Object>> statisticsDeviceByDepartment1(@RequestBody Map<String, Object> params) {
        return restfulService.statisticsDeviceByDepartment1(params);
    }

    @Override
    public List<Map<String, Object>> statisticsDeviceByBizSystem(@RequestBody Map<String, Object> params) {
        return restfulService.statisticsDeviceByBizSystem(params);
    }

    @Override
    public List<Map<String, Object>> statisticsDepartmentForAvailability(@RequestParam(value = "department1", required = false) String department1) {
        return restfulService.statisticsDepartmentForAvailability(department1);
    }

    /**
     * 判断设备删除状态
     *
     * @param instanceList 设备列表
     */
    @Override
    public List<Map<String, Object>> checkInstanceDeleteStatus(@RequestBody List<Map<String, String>> instanceList) {
        return restfulService.checkInstanceDeleteStatus(instanceList);
    }
}
