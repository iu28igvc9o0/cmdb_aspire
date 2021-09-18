package com.aspire.ums.cmdb.v3.module.mapper;

import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCiCodeRelation;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCiRelation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbV3ModuleCiCodeRelationMapper
 * Author:   hangfang
 * Date:     2020/4/26
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Mapper
public interface CmdbV3ModuleCiCodeRelationMapper {

    /**
     * 根据模型关系id获取所有字段关联信息
     * @param relationId 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbV3ModuleCiCodeRelation> listByRelationId(String relationId);

    /**
     * 保存模型字段关联关系
     * @param codeRelations 实例信息
     * @return 返回所有实例数据
     */
    void save(List<CmdbV3ModuleCiCodeRelation> codeRelations);

    /**
     * 根据关系id删除字段关联关系
     * @param relationId 实例信息
     * @return 返回所有实例数据
     */
    void deleteByRelationId(String relationId);

    /**
     * 根据模型id删除字段关联关系
     * @param moduleId 实例信息
     * @return 返回所有实例数据
     */
    void deleteByModuleId(String moduleId);
}
