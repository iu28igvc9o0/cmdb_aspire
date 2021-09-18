package com.aspire.ums.cmdb.v3.dictMapper.mapper;

import com.aspire.ums.cmdb.v3.dictMapper.payload.CmdbDictMapperEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbDictMapper
 * Author:   zhu.juwang
 * Date:     2019/10/15 11:01
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Mapper
public interface CmdbDictMapper {
    /**
     * 根据实体类查询列表信息
     * @param entity
     * @return
     */
    List<CmdbDictMapperEntity> listByEntity(CmdbDictMapperEntity entity);
}
