package com.aspire.ums.cmdb.v2.collectUnknown.service;

import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.collectUnknown.payload.CmdbCollectUnknown;
import com.aspire.ums.cmdb.collectUnknown.payload.CmdbCollectUnknownQuery;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbCollectUnknownService
 * Author:   hangfang
 * Date:     2019/10/10
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface CmdbCollectUnknownService {
    /**
     * 根据条件筛选未知设备
     */
    Result<CmdbCollectUnknown> list(CmdbCollectUnknownQuery collectUnknownQuery);

    /**
     * 新增未知设备
     */
    void insert(CmdbCollectUnknown collectUnknown);

    /**
     * 批量新增未知设备
     */
    List<Map<String, Object>> insertByBatch(List<CmdbCollectUnknown> collectUnknowns);

    /**
     * 更新未知设备
     */
    void update(CmdbCollectUnknown collectUnknown);

    /**
     * 维护未知设备
     */
    void maintain(CmdbCollectUnknown collectUnknown);

}
