package com.aspire.ums.cmdb.v3.module.service;
import java.util.List;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleRefRelation;

/**
* 描述：
* @author
* @date 2020-01-09 14:33:21
*/
public interface ICmdbV3ModuleRefRelationService {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbV3ModuleRefRelation> list();

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbV3ModuleRefRelation get(CmdbV3ModuleRefRelation entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbV3ModuleRefRelation entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbV3ModuleRefRelation entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbV3ModuleRefRelation entity);
}