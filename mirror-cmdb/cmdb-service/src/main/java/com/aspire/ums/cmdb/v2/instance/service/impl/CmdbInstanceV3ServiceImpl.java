package com.aspire.ums.cmdb.v2.instance.service.impl;

import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.helper.JDBCHelper;
import com.aspire.ums.cmdb.sqlManage.CmdbSqlManage;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceV3Service;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v3.condication.service.ICmdbV3CondicationSettingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: CmdbInstanceV3ServiceImpl
 * Author:   zhu.juwang
 * Date:     2020/6/30 9:09
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
@Slf4j
public class CmdbInstanceV3ServiceImpl implements ICmdbInstanceV3Service {

    @Value("${cmdb.access.inner}")
    private String innerUserId;
    @Value("${cmdb.query.db:mysql}")
    private String queryDbType;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private ICmdbV3CondicationSettingService condctService;
    @Autowired
    private JDBCHelper jdbcHelper;

    /**
     * 使用中文名称作为查询条件获取主机列表
     * @param params
     * @return
     */
    public Result<Map<String, Object>> getInstanceListByName(Map<String, Object> params) {
        return null;
    }

    @Override
    public Map<String, Object> tempQueryInstanceDetail(Map<String, Object> refParams) {
        if (!refParams.containsKey("token")) {
            refParams.put("token", innerUserId);
        }
        if (!refParams.containsKey("condicationCode")) {
            refParams.put("condicationCode", "instance_detail");
        }
        Map<String, Object> queryParams = condctService.parseQuery(refParams);

        List<Map<String, Object>> paramsList = (List<Map<String, Object>>) queryParams.get("params");
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> statementParams = new LinkedMap();
        for (Map<String, Object> params : paramsList) {
            this.assessKey(params, "filed", "Format params error. querySetting.params every member must has filed、operator、value properties.");
            this.assessKey(params, "operator", "Format params error. querySetting.params every member must has filed、operator、value properties.");
            this.assessKey(params, "value", "Format params error. querySetting.params every member must has filed、operator、value properties.");
            String operator = params.get("operator").toString().trim();
            String filed = params.get("filed").toString().trim();
            Object value = params.get("value");
            if (StringUtils.isNotEmpty(value)) {
                switch (operator.toLowerCase(Locale.ENGLISH)) {
                    case "like":
                        queryBuilder.append(jdbcHelper.likeSql(filed, true));
                        statementParams.put(filed, value);
                        break;
                    case "<":
                    case "<=":
                        queryBuilder.append(jdbcHelper.lteSql(filed, true));
                        statementParams.put(filed, value);
                        break;
                    case ">":
                    case ">=":
                        queryBuilder.append(jdbcHelper.gteSql(filed, true));
                        statementParams.put(filed, value);
                        break;
                    case "in":
                        queryBuilder.append(jdbcHelper.inSql(filed, true));
                        statementParams.put(filed, value);
                        break;
                    case "=":
                        queryBuilder.append(jdbcHelper.eqSql(filed, true));
                        statementParams.put(filed, value);
                        break;
                    case "not in":
                        queryBuilder.append(jdbcHelper.notInSql(filed, true));
                        statementParams.put(filed, value);
                        break;
                    case "between":
                        List<String> valueList = (List<String>) value;
                        queryBuilder.append(jdbcHelper.betweenSql(filed, true));
                        statementParams.put(filed,value);
                        statementParams.put(filed+"_start", valueList.get(0));
                        statementParams.put(filed+"_end", valueList.get(1));
                        break;
                    default:
                        throw new RuntimeException("Don't support operator type [" + operator + "]");
                }
            }
        }
        // 处理排序
        StringBuilder sortBuilderSQL = new StringBuilder();
        if (queryParams.containsKey("sort")) {
            List<Map<String, Object>> sortList = (List<Map<String, Object>>) queryParams.get("sort");
            for (Map<String, Object> sortMap : sortList) {
                if (sortMap.get("filed") != null) {
                    String sortFiled = sortMap.get("filed").toString();
                    if (sortMap.get("type") != null) {
                        if (!("").equals(sortBuilderSQL.toString())) {
                            sortBuilderSQL.append(",");
                        }
                        String sortType = sortMap.get("type").toString();
                        sortBuilderSQL.append(" IFNULL(").append(sortFiled).append(",'') = '' , INET_ATON(").append(sortFiled).append(") ").append(sortType).append(" ");
                    }
                }
            }
        }
        String moduleId = queryParams.get("query_module_id").toString();
        String querySQL = "select * from (" + moduleService.getTempModuleQuerySQL(moduleId)+ ") res where 1=1 ";
        // 增加查询条件
        if (!("").equals(queryBuilder.toString())) {
            querySQL += queryBuilder.toString();
        }
        CmdbSqlManage sqlManage = new CmdbSqlManage(querySQL, moduleId, Constants.INSTANCE_AUTH_MODULE, Constants.NEED_AUTH);
        Map<String, Object> mapResult = jdbcHelper.getQueryMap(sqlManage, null, statementParams);
        return mapResult;
    }

