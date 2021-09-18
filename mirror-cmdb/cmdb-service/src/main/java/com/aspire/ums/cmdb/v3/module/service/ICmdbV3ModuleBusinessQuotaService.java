package com.aspire.ums.cmdb.v3.module.service;

import java.util.List;
import java.util.Map;

/**
 * @projectName: ICmdbV3ModuleBusinessQuotaService
 * @description: 接口
 * @author: luowenbo
 * @create: 2020-08-04 15:51
 **/
public interface ICmdbV3ModuleBusinessQuotaService {
    List<Map<String,Object>> getAllBusinessQuotaInfo();

    List<Map<String,Object>> getNeedChargeBusinessQuotaInfo();

}
