package com.aspire.cmdb.agent.collect.service.impl;

import com.aspire.cmdb.agent.collect.entity.CmdbSyncSuyanRelation;
import com.aspire.cmdb.agent.collect.mapper.CmdbSyncSuyanRelationMapper;
import com.aspire.cmdb.agent.collect.service.CmdbSyncSuyanRelationService;
import com.aspire.ums.cmdb.v2.instance.mapper.CmdbInstancePortRelationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbSyncSuyanRelationServiceImpl
 * Author:   hangfang
 * Date:     2020/9/14
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
public class CmdbSyncSuyanRelationServiceImpl implements CmdbSyncSuyanRelationService {

    @Autowired
    private CmdbSyncSuyanRelationMapper relationMapper;

    @Override
    public List<CmdbSyncSuyanRelation> list(String suyanPodId) {
        return relationMapper.list(suyanPodId);
    }

    @Override
    public void insert(CmdbSyncSuyanRelation relation) {
        relationMapper.insert(relation);
    }

    @Override
    public void clear() {
        relationMapper.clear();
    }

    @Override
    public void insertByBatch(List<CmdbSyncSuyanRelation> relations) {
        relationMapper.insertByBatch(relations);
    }


}
