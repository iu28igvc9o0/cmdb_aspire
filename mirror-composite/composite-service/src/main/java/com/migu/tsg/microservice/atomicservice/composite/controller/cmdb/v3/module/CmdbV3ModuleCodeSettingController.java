package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.v3.module;

import com.aspire.mirror.composite.service.cmdb.v3.module.ICmdbV3ModuleCodeSettingAPI;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCodeSetting;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.v3.module.ICmdbV3ModuleCodeSettingClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbV3ModuleCodeSettingController
 * Author:   hangfang
 * Date:     2020/2/12
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbV3ModuleCodeSettingController implements ICmdbV3ModuleCodeSettingAPI {

    @Autowired
    private ICmdbV3ModuleCodeSettingClient moduleCodeSettingClient;
    @Override
    public Map<String, Object> save(@RequestBody List<CmdbV3ModuleCodeSetting> moduleCodeSettings) {
        return moduleCodeSettingClient.save(moduleCodeSettings);
    }
}
