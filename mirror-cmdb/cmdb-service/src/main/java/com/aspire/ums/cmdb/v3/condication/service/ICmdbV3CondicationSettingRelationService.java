package com.aspire.ums.cmdb.v3.condication.service;
import java.util.List;
import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3CondicationSettingRelation;

/**
* 描述：
* @author
* @date 2020-01-09 14:33:20
*/
public interface ICmdbV3CondicationSettingRelationService {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbV3CondicationSettingRelation> list();

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbV3CondicationSettingRelation get(CmdbV3CondicationSettingRelation entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbV3CondicationSettingRelation entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbV3CondicationSettingRelation entity);

    /**
     * 删除实例
     * @param relationId 关系ID
     * @return
     */
    void deleteById(String relationId);

    /**
     * 删除实例
     * @param condicationSettingId 查询条件ID
     * @return
     */
    void deleteByCondicationSettingId(String condicationSettingId);
}