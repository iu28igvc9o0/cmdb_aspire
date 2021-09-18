package com.aspire.ums.cmdb.v3.condication.service;

import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3CondicationSortRelation;

import java.util.List;

/**
* 描述：
* @author
* @date 2020-03-16 09:19:57
*/
public interface ICmdbV3CondicationSortRelationService {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbV3CondicationSortRelation> list();

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbV3CondicationSortRelation get(CmdbV3CondicationSortRelation entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbV3CondicationSortRelation entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbV3CondicationSortRelation entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbV3CondicationSortRelation entity);

    /**
     * 根据配置ID查询排序配置列表
     * @param condicationSettingId 配置ID
     * @return
     */
    List<CmdbV3CondicationSortRelation> getSortRelationByCondicationId(String condicationSettingId);
}