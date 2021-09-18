package com.aspire.ums.cmdb.view.mapper;

import com.aspire.ums.cmdb.view.payload.CmdbView;
import com.aspire.ums.cmdb.view.payload.CmdbViewNode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbViewNodeMapper
 * Author:   hangfang
 * Date:     2020/5/19
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Mapper
public interface CmdbViewNodeMapper {

    /**
     * 新增视图节点
     */
    void save(CmdbViewNode view);

    /**
     * 删除视图节点
     */
    void deleteById(String id);
    /**
     * 根据视图id删除视图节点
     */
    void deleteByViewId(String viewId);

    /**
     * 获取视图节点列表
     */
    List<CmdbViewNode> list(String id);

    /**
     * 根据id获取视图节点
     */
    CmdbViewNode getOne(CmdbViewNode node);
}
