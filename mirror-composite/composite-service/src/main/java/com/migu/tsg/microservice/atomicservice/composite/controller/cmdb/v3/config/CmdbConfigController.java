package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.v3.config;

import com.aspire.mirror.composite.service.cmdb.v3.config.ICmdbConfigAPI;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfigRequest;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.v3.config.ICmdbConfigClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    private ICmdbConfigClient configClient;
    @Override
    public CmdbConfig getConfigByCode(@RequestParam("configCode") String configCode) {
        return configClient.getConfigByCode(configCode);
    }

    @Override
    public Map<String, Object> save(@RequestBody CmdbConfig cmdbConfig) {
        return configClient.save(cmdbConfig);
    }

    @Override
    public Map<String, Object> update(@RequestBody CmdbConfig cmdbConfig) {
        return configClient.update(cmdbConfig);
    }

    @Override
    public Map<String, Object> delete(@RequestBody CmdbConfig cmdbConfig) {
        return configClient.delete(cmdbConfig);
    }

    @Override
    public Result<CmdbConfig> list(@RequestBody CmdbConfigRequest req) {
        return configClient.list(req);
    }

    @Override
    public Map<String,Object> getOne(@RequestParam("configCode") String configCode) {
        return configClient.getOne(configCode);
    }
}
