package com.aspire.ums.cmdb.v2.restful.service;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.restful.payload.StatisticRequestEntity;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: ICommonRestfulService
 * Author:   zhu.juwang
 * Date:     2020/3/13 11:21
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface ICommonRestfulService {
    /**
     * 获取主机列表
     * @param params
     * @return
     */
    Result<Map<String, Object>> getInstanceList(Map<String, Object> params);

    /**
     * 获取主机详情
     * @param params
     * @return
     */
    Map<String, Object> getInstanceDetail(Map<String, Object> params);

    /**
     * 统计资源设备
     * @param statisticRequestEntity 查询参数
     */
    Object getInstanceStatistics(StatisticRequestEntity statisticRequestEntity);

    /**
     * 使用中文名称作为查询条件获取主机列表
     * @param params
     * @return
     */
    Result<Map<String, Object>> getInstanceListByName(Map<String, Object> params);
}