    @Override
    public Result<Map<String, Object>> tempQueryInstanceList(Map<String, Object> params) {
        if (!params.containsKey("token")) {
            params.put("token", innerUserId);
        }
        if (!params.containsKey("condicationCode")) {
            params.put("condicationCode", "instance_list");
        }
        Map<String, Object> queryParams = condctService.parseQuery(params);
        return getInstanceList(queryParams);
    }

    private Result<Map<String, Object>> getInstanceList(Map<String, Object> queryParams) {
        List<Map<String, Object>> paramsList = (List<Map<String, Object>>) queryParams.get("params");
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> statementParams = new LinkedMap();
        for (Map<String, Object> params : paramsList) {
            this.assessKey(params, "filed", "Format params error. querySetting.params every member must has filed、operator、value properties.");
            this.assessKey(params, "operator", "Format params error. querySetting.params every member must has filed、operator、value properties.");
            this.assessKey(params, "value", "Format params error. querySetting.params every member must has filed、operator、value properties.");
            String operator = params.get("operator").toString().trim();
            String filed = params.get("filed").toString().trim();
            Object value = params.get("value");
            if (StringUtils.isNotEmpty(value)) {
                switch (operator.toLowerCase(Locale.ENGLISH)) {
                    case "like":
                        queryBuilder.append(jdbcHelper.likeSql(filed, true));
                        statementParams.put(filed, value);
                        break;
                    case "<":
                    case "<=":
                        queryBuilder.append(jdbcHelper.lteSql(filed, true));
                        statementParams.put(filed, value);
                        break;
                    case ">":
                    case ">=":
                        queryBuilder.append(jdbcHelper.gteSql(filed, true));
                        statementParams.put(filed, value);
                        break;
                    case "in":
                        queryBuilder.append(jdbcHelper.inSql(filed, true));
                        statementParams.put(filed, value);
                        break;
                    case "=":
                        queryBuilder.append(jdbcHelper.eqSql(filed, true));
                        statementParams.put(filed, value);
                        break;
                    case "not in":
                        queryBuilder.append(jdbcHelper.notInSql(filed, true));
                        statementParams.put(filed, value);
                        break;
                    case "between":
                        List<String> valueList = (List<String>) value;
                        queryBuilder.append(jdbcHelper.betweenSql(filed, true));
                        statementParams.put(filed,value);
                        statementParams.put(filed+"_start", valueList.get(0));
                        statementParams.put(filed+"_end", valueList.get(1));
                        break;
                    default:
                        throw new RuntimeException("Don't support operator type [" + operator + "]");
                }
            }
        }
        // 处理排序
        StringBuilder sortBuilderSQL = new StringBuilder();
        if (queryParams.containsKey("sort")) {
            List<Map<String, Object>> sortList = (List<Map<String, Object>>) queryParams.get("sort");
            for (Map<String, Object> sortMap : sortList) {
                if (sortMap.get("filed") != null) {
                    String sortFiled = sortMap.get("filed").toString();
                    if (sortMap.get("type") != null) {
                        if (!("").equals(sortBuilderSQL.toString())) {
                            sortBuilderSQL.append(",");
                        }
                        String sortType = sortMap.get("type").toString();
                        sortBuilderSQL.append(" IFNULL(").append(sortFiled).append(",'') = '' , INET_ATON(").append(sortFiled).append(") ").append(sortType).append(" ");
                    }
                }
            }
        }
        Integer currentPage = null, pageSize = null;
        if (queryParams.containsKey("currentPage") && queryParams.get("currentPage") != null) {
            currentPage = (Integer) queryParams.get("currentPage");
        }
        if (queryParams.containsKey("pageSize") && queryParams.get("pageSize") != null) {
            pageSize = (Integer) queryParams.get("pageSize");
        }

        String moduleId = queryParams.get("query_module_id").toString();
        String querySQL = "select * from zn_cmdb_instance res where 1=1 ";
        // 增加查询条件
        if (!("").equals(queryBuilder.toString())) {
            querySQL += queryBuilder.toString();
        }
        // 增加排序
        if (!("").equals(sortBuilderSQL.toString())) {
            querySQL += " order by " + sortBuilderSQL.toString();
        }
        String queryList = querySQL;
        if (currentPage != null && pageSize != null) {
            queryList += " limit " + ((currentPage - 1) * pageSize) + ", " + pageSize;
        }
        CmdbSqlManage listManage = new CmdbSqlManage(queryList, moduleId, Constants.INSTANCE_AUTH_MODULE, Constants.NEED_AUTH);
        List<Map<String, Object>> list = jdbcHelper.getQueryList(listManage, null, null, null, statementParams);
        CmdbSqlManage countManage = new CmdbSqlManage(querySQL, moduleId, Constants.INSTANCE_AUTH_MODULE, Constants.NEED_AUTH);
        Integer totalCount = jdbcHelper.getInt(countManage, null, statementParams);
        Result<Map<String, Object>> resultPage = new Result<>(totalCount, list);
        resultPage.setColumns(moduleService.getModuleColumns(moduleId));
        return resultPage;
    }

    private void assessKey(Map<String, Object> map, String key, String msg) {
        if (!map.containsKey(key)) {
            throw new RuntimeException(msg);
        }
    }

}
