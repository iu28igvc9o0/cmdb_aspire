package com.aspire.ums.cmdb.v3.dictMapper.service.impl;

import com.aspire.ums.cmdb.v3.dictMapper.mapper.CmdbSyncFiledMapper;
import com.aspire.ums.cmdb.v3.dictMapper.payload.CmdbSyncFiledMapperEntity;
import com.aspire.ums.cmdb.v3.dictMapper.service.ICmdbSyncFiledMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbSyncFiledMapperServiceImpl
 * Author:   hangfang
 * Date:     2020/9/9
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
public class CmdbSyncFiledMapperServiceImpl implements ICmdbSyncFiledMapperService {

    @Autowired
    private CmdbSyncFiledMapper syncFiledMapper;

    @Override
    public List<CmdbSyncFiledMapperEntity> listByEntity(CmdbSyncFiledMapperEntity entity) {
        return syncFiledMapper.listByEntity(entity);
    }
}
