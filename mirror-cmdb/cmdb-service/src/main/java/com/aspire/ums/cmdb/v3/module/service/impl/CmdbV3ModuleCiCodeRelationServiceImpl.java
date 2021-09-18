package com.aspire.ums.cmdb.v3.module.service.impl;

import com.aspire.ums.cmdb.v3.module.mapper.CmdbV3ModuleCiCodeRelationMapper;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCiCodeRelation;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleCiCodeRelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbV3ModuleCiCodeRelationServiceImpl
 * Author:   hangfang
 * Date:     2020/4/26
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
@Slf4j
public class CmdbV3ModuleCiCodeRelationServiceImpl implements ICmdbV3ModuleCiCodeRelationService {

    @Autowired
    private CmdbV3ModuleCiCodeRelationMapper codeRelationMapper;

    @Override
    public void save(List<CmdbV3ModuleCiCodeRelation> ciCodeRelations) {
        codeRelationMapper.save(ciCodeRelations);
    }

    @Override
    public List<CmdbV3ModuleCiCodeRelation> listByRelationId(String relationId) {
        return codeRelationMapper.listByRelationId(relationId);
    }

    @Override
    public void deleteByRelationId(String relationId) {
        codeRelationMapper.deleteByRelationId(relationId);
    }

    @Override
    public void deleteByModuleId(String moduleId) {
        codeRelationMapper.deleteByModuleId(moduleId);
    }
}
