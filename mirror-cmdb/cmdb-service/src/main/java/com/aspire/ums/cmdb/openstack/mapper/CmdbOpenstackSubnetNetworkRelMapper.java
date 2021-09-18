package com.aspire.ums.cmdb.openstack.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aspire.ums.cmdb.openstack.payload.entity.CmdbOpenstackSubnetNetworkRel;

/**
 * 描述：
 * 
 * @author
 * @date 2020-11-13 17:01:00
 */
@Mapper
public interface CmdbOpenstackSubnetNetworkRelMapper {

    /**
     * 获取所有实例
     * 
     * @return 返回所有实例数据
     */
    List<CmdbOpenstackSubnetNetworkRel> list();

    /**
     * 获取所有实例
     * 
     * @param entity
     *            实例信息
     * @return 返回所有实例数据
     */
    List<CmdbOpenstackSubnetNetworkRel> listByEntity(CmdbOpenstackSubnetNetworkRel entity);

    /**
     * 根据主键ID 获取数据信息
     * 
     * @param entity
     *            实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbOpenstackSubnetNetworkRel get(CmdbOpenstackSubnetNetworkRel entity);

    /**
     * 新增实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void insert(CmdbOpenstackSubnetNetworkRel entity);

    /**
     * 修改实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void update(CmdbOpenstackSubnetNetworkRel entity);

    /**
     * 删除实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void delete(CmdbOpenstackSubnetNetworkRel entity);

    void deleteByInstanceId(CmdbOpenstackSubnetNetworkRel entity);
}
