package com.aspire.ums.cmdb.view.mapper;

import com.aspire.ums.cmdb.view.payload.CmdbViewNode;
import com.aspire.ums.cmdb.view.payload.CmdbViewNodeShow;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbViewNodeShowMapper
 * Author:   hangfang
 * Date:     2020/5/19
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Mapper
public interface CmdbViewNodeShowMapper {

    /**
     * 新增视图节点显示信息
     */
    void save(CmdbViewNodeShow view);
    /**
     * 批量新增视图节点显示信息
     */
    void saveByBatch(List<CmdbViewNodeShow> nodeShowList);

    /**
     * 根据视图id删除视图节点显示信息
     */
    void deleteByViewId(String viewId);
    /**
     * 根据节点id删除视图节点显示信息
     */
    void deleteByNodeId(String nodeId);

    /**
     * 获取视图节点显示信息列表
     */
    List<CmdbViewNodeShow> list(String nodeId);
}
