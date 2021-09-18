package com.aspire.ums.cmdb.view.service;

import com.aspire.ums.cmdb.view.payload.CmdbViewNode;
import com.aspire.ums.cmdb.view.payload.CmdbViewNodeShow;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICmdbViewNodeShowService
 * Author:   hangfang
 * Date:     2020/5/19
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface ICmdbViewNodeShowService {

    /**
     * 新增自定义视图节点展示信息
     */
    void saveByBatch(List<CmdbViewNodeShow> nodeShowList);

    /**
     * 根据节点id展示信息
     */
    void deleteByNodeId(String nodeId);

    /**
     * 根据视图id展示信息
     */
    void deleteByViewId(String viewId);
}
