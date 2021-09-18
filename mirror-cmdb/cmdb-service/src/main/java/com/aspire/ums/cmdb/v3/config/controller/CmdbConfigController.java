package com.aspire.ums.cmdb.v3.config.controller;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.v3.config.ICmdbConfigAPI;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfigRequest;
import com.aspire.ums.cmdb.v3.config.service.ICmdbConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: CmdbConfigController
 * Author:   zhu.juwang
 * Date:     2020/6/11 10:31
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
public class CmdbConfigController implements ICmdbConfigAPI {

    @Autowired
    private ICmdbConfigService configService;

    @Override
    public CmdbConfig getConfigByCode(@RequestParam("configCode") String configCode) {
        return configService.getConfigByCode(configCode);
    }

    @Override
    public Map<String, Object> save(@RequestBody CmdbConfig cmdbConfig) {
        return configService.insert(cmdbConfig);
    }

    @Override
    public Map<String, Object> update(@RequestBody CmdbConfig cmdbConfig) {
        return configService.update(cmdbConfig);
    }

    @Override
    public Map<String, Object> delete(@RequestBody CmdbConfig cmdbConfig) {
        return configService.delete(cmdbConfig);
    }

    @Override
    public Result<CmdbConfig> list(@RequestBody CmdbConfigRequest req) {
        return configService.listByPage(req);
    }

    @Override
    public Map<String,Object> getOne(@RequestParam("configCode") String configCode) {
        CmdbConfig cc = new CmdbConfig();
        cc.setConfigCode(configCode);
        Map<String,Object> rs = new HashMap<>();
        CmdbConfig one = configService.get(cc);
        if(null == one) {
            rs.put("flag",false);
            rs.put("msg","数据库中没有该code");
        } else {
            rs.put("flag",true);
            rs.put("msg",one);
        }
        return rs;
    }
}
