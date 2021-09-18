package com.aspire.ums.cmdb.openstack.service;

import java.util.List;
import java.util.Map;

import com.aspire.ums.cmdb.openstack.payload.entity.CmdbOpenstackAdmin;

/**
 * 描述：
 * 
 * @author
 * @date 2020-11-13 17:00:53
 */
public interface ICmdbOpenstackAdminService {

    /**
     * 获取所有实例
     * 
     * @return 返回所有实例数据
     */
    List<CmdbOpenstackAdmin> list();

    /**
     * 根据主键ID 获取数据信息
     * 
     * @param entity
     *            实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbOpenstackAdmin get(CmdbOpenstackAdmin entity);

    CmdbOpenstackAdmin findByInstanceId(String instanceId);

    /**
     * 新增实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void insert(CmdbOpenstackAdmin entity);

    void batchInsert(List<CmdbOpenstackAdmin> entityList);

    /**
     * 修改实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void update(CmdbOpenstackAdmin entity);

    void updateByInstanceId(Map<String, Object> params);

    /**
     * 删除实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void delete(CmdbOpenstackAdmin entity);
}
