package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.restful.beixiang;

import com.aspire.mirror.composite.service.cmdb.restful.beixiang.IBeiXiangRestfulAPI;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.restful.beixiang.IBeiXiangRestfulClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: RepairEventImportFactory
 * Author:   hangfang
 * Date:     2020/1/8
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class IBeiXiangRestfulController implements IBeiXiangRestfulAPI {

    @Autowired
    private IBeiXiangRestfulClient beiXiangRestfulClient;

    @Override
    public List<Map<String, Object>> countIdcTypePer(@RequestParam(value = "top", required = false) Integer top) {
        return beiXiangRestfulClient.countIdcTypePer(top);
    }

    @Override
    public List<Map<String, Object>> countIdcDeviceNumber(@RequestParam(value = "top", required = false)Integer top) {
        return beiXiangRestfulClient.countIdcDeviceNumber(top);
    }

    @Override
    public List<Map<String, Object>> countDepart1BizPer(@RequestParam(value = "top", required = false)Integer top) {
        return beiXiangRestfulClient.countDepart1BizPer(top);
    }

    @Override
    public List<Map<String, Object>> countDepart1BizNumber(@RequestParam(value = "top", required = false)Integer top) {
        return beiXiangRestfulClient.countDepart1BizNumber(top);
    }

    @Override
    public List<Map<String, Object>> countDepart1DeviceNumber(@RequestParam(value = "top", required = false)Integer top) {
        return beiXiangRestfulClient.countDepart1DeviceNumber(top);
    }

    @Override
    public List<Map<String, Object>> countDeviceByIdcType(@RequestParam(value = "idcType", required = false)String idcType) {
        return beiXiangRestfulClient.countDeviceByIdcType(idcType);
    }

    @Override
    public List<Map<String, Object>> countDepart1Device(@RequestParam(value = "top", required = false)Integer top) {
        return beiXiangRestfulClient.countDepart1Device(top);
    }

    @Override
    public List<Map<String, Object>> roomDevicePer() {
        return beiXiangRestfulClient.roomDevicePer();
    }
}
