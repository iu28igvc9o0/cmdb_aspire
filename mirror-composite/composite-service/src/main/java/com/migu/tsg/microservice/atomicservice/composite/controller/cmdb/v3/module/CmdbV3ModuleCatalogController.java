package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.v3.module;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.v3.module.ICmdbV3ModuleCatalogAPI;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCatalog;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.v3.module.ICmdbV3ModuleCatalogClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: CmdbV3ModuleCatalogController
 * Author:   zhu.juwang
 * Date:     2020/1/15 10:49
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbV3ModuleCatalogController implements ICmdbV3ModuleCatalogAPI {

    @Autowired
    private ICmdbV3ModuleCatalogClient catalogClient;

    @Override
    public List<CmdbV3ModuleCatalog> getFirstLevel() {
        return catalogClient.getFirstLevel();
    }

    @Override
    public JSONObject getAllLevelTree(@RequestParam(value = "catalogId", required = false) String catalogId) {
        return catalogClient.getAllLevelTree(catalogId);
    }

    @Override
    public JSONObject create(@RequestBody CmdbV3ModuleCatalog catalog) {
        return catalogClient.create(catalog);
    }

    @Override
    public JSONObject update(@RequestBody CmdbV3ModuleCatalog catalog) {
        return catalogClient.update(catalog);
    }

    @Override
    public JSONObject updateSort(@RequestParam("catalogId") String catalogId, @RequestParam("sortType") String sortType) {
        return catalogClient.updateSort(catalogId, sortType);
    }

    @Override
    public JSONObject validCatalog(@RequestParam("parentCatalogId") String parentCatalogId,
                                   @RequestParam("catalogCode") String catalogCode,
                                   @RequestParam("catalogName") String catalogName) {
        return catalogClient.validCatalog(parentCatalogId, catalogCode, catalogName);
    }

    @Override
    public JSONObject delete(@RequestBody CmdbV3ModuleCatalog catalog) {
        return catalogClient.delete(catalog);
    }
}
