package com.aspire.ums.cmdb.v3.module.web;

import com.aspire.ums.cmdb.v3.module.ICmdbV3ModuleBusinessQuotaAPI;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleBusinessQuotaService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @projectName: CmdbV3ModuleBusinessQuotaController
 * @description: 类
 * @author: luowenbo
 * @create: 2020-08-04 15:53
 **/
@RestController
public class CmdbV3ModuleBusinessQuotaController implements ICmdbV3ModuleBusinessQuotaAPI {
    @Autowired
    private ICmdbV3ModuleBusinessQuotaService businessQuotaService;

    @Override
    public List<Map<String, Object>> getAllBusinessQuotaInfo() {
        return businessQuotaService.getAllBusinessQuotaInfo();
    }

    @Override
    public List<Map<String, Object>> getNeedChargeBusinessQuotaInfo() {
        return businessQuotaService.getNeedChargeBusinessQuotaInfo();
    }
}
