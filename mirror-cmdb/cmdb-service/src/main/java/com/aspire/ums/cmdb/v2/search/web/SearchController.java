package com.aspire.ums.cmdb.v2.search.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.code.payload.CmdbSimpleCode;
import com.aspire.ums.cmdb.config.RequestAuthContext;
import com.aspire.ums.cmdb.search.ISearchAPI;
import com.aspire.ums.cmdb.search.payload.ColumnFilter;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.code.service.ICmdbCodeService;
import com.aspire.ums.cmdb.v2.search.service.ISearchService;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.aspire.ums.cmdb.v3.config.service.ICmdbConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: SearchController
 * Author:   zhu.juwang
 * Date:     2019/5/21 20:06
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
public class SearchController implements ISearchAPI {
    @Autowired
    private ISearchService searchService;
    @Autowired
    private ICmdbConfigService cmdbConfigService;
    @Autowired
    private ICmdbCodeService codeService;

    @Override
    public JSONObject getOrInsertColumnFilter(@PathVariable("menuType") String menuType,
                                              @PathVariable("moduleId") String moduleId,
                                              @PathVariable("loginName")String loginName) {
        ColumnFilter query = new ColumnFilter();
        query.setMenuType(menuType);
        query.setModuleId(moduleId);
        query.setLoginName(loginName);
        ColumnFilter columnFilter = searchService.getOne(query);
        if (columnFilter == null) {
            CmdbConfig cmdbConfig = cmdbConfigService.getConfigByCode("COLUMN_FILTER:" + moduleId);
            if (cmdbConfig != null) {
                query.setColumnInfo(cmdbConfig.getConfigValue());
            } else {
                List<CmdbSimpleCode> codeList = codeService.getSimpleCodeListByModuleId(moduleId);
                Map<String, Boolean> columnInfo = new LinkedHashMap<>();
                if (codeList != null && codeList.size() > 0) {
                    for (CmdbSimpleCode simpleCode : codeList) {
                        columnInfo.put(simpleCode.getFiledCode(), Boolean.TRUE);
                    }
                }
                query.setColumnInfo(JSONObject.toJSONString(columnInfo));
            }
            query.setId(UUIDUtil.getUUID());
            query.setInsertTime(new Date());
            query.setLoginName(loginName);
            searchService.insert(query);
            columnFilter = query;
        }
        return (JSONObject) JSON.toJSON(columnFilter);
    }

    @Override
    public Map<String, Object> updateColumnFilter(@RequestBody ColumnFilter columnFilter) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        columnFilter.setColumnInfo(JSON.toJSON(columnFilter.getColumnMap()).toString());
        try {
            ColumnFilter res = searchService.getOne(columnFilter);
            if (null==res) {
                columnFilter.setId(UUIDUtil.getUUID());
                columnFilter.setInsertTime(new Date());
                searchService.insert(columnFilter);
            }else {
            searchService.update(columnFilter);
            }
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("msg", e.getMessage());
            return resultMap;
        }
        resultMap.put("success", true);
        return resultMap;
    }
}
