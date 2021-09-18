package com.aspire.cmdb.agent.collect.mapper;

import com.aspire.cmdb.agent.collect.entity.CmdbSyncSuyanRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbSyncSuyanRelationMapper
 * Author:   hangfang
 * Date:     2020/9/14
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */

@Mapper
public interface CmdbSyncSuyanRelationMapper {

    List<CmdbSyncSuyanRelation> list(@Param("suyanPodId") String suyanPodId);

    void insert(CmdbSyncSuyanRelation relation);

    void insertByBatch(List<CmdbSyncSuyanRelation> relation);

    void clear();

}
