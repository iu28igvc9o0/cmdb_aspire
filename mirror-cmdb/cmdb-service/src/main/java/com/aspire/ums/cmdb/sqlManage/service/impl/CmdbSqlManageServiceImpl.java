package com.aspire.ums.cmdb.sqlManage.service.impl;

import com.aspire.ums.cmdb.helper.JDBCHelper;
import com.aspire.ums.cmdb.sqlManage.CmdbSqlManage;
import com.aspire.ums.cmdb.sqlManage.mapper.SqlManageMapper;
import com.aspire.ums.cmdb.sqlManage.service.CmdbSqlManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbSqlManageServiceImpl
 * Author:   hangfang
 * Date:     2020/6/17
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
@Slf4j
public class CmdbSqlManageServiceImpl implements CmdbSqlManageService {

    @Autowired
    private JDBCHelper jdbcHelper;
    @Autowired
    private SqlManageMapper manageMapper;

    @Override
    public CmdbSqlManage getByName(String managerName) {
        CmdbSqlManage sqlManage = manageMapper.getByName(managerName);
        if (sqlManage == null) {
            throw new RuntimeException("Can't find record by name[" + managerName + "]. Query failed.");
        }
        return sqlManage;
    }

    @Override
    public <T> List<Map<String, T>> queryList(String managerName, Map<String, Object> params) {
        return jdbcHelper.getQueryList(this.getByName(managerName), null, null, null, params);
    }

    @Override
    public <T> Map<String, T> queryMap(String managerName, Map<String, Object> params) {
        return jdbcHelper.getQueryMap(this.getByName(managerName), null, params);
    }
}
