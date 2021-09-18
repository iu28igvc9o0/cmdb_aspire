package com.aspire.ums.cmdb.demand.service;
import java.util.List;
import com.aspire.ums.cmdb.demand.entity.CmdbDemandResourceType;

/**
* 描述：
* @author
* @date 2019-05-09 16:28:20
*/
public interface ICmdbDemandResourceTypeService {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbDemandResourceType> list();

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbDemandResourceType get(CmdbDemandResourceType entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbDemandResourceType entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbDemandResourceType entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbDemandResourceType entity);
}