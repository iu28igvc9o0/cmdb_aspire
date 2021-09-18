package com.aspire.ums.cmdb.v3.condication.service;

import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3CondicationReturnRelation;

import java.util.List;

/**
* 描述：
* @author
* @date 2020-03-11 15:11:41
*/
public interface ICmdbV3CondicationReturnRelationService {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbV3CondicationReturnRelation> list();

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbV3CondicationReturnRelation get(CmdbV3CondicationReturnRelation entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbV3CondicationReturnRelation entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbV3CondicationReturnRelation entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbV3CondicationReturnRelation entity);
}