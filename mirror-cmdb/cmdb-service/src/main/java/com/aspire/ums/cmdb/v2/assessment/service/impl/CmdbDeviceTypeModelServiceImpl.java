package com.aspire.ums.cmdb.v2.assessment.service.impl;

import com.aspire.ums.cmdb.assessment.payload.CmdbDeviceTypeModel;
import com.aspire.ums.cmdb.v2.assessment.mapper.CmdbDeviceTypeModelMapper;
import com.aspire.ums.cmdb.v2.assessment.service.ICmdbDeviceTypeModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service
public class CmdbDeviceTypeModelServiceImpl implements ICmdbDeviceTypeModelService {

    @Autowired
    private CmdbDeviceTypeModelMapper typeModelMapper;

    @Override
    public CmdbDeviceTypeModel getDeviceTypeByModel(String deviceModel) {
        return typeModelMapper.getDeviceTypeByModel(deviceModel);
    }

    @Override
    public List<CmdbDeviceTypeModel> getModelByDeviceType(String deviceType) {
        return typeModelMapper.getModelByDeviceType(deviceType);
    }
}
