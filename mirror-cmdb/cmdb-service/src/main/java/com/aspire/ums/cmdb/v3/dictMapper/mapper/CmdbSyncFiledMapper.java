package com.aspire.ums.cmdb.v3.dictMapper.mapper;

import com.aspire.ums.cmdb.v3.dictMapper.payload.CmdbDictMapperEntity;
import com.aspire.ums.cmdb.v3.dictMapper.payload.CmdbSyncFiledMapperEntity;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbSyncFiledMapper
 * Author:   hangfang
 * Date:     2020/9/9
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface CmdbSyncFiledMapper {

    /**
     * 根据实体类查询列表信息
     * @param entity
     * @return
     */
    List<CmdbSyncFiledMapperEntity> listByEntity(CmdbSyncFiledMapperEntity entity);
}
