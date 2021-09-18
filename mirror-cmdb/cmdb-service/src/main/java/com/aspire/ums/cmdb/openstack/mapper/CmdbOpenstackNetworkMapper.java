package com.aspire.ums.cmdb.openstack.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.aspire.ums.cmdb.openstack.payload.entity.CmdbOpenstackNetwork;

/**
 * 描述：
 * 
 * @author
 * @date 2020-11-13 17:00:57
 */
@Mapper
public interface CmdbOpenstackNetworkMapper {

    /**
     * 获取所有实例
     * 
     * @return 返回所有实例数据
     */
    List<CmdbOpenstackNetwork> list();

    /**
     * 获取所有实例
     * 
     * @param entity
     *            实例信息
     * @return 返回所有实例数据
     */
    List<CmdbOpenstackNetwork> listByEntity(CmdbOpenstackNetwork entity);

    /**
     * 根据主键ID 获取数据信息
     * 
     * @param entity
     *            实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbOpenstackNetwork get(CmdbOpenstackNetwork entity);

    CmdbOpenstackNetwork getByInstanceId(CmdbOpenstackNetwork entity);

    CmdbOpenstackNetwork findByInstanceId(String instanceId);

    /**
     * 新增实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void insert(CmdbOpenstackNetwork entity);

    void batchInsert(List<CmdbOpenstackNetwork> entityList);

    /**
     * 修改实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void update(CmdbOpenstackNetwork entity);

    void updateByInstanceId(Map<String, Object> params);

    /**
     * 删除实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void delete(CmdbOpenstackNetwork entity);

    void deleteByInstanceId(CmdbOpenstackNetwork entity);
}
