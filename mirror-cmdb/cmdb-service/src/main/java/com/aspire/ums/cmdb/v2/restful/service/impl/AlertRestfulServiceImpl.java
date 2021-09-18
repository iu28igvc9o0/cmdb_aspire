package com.aspire.ums.cmdb.v2.restful.service.impl;

import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.config.RequestAuthContext;
import com.aspire.ums.cmdb.helper.JDBCHelper;
import com.aspire.ums.cmdb.sqlManage.CmdbSqlManage;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.restful.mapper.AlertRestfulMapper;
import com.aspire.ums.cmdb.v2.restful.service.IAlertRestfulService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: AlertRestfulServiceImpl
 * Author:   zhu.juwang
 * Date:     2019/12/10 15:59
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
@Slf4j
public class AlertRestfulServiceImpl implements IAlertRestfulService {

    @Autowired
    private AlertRestfulMapper restfulMapper;
    @Autowired
    private JDBCHelper jdbcHelper;

    @Override
    public List<Map<String, Object>> statisticsDeviceByIdcType(Map<String, Object> params) {
        return restfulMapper.statisticsDeviceByIdcType(params);
    }

    @Override
    public List<Map<String, Object>> statisticsDeviceByDepartment1(Map<String, Object> params) {
        if (params.containsKey("bizSystem") && params.get("bizSystem") != null) {
            params.put("bizSystemList", params.get("bizSystem").toString().split(","));
        }
        if (params.containsKey("department1") && params.get("department1") != null) {
            params.put("department1List", params.get("department1").toString().split(","));
        }
        return restfulMapper.statisticsDeviceByDepartment1(params);
    }

    @Override
    public List<Map<String, Object>> statisticsDeviceByBizSystem(Map<String, Object> params) {
        if (params.containsKey("bizSystem") && params.get("bizSystem") != null) {
            params.put("bizSystemList", params.get("bizSystem").toString().split(","));
        }
        
        Map<String, List<String>> resFilterMap = RequestAuthContext.getRequestHeadUser().getResFilterConfig();
        return restfulMapper.statisticsDeviceByBizSystem(params, resFilterMap);
    }

    @Override
    public List<Map<String, Object>> statisticsDepartmentForAvailability(String department1) {
        if (StringUtils.isEmpty(department1)) {
            return restfulMapper.statisticsDepartment1ForAvailability();
        }
        return restfulMapper.statisticsDepartment2ForAvailability(department1);
    }

    /**
     * 验证设备删除状态
     *
     * @param instanceList 设备列表
     */
    @Override
    public List<Map<String, Object>> checkInstanceDeleteStatus(List<Map<String, String>> instanceList) {
        String selectTemplate = "SELECT #{%s} id, #{%s} ip";
        int idx = 0;
        StringBuilder unionSql = new StringBuilder();
        Map<String, Object> queryParams = Maps.newHashMap();
        for (Map<String, String> instanceMap : instanceList) {
            if (idx > 0) {
                unionSql.append(" union ");
            }
            unionSql.append(String.format(selectTemplate, "id" + idx, "ip" + idx));
            queryParams.put("id" + idx, instanceMap.get("id"));
            queryParams.put("ip" + idx, instanceMap.get("ip"));
            idx ++ ;
        }
        StringBuilder querySql = new StringBuilder();
        querySql.append(" SELECT d.id, d.ip, IFNULL(s.is_delete, '1') is_delete FROM (")
                .append(unionSql)
                .append(" ) d ")
                .append(" LEFT JOIN (")
                .append(" SELECT c.id, m.ip, c.`is_delete` FROM cmdb_instance c")
                .append(" INNER JOIN cmdb_instance_ip_manager m ON c.`id` = m.`instance_id`")
                .append(" ) s ON s.id = d.id AND s.ip = d.ip");
        CmdbSqlManage cmdbSqlManage = new CmdbSqlManage(querySql.toString(), Constants.INSTANCE_AUTH_MODULE,
                Constants.UN_NEED_AUTH);
        return jdbcHelper.getQueryList(cmdbSqlManage, null, null, null, queryParams);
    }
}
