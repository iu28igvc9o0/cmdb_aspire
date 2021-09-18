package com.aspire.ums.cmdb.v2.instance.service;

import com.aspire.ums.cmdb.common.Result;

import java.util.Map;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: ICmdbInstanceV3Service
 * Author:   zhu.juwang
 * Date:     2020/6/29 9:58
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface ICmdbInstanceV3Service {

    /**
     * 使用中文名称作为查询条件获取主机列表
     * @param params
     * @return
     */
    Result<Map<String, Object>> getInstanceListByName(Map<String, Object> params);

    Map<String, Object> tempQueryInstanceDetail(Map<String, Object> params);

    Result<Map<String, Object>> tempQueryInstanceList(Map<String, Object> params);
}
