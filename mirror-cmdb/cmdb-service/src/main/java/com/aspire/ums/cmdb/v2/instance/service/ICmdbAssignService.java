package com.aspire.ums.cmdb.v2.instance.service;

import com.aspire.ums.cmdb.instance.payload.CmdbAssign;
import com.aspire.ums.cmdb.instance.payload.CmdbAssignQuery;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICmdbAssignService
 * Author:   hangfang
 * Date:     2019/11/13
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface ICmdbAssignService {
    /**
     * 获取资源分配分析列表
     */
    List<CmdbAssign> list(CmdbAssignQuery query);

    /**
     * 获取资源分配分析数量
     */
    Integer listCount(CmdbAssignQuery query);

    /**
     * 保存资源分配分析数据
     */
    void save(CmdbAssign assign);

    /**
     * 删除资源分配分析数据
     */
    void delete(String id);
}
