package com.aspire.ums.cmdb.v3.condication.web;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.v3.condication.ICmdbV3CondicationSettingAPI;
import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3CondicationSetting;
import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3CondicationSettingQuery;
import com.aspire.ums.cmdb.v3.condication.service.ICmdbV3CondicationSettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: CmdbV3CondicationSettingController
 * Author:   zhu.juwang
 * Date:     2020/1/10 14:16
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbV3CondicationSettingController implements ICmdbV3CondicationSettingAPI {

    @Autowired
    private ICmdbV3CondicationSettingService settingService;

    @Override
    public Result<CmdbV3CondicationSetting> list(@RequestBody CmdbV3CondicationSettingQuery settingQuery) {
        try {
            return settingService.getCondicationSettingList(settingQuery);
        } catch (Exception e) {
            log.error("查询查询条件配置列表失败: {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Map<String, String> save(@RequestBody CmdbV3CondicationSetting setting) {
        return settingService.insert(setting);
    }

    @Override
    public Map<String, String> update(@RequestBody CmdbV3CondicationSetting setting) {
        return settingService.update(setting);
    }

    @Override
    public Map<String, String> delete(@RequestBody CmdbV3CondicationSetting setting) {
        return settingService.delete(setting);
    }

    @Override
    public CmdbV3CondicationSetting get(@RequestBody CmdbV3CondicationSetting setting) {
        return settingService.get(setting);
    }

    @Override
    public JSONObject validConditionUnique(@RequestParam(value = "code",required = false) String code,
                                           @RequestParam(value = "name",required = false) String name) {
        return settingService.validConditionUnique(code,name);
    }
}
