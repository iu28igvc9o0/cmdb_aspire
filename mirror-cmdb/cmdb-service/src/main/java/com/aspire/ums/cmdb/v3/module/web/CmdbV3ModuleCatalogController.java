package com.aspire.ums.cmdb.v3.module.web;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.v3.module.ICmdbV3ModuleCatalogAPI;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCatalog;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleCatalogService;
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
    private ICmdbV3ModuleCatalogService catalogService;

    @Override
    public List<CmdbV3ModuleCatalog> getFirstLevel() {
        try {
            log.info("Request info CmdbV3ModuleCatalogController.getFirstLevel ----> ");
            return catalogService.getFirstLevel();
        } catch (Exception e) {
            log.error("获取根模型分组失败.", e);
        }
        return null;
    }

    @Override
    public JSONObject getAllLevelTree(@RequestParam(value = "catalogId", required = false) String catalogId) {
        try {
            log.info("Request info CmdbV3ModuleCatalogController.getAllLevelTree ----> ");
            return catalogService.getAllLevelTree(catalogId);
        } catch (Exception e) {
            log.error("获取模型分组树失败.", e);
        }
        return null;
    }

    @Override
    public JSONObject create(@RequestBody CmdbV3ModuleCatalog catalog) {
        JSONObject returnJson = new JSONObject();
        try {
            log.info("Request info CmdbV3ModuleCatalogController.create ----> params: {}", catalog);
            return catalogService.insert(catalog);
        } catch (Exception e) {
            returnJson.put("flag", "error");
            returnJson.put("msg", "新增模型失败" + e.getMessage());
        }
        return returnJson;
    }

    @Override
    public JSONObject update(@RequestBody CmdbV3ModuleCatalog catalog) {
        JSONObject returnJson = new JSONObject();
        try {
            log.info("Request info CmdbV3ModuleCatalogController.update ----> params: {}", catalog);
            return catalogService.update(catalog);
        } catch (Exception e) {
            returnJson.put("flag", "error");
            returnJson.put("msg", "修改模型失败" + e.getMessage());
        }
        return returnJson;
    }

    @Override
    public JSONObject updateSort(@RequestParam("catalogId") String catalogId, @RequestParam("sortType") String sortType) {
        JSONObject returnJson = new JSONObject();
        try {
            log.info("Request info CmdbV3ModuleCatalogController.updateSort ----> catalogId: {}  sortType: {}", catalogId, sortType);
            catalogService.updateSort(catalogId, sortType);
            returnJson.put("flag", "success");
        } catch (Exception e) {
            returnJson.put("flag", "error");
            returnJson.put("msg", "排序失败" + e.getMessage());
        }
        return returnJson;
    }

    @Override
    public JSONObject validCatalog(@RequestParam("parentCatalogId") String parentCatalogId,
                                   @RequestParam("catalogCode") String catalogCode,
                                   @RequestParam("catalogName") String catalogName) {
        JSONObject returnJson = new JSONObject();
        try {
            log.info("Request info CmdbV3ModuleCatalogController.validCatalog ----> parentCatalogId: {}  catalogCode: {}  catalogName: {}",
                    parentCatalogId, catalogCode, catalogName);
            return catalogService.validCatalog(parentCatalogId, catalogCode, catalogName);
        } catch (Exception e) {
            returnJson.put("flag", "error");
            returnJson.put("msg", "验证失败" + e.getMessage());
        }
        return returnJson;
    }

    @Override
    public JSONObject delete(@RequestBody CmdbV3ModuleCatalog catalog) {
        JSONObject returnJson = new JSONObject();
        try {
            log.info("Request info CmdbV3ModuleCatalogController.delete ----> catalog: {}", catalog);
            return catalogService.delete(catalog);
        } catch (Exception e) {
            returnJson.put("flag", "error");
            returnJson.put("msg", "验证失败" + e.getMessage());
        }
        return returnJson;
    }
}
