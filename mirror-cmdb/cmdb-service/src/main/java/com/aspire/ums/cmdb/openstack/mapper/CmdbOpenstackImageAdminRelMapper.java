package com.aspire.ums.cmdb.openstack.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aspire.ums.cmdb.openstack.payload.entity.CmdbOpenstackImageAdminRel;

/**
 * 描述：
 * 
 * @author
 * @date 2020-11-13 17:00:56
 */
@Mapper
public interface CmdbOpenstackImageAdminRelMapper {

    /**
     * 获取所有实例
     * 
     * @return 返回所有实例数据
     */
    List<CmdbOpenstackImageAdminRel> list();

    /**
     * 获取所有实例
     * 
     * @param entity
     *            实例信息
     * @return 返回所有实例数据
     */
    List<CmdbOpenstackImageAdminRel> listByEntity(CmdbOpenstackImageAdminRel entity);

    /**
     * 根据主键ID 获取数据信息
     * 
     * @param entity
     *            实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbOpenstackImageAdminRel get(CmdbOpenstackImageAdminRel entity);

    /**
     * 新增实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void insert(CmdbOpenstackImageAdminRel entity);

    /**
     * 修改实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void update(CmdbOpenstackImageAdminRel entity);

    /**
     * 删除实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void delete(CmdbOpenstackImageAdminRel entity);

    void deleteByInstanceId(CmdbOpenstackImageAdminRel entity);

}
