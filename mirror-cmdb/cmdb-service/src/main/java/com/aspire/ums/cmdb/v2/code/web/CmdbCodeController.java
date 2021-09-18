package com.aspire.ums.cmdb.v2.code.web;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.code.ICmdbCodeAPI;
import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.code.payload.CmdbCodeGroup;
import com.aspire.ums.cmdb.code.payload.CmdbCodeQuery;
import com.aspire.ums.cmdb.code.payload.CmdbSimpleCode;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.common.throwable.CmdbRuntimeException;
import com.aspire.ums.cmdb.module.payload.Module;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.code.service.ICmdbCodeService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
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
    private ICmdbCodeService cmdbCodeService;
    @Autowired
    private ModuleService moduleService;

    @Override
    public Result<CmdbCode> list(@RequestBody CmdbCodeQuery query) {
        try {
            log.info("Request CmdbCodeController.list data -> {}", JSONObject.toJSON(query));
            return cmdbCodeService.list(query);
        } catch (Exception e) {
            log.error("Query cmdb code error. Cause: {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public CmdbCode get(@RequestParam(value = "codeId", required = false) String codeId,
                        @RequestParam(value = "moduleCatalogId", required = false) String moduleCatalogId,
                        @RequestParam(value = "filedCode", required = false) String filedCode) {
        try {
            log.info("Request CmdbCodeController.getCodeByCodeId codeId -> {}", codeId);
            CmdbCode queryCode = new CmdbCode();
            queryCode.setCodeId(codeId);
            queryCode.setModuleCatalogId(moduleCatalogId);
            queryCode.setFiledCode(filedCode);
            return cmdbCodeService.get(queryCode);
        } catch (Exception e) {
            log.error("Query cmdb code error. Cause: {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public CmdbCode getCodeByCodeId(@RequestParam(value = "codeId") String codeId) {
        try {
            log.info("Request CmdbCodeController.getCodeByCodeId codeId -> {}", codeId);
            CmdbCode queryCode = new CmdbCode();
            queryCode.setCodeId(codeId);
            return cmdbCodeService.get(queryCode);
        } catch (Exception e) {
            log.error("Query cmdb code error. Cause: {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Map<String, String> saveCode(HttpServletResponse response, @RequestBody CmdbCode cmdbCode) {
        Map<String, String> returnMap = new HashMap<>();
        try {
            log.info("Request CmdbCodeController.saveCode  data -> {}", JSONObject.toJSON(cmdbCode));
            if (StringUtils.isNotEmpty(cmdbCode.getCodeId())) {
                returnMap = cmdbCodeService.update(cmdbCode);
            } else {
                returnMap = cmdbCodeService.insert(cmdbCode);
            }
        } catch (Exception e) {
            log.error("Save cmdb code error. Cause: {}", e.getMessage(), e);
            returnMap.put("flag", "error");
            returnMap.put("msg", "保存码表失败:" + e.getMessage());
        }
        return returnMap;
    }

    @Override
    public Map<String, String> updateCode(HttpServletResponse response, @RequestBody CmdbCode cmdbCode) {
        Map<String, String> returnMap = new HashMap<>();
        try {
            log.info("Request CmdbCodeController.updateCode  data -> {}", JSONObject.toJSON(cmdbCode));
            returnMap = cmdbCodeService.update(cmdbCode);
        } catch (Exception e) {
            log.error("Update cmdb code error. Cause: {}", e.getMessage(), e);
            returnMap.put("flag", "error");
            returnMap.put("msg", "修改码表失败:" + e.getMessage());
        }
        return returnMap;
    }

    @Override
    public Map<String, String> delete(@RequestBody CmdbCode cmdbCode) {
        Map<String, String> returnMap = new HashMap<>();
        try {
            log.info("Request CmdbCodeController.delete  data -> {}", JSONObject.toJSON(cmdbCode));
            returnMap = cmdbCodeService.delete(cmdbCode);
        } catch (Exception e) {
            log.error("Delete cmdb code error. Cause: {}", e.getMessage(), e);
            returnMap.put("flag", "error");
            returnMap.put("msg", "删除码表失败:" + e.getMessage());
        }
        return returnMap;
    }

    @Override
    public List<CmdbCodeGroup> queryCodeListFormatGroup(@RequestParam(value = "catalogId", required = false) String catalogId) {
        return cmdbCodeService.queryCodeListFormatGroup(catalogId);
    }

    @Override
    public List<CmdbCode> getCodeListByModuleId(@PathVariable("moduleId") String moduleId) {
        try {
            log.info("Request CmdbCodeController.getCodeListByModuleId moduleId -> {}", moduleId);
            return cmdbCodeService.getCodeListByModuleId(moduleId);
        } catch (Exception e) {
            log.error("Update cmdb code error. Cause: {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 根据模型ID查询码表列表
     * @param moduleId 模型ID
     * @return
     */
    @Override
    public Map<String, Object> getSelfCodeListByModuleId(@RequestParam("moduleId") String moduleId) {
        Map<String, Object> resultMap = new HashMap<>();
        Module module = moduleService.getModuleDetail(moduleId);
        List<CmdbCode> codeList = cmdbCodeService.getSelfCodeListByModuleId(moduleId);
        resultMap.put("id", moduleId);
        resultMap.put("name", module.getName());
        resultMap.put("codeList", codeList);
        return resultMap;
    }

    @Override
    public String changeCodeLength(@RequestParam("filedCode") String filedCode,
                                   @RequestParam("changeLength") Integer changeLength) {
        return cmdbCodeService.changeCodeLength(filedCode, changeLength);
    }

    @Override
    public List<Map<String, String>> getDistinctCodeList() {
        return cmdbCodeService.getDistinctCodeList();
    }

    @Override
    public Map<String, Map<String, String>> validCodeValue(@RequestBody Map<String, Object> params) {
        log.info("Request CmdbCodeController.validCodeValue params -> {}", params);
        return cmdbCodeService.validCodeValue(params);
    }

    @Override
    public Map<String, Map<String, Object>> approveCodeValue(@RequestBody Map<String, Object> paramsList) {
        log.info("Request CmdbCodeController.approveCodeValue params -> {}", paramsList);
        return cmdbCodeService.approveCodeValue(paramsList);
    }

    @Override
    public JSONObject validateCmdbCodeUnique(@RequestParam("filedCode") String filedCode,
                                             @RequestParam("moduleCatalogId") String moduleCatalogId) {
        log.info("Request CmdbCodeController.validateCmdbCodeUnique params -> {}", filedCode,moduleCatalogId);
        return cmdbCodeService.validateCmdbCodeUnique(filedCode,moduleCatalogId);
    }

    @Override
    public List<Map<String, Object>> getCodeDataSource(@RequestBody Map<String, Object> params) {
        log.info("Request CmdbCodeController.getCodeDataSource params -> {}", params);
        if (!params.containsKey("codeId")) {
            throw new CmdbRuntimeException("缺少codeId条件.");
        }
        return cmdbCodeService.getRefCodeData(params.get("codeId").toString());
    }

    @Override
    public LinkedList<CmdbCode> getCasParentCodes(@RequestBody List<String> codeIds) {
        log.info("Request CmdbCodeController.getCasParentCodes params -> {}", codeIds);
        return cmdbCodeService.getCasParentCodes(codeIds);
    }

    @Override
    public CmdbSimpleCode getByEntity(@RequestBody CmdbCode entity) {
        log.info("Request CmdbCodeController.getCasParentCodes params -> {}", entity);
        return cmdbCodeService.getByEntity(entity);
    }
}
