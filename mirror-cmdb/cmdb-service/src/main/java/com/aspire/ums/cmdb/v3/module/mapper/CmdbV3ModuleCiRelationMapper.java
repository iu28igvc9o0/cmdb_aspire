package com.aspire.ums.cmdb.v3.module.mapper;

import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCiCodeRelation;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCiRelation;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCiRelationDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbV3ModuleCiRelationMapper
 * Author:   hangfang
 * Date:     2020/4/26
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Mapper
public interface CmdbV3ModuleCiRelationMapper {

    /**
     * 根据模型id获取所有该模型的资源关系
     * @param moduleId 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbV3ModuleCiRelation> listByModuleId(@Param("moduleId")String moduleId);

    /**
     * 根据模型id获取所有该模型的资源关系(主要返回给CI详情)
     * @param moduleId 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbV3ModuleCiRelationDetail> listRDetailByModuleId(@Param("moduleId")String moduleId);
    /**
     * 根据模型id获取所有该模型的资源关系
     * @param id 实例信息
     * @return 返回所有实例数据
     */
    CmdbV3ModuleCiRelation getById(@Param("id")String id);
    /**
     * 根据模型id获取所有该模型的资源关系
     * @param id 实例信息
     * @return 返回所有实例数据
     */
    CmdbV3ModuleCiRelationDetail getDetailById(@Param("id") String id);
    /**
     * 判断条件下是否存在
     * @param moduleId 实例信息
     * @return 返回所有实例数据
     */
    CmdbV3ModuleCiRelation getExist(@Param("moduleId") String moduleId,
                                    @Param("codeId") String codeId);

    /**
     * 保存模型关系
     * @param relation 实例信息
     * @return 返回所有实例数据
     */
    void save(CmdbV3ModuleCiRelation relation);

    /**
     * 根据主键删除模型关系
     * @param id 实例信息
     * @return 返回所有实例数据
     */
    void delete(@Param("id") String id);

    /**
     * 根据模型id删除模型关系
     * @param moduleId 实例信息
     * @return 返回所有实例数据
     */
    void deleteByModuleId(@Param("moduleId")String moduleId);
}
