package com.aspire.ums.cmdb.view.service;

import com.aspire.ums.cmdb.view.payload.CmdbViewNode;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICmdbViewNodeService
 * Author:   hangfang
 * Date:     2020/5/19
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface ICmdbViewNodeService {

    /**
     * 新增自定义视图节点
     */
    void save(CmdbViewNode viewNode);
    /**
     * 根据viewId删除自定义视图节点
     */
    void deleteByViewId(String viewId);
    /**
     * 根据id删除自定义视图节点
     */
    void deleteById(String id);

    /**
     * 根据id获取自定义视图节点
     */
    CmdbViewNode getOne(CmdbViewNode node);

}
