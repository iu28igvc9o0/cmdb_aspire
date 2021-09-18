package com.aspire.cmdb.agent.collect.service;

import com.aspire.cmdb.agent.collect.entity.CmdbSyncSuyanRelation;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbSyncSuyanRelationService
 * Author:   hangfang
 * Date:     2020/9/14
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface CmdbSyncSuyanRelationService {

    List<CmdbSyncSuyanRelation> list(String suyanPodId);

    void insert(CmdbSyncSuyanRelation relation);

    void clear();

    void insertByBatch(List<CmdbSyncSuyanRelation> relations);

}
