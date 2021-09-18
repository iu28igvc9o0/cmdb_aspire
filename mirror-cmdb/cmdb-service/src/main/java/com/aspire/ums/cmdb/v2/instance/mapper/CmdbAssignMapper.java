package com.aspire.ums.cmdb.v2.instance.mapper;

import com.aspire.ums.cmdb.instance.payload.CmdbAssign;
import com.aspire.ums.cmdb.instance.payload.CmdbAssignQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbAssignMapper
 * Author:   hangfang
 * Date:     2019/11/13
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Mapper
public interface CmdbAssignMapper {
    /**
     * 获取资源分配分析列表
     */
    List<CmdbAssign> list(CmdbAssignQuery query);

    /**
     * 获取资源分配分析列表
     */
    Integer listCount(CmdbAssignQuery query);
    /**
     * 保存资源分配分析数据
     */
    void insert(CmdbAssign assign);

    /**
     * 删除资源分配分析数据
     */
    void delete(String id);
}
