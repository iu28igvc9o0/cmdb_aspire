package com.aspire.ums.cmdb.openstack.service;

import java.util.List;

import com.aspire.ums.cmdb.openstack.payload.entity.CmdbOpenstackImageServerRel;

/**
 * 描述：
 * 
 * @author
 * @date 2020-11-13 17:00:56
 */
public interface ICmdbOpenstackImageServerRelService {

    /**
     * 获取所有实例
     * 
     * @return 返回所有实例数据
     */
    List<CmdbOpenstackImageServerRel> list();

    /**
     * 根据主键ID 获取数据信息
     * 
     * @param entity
     *            实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbOpenstackImageServerRel get(CmdbOpenstackImageServerRel entity);

    /**
     * 新增实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void insert(CmdbOpenstackImageServerRel entity);

    /**
     * 修改实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void update(CmdbOpenstackImageServerRel entity);

    /**
     * 删除实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void delete(CmdbOpenstackImageServerRel entity);

    void deleteByInstanceId(CmdbOpenstackImageServerRel entity);
}
