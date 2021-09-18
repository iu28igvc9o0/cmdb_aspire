package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.code;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.code.ICmdbCodeAPI;
import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.code.payload.CmdbCodeGroup;
import com.aspire.ums.cmdb.code.payload.CmdbCodeQuery;
import com.aspire.ums.cmdb.common.Result;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.code.CmdbCodeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbCodeController
 * Author:   zhu.juwang
 * Date:     2019/5/13 18:58
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbCodeController implements ICmdbCodeAPI {

    @Autowired
    private CmdbCodeClient codeClient;

    @Override
    public Result<CmdbCode> list(@RequestBody CmdbCodeQuery query) {
        return codeClient.list(query);
    }

    @Override
    public CmdbCode get(@RequestParam(value = "codeId", required = false) String codeId,
                        @RequestParam(value = "moduleCatalogId", required = false) String moduleCatalogId,
                        @RequestParam(value = "filedCode", required = false) String fieldCode) {
        return codeClient.get(codeId, moduleCatalogId, fieldCode);
    }

    @Override
    public CmdbCode getCodeByCodeId(@RequestParam(value = "codeId", required = false) String codeId) {
        return codeClient.getCodeByCodeId(codeId);
    }

    @Override
    public Map<String, String> valid(@RequestParam("filed") String filed, @RequestParam("value") String value) {
        return codeClient.valid(filed, value);
    }

    @Override
    public Map<String, String> saveCode(HttpServletResponse response, @RequestBody CmdbCode cmdbCode) {
        return codeClient.saveCode(cmdbCode);
    }

    @Override
    public Map<String, String> updateCode(HttpServletResponse response, @RequestBody CmdbCode cmdbCode) {
        return codeClient.updateCode(cmdbCode);
    }

    @Override
    public Map<String, String> delete(@RequestBody CmdbCode cmdbCode) {
        return codeClient.delete(cmdbCode);
    }

    @Override
    public List<String> queryGroupList() {
        return codeClient.queryGroupList();
    }

    @Override
    public List<CmdbCodeGroup> queryCodeListFormatGroup(@RequestParam(value = "catalogId", required = false) String catalogId) {
        return codeClient.queryCodeListFormatGroup(catalogId);
    }

    @Override
    public List<CmdbCode> getCodeListByModuleId(@PathVariable("moduleId") String moduleId) {
        return codeClient.getCodeListByModuleId(moduleId);
    }

    @Override
    public Map<String, Object> getSelfCodeListByModuleId(@RequestParam("moduleId") String moduleId) {
        return codeClient.getSelfCodeListByModuleId(moduleId);
    }

    @Override
    public List<Map<String, String>> getDistinctCodeList() {
        return codeClient.getDistinctCodeList();
    }

    @Override
    public Map<String, Map<String, String>> validCodeValue(@RequestBody Map<String, Object> params) {
        return codeClient.validCodeValue(params);
    }

    @Override
    public JSONObject validateCmdbCodeUnique(@RequestParam("filedCode") String filedCode,
                                             @RequestParam("moduleCatalogId") String moduleCatalogId) {
        return codeClient.validateCmdbCodeUnique(filedCode,moduleCatalogId);
    }

    @Override
    public LinkedList<CmdbCode> getCasParentCodes(@RequestBody List<String> codeIds) {
        return codeClient.getCasParentCodes(codeIds);
    }
}
