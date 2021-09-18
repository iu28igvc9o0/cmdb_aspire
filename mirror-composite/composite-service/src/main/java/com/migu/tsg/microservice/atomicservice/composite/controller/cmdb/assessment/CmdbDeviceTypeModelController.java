package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.assessment;

import com.aspire.mirror.composite.service.cmdb.assessment.IDeviceTypeModelAPI;
import com.aspire.ums.cmdb.assessment.payload.CmdbDeviceTypeModel;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.assessment.CmdbDeviceTypeModelClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: RepairEventImportFactory
 * Author:   hangfang
 * Date:     2019/12/19
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbDeviceTypeModelController implements IDeviceTypeModelAPI {

    @Autowired
    private CmdbDeviceTypeModelClient typeModelClient;
    @Override
    public CmdbDeviceTypeModel getDeviceTypeByModel(@RequestParam("deviceModel") String deviceModel) {
        return typeModelClient.getDeviceTypeByModel(deviceModel);
    }

    @Override
    public List<CmdbDeviceTypeModel> getModelByDeviceType(@RequestParam("deviceType") String deviceType) {
        return typeModelClient.getModelByDeviceType(deviceType);
    }
}
