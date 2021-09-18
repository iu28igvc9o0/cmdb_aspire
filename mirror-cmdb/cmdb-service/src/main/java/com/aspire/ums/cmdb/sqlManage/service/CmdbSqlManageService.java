package com.aspire.ums.cmdb.sqlManage.service;

import com.aspire.ums.cmdb.sqlManage.CmdbSqlManage;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbSqlManageService
 * Author:   hangfang
 * Date:     2020/6/17
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface CmdbSqlManageService {

    /**
     * 根据统计接口名称 获取SQL信息
     * @param managerName 接口名称
     * @return
     */
    CmdbSqlManage getByName(String managerName);

    /**
     * 查询数据列表
     * @param statisticName 统计名称
     * @param params 统计参数
     * @param <T> 返回参数类型
     * @return
     */
    <T> List<Map<String, T>> queryList(String statisticName, Map<String, Object> params);

    /**
     * 查询数据列表
     * @param statisticName 统计名称
     * @param params 统计参数
     * @param <T> 返回参数类型
     * @return
     */
    <T> Map<String, T> queryMap(String statisticName, Map<String, Object> params);
}
