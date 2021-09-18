package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.v3.condication;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.v3.condication.ICmdbV3CondicationSettingAPI;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3CondicationSetting;
import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3CondicationSettingQuery;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.v3.condication.ICmdbV3CondicationSettingClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: CmdbV3CondicationSettingController
 * Author:   zhu.juwang
 * Date:     2020/1/17 15:18
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
public class CmdbV3CondicationSettingController implements ICmdbV3CondicationSettingAPI {

    @Autowired
    private ICmdbV3CondicationSettingClient settingClient;

    @Override
    public Result<CmdbV3CondicationSetting> list(@RequestBody CmdbV3CondicationSettingQuery settingQuery) {
        return settingClient.list(settingQuery);
    }

    @Override
    public Map<String, String> save(@RequestBody CmdbV3CondicationSetting setting) {
        return settingClient.save(setting);
    }

    @Override
    public Map<String, String> update(@RequestBody CmdbV3CondicationSetting setting) {
        return settingClient.update(setting);
    }

    @Override
    public Map<String, String> delete(@RequestBody CmdbV3CondicationSetting setting) {
        return settingClient.delete(setting);
    }

    @Override
    public CmdbV3CondicationSetting get(@RequestBody CmdbV3CondicationSetting setting) {
        return settingClient.get(setting);
    }

    @Override
    public JSONObject validConditionUnique(@RequestParam(value = "code",required = false) String code,
                                           @RequestParam(value = "name",required = false) String name) {
        return settingClient.validConditionUnique(code, name);
    }
}
