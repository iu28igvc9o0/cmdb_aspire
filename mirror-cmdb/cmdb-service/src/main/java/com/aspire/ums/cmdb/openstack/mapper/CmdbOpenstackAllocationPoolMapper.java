package com.aspire.ums.cmdb.openstack.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aspire.ums.cmdb.openstack.payload.entity.CmdbOpenstackAllocationPool;

/**
 * 描述：
 * 
 * @author
 * @date 2020-11-13 17:00:54
 */
@Mapper
public interface CmdbOpenstackAllocationPoolMapper {

    /**
     * 获取所有实例
     * 
     * @return 返回所有实例数据
     */
    List<CmdbOpenstackAllocationPool> list();

    /**
     * 获取所有实例
     * 
     * @param entity
     *            实例信息
     * @return 返回所有实例数据
     */
    List<CmdbOpenstackAllocationPool> listByEntity(CmdbOpenstackAllocationPool entity);

    /**
     * 根据主键ID 获取数据信息
     * 
     * @param entity
     *            实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbOpenstackAllocationPool get(CmdbOpenstackAllocationPool entity);

    /**
     * 新增实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void insert(CmdbOpenstackAllocationPool entity);

    void batchInsert(List<CmdbOpenstackAllocationPool> entityList);

    /**
     * 修改实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void update(CmdbOpenstackAllocationPool entity);

    /**
     * 删除实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void delete(CmdbOpenstackAllocationPool entity);

    void deleteBySubnetId(CmdbOpenstackAllocationPool entity);
}
