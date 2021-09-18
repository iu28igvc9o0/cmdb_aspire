package com.aspire.ums.cmdb.v2.assessment.web;

import com.aspire.ums.cmdb.assessment.IDeviceTypeModelAPI;
import com.aspire.ums.cmdb.assessment.payload.CmdbDeviceTypeModel;
import com.aspire.ums.cmdb.v2.assessment.service.ICmdbDeviceTypeModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ICmdbDeviceTypeModelService typeModelService;
    /**
     * 根据型号获取设备类型
     * @return 设备类型
     */
    @Override
    public CmdbDeviceTypeModel getDeviceTypeByModel(String deviceModel) {
        return typeModelService.getDeviceTypeByModel(deviceModel);
    }

    /**
     * 根据类型获取设备型号
     * @return 设备类型
     */
    @Override
    public List<CmdbDeviceTypeModel> getModelByDeviceType(String deviceType) {
        return typeModelService.getModelByDeviceType(deviceType);
    }
}
