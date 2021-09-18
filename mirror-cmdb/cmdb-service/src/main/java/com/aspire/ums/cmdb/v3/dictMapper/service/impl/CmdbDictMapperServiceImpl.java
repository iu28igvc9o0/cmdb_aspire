package com.aspire.ums.cmdb.v3.dictMapper.service.impl;

import com.aspire.ums.cmdb.v3.dictMapper.mapper.CmdbDictMapper;
import com.aspire.ums.cmdb.v3.dictMapper.payload.CmdbDictMapperEntity;
import com.aspire.ums.cmdb.v3.dictMapper.service.ICmdbDictMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbDictMapperServiceImpl
 * Author:   zhu.juwang
 * Date:     2019/10/15 11:12
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
@Primary
public class CmdbDictMapperServiceImpl implements ICmdbDictMapperService {

    @Autowired
    private CmdbDictMapper cmdbDictMapper;

    @Override
    public List<CmdbDictMapperEntity> listByEntity(CmdbDictMapperEntity entity) {
        return cmdbDictMapper.listByEntity(entity);
    }
}
