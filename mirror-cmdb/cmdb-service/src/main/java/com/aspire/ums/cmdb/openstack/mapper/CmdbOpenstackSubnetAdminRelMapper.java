package com.aspire.ums.cmdb.openstack.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aspire.ums.cmdb.openstack.payload.entity.CmdbOpenstackSubnetAdminRel;

/**
 * 描述：
 * 
 * @author
 * @date 2020-11-13 17:01:00
 */
@Mapper
public interface CmdbOpenstackSubnetAdminRelMapper {

    /**
     * 获取所有实例
     * 
     * @return 返回所有实例数据
     */
    List<CmdbOpenstackSubnetAdminRel> list();

    /**
     * 获取所有实例
     * 
     * @param entity
     *            实例信息
     * @return 返回所有实例数据
     */
    List<CmdbOpenstackSubnetAdminRel> listByEntity(CmdbOpenstackSubnetAdminRel entity);

    /**
     * 根据主键ID 获取数据信息
     * 
     * @param entity
     *            实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbOpenstackSubnetAdminRel get(CmdbOpenstackSubnetAdminRel entity);

    /**
     * 新增实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void insert(CmdbOpenstackSubnetAdminRel entity);

    /**
     * 修改实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void update(CmdbOpenstackSubnetAdminRel entity);

    /**
     * 删除实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void delete(CmdbOpenstackSubnetAdminRel entity);

    void deleteByInstanceId(CmdbOpenstackSubnetAdminRel entity);
}
