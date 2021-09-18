package com.aspire.ums.cmdb.v3.module.service.impl;

import com.aspire.ums.cmdb.v3.module.mapper.CmdbV3ModuleBusinessQuotaMapper;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleBusinessQuotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @projectName: CmdbV3ModuleBusinessQuotaServiceImpl
 * @description: 类
 * @author: luowenbo
 * @create: 2020-08-04 15:51
 **/
@Service
public class CmdbV3ModuleBusinessQuotaServiceImpl implements ICmdbV3ModuleBusinessQuotaService {
    @Autowired
    private CmdbV3ModuleBusinessQuotaMapper businessQuotaMapper;

    @Override
    public List<Map<String, Object>> getAllBusinessQuotaInfo() {
        return businessQuotaMapper.getAllBusinessQuotaInfo();
    }

    @Override
    public List<Map<String, Object>> getNeedChargeBusinessQuotaInfo() {
        return businessQuotaMapper.getNeedChargeBusinessQuotaInfo();
    }
}
