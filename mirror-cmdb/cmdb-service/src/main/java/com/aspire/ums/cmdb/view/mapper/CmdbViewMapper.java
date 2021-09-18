package com.aspire.ums.cmdb.view.mapper;

import com.aspire.ums.cmdb.view.payload.CmdbView;
import com.aspire.ums.cmdb.view.payload.CmdbViewQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbViewMapper
 * Author:   hangfang
 * Date:     2020/5/19
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Mapper
public interface CmdbViewMapper {

    /**
     * 新增视图
     */
    void save(CmdbView view);

    /**
     * 删除视图
    */
    void deleteById(@Param("id") String id);

    /**
     * 获取视图列表
     */
    List<CmdbView> list(CmdbViewQuery query);
    /**
     * 获取视图列表
     */
    Integer count(CmdbViewQuery query);

    /**
     * 根据id获取视图
     */
    CmdbView getOne(@Param("id") String id);
    /**
     * 根据id获取视图
     */
    CmdbView getOneByCode(@Param("viewCode") String viewCode);
    /**
     * 根据id获取视图
     */
    CmdbView getSimpleOne(@Param("id") String id);

    /**
     * 根据viewCode获取视图
     */
    CmdbView getSimpleOneByCode(@Param("viewCode") String viewCode);
}
