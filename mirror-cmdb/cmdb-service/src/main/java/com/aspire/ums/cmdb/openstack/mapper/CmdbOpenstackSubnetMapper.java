package com.aspire.ums.cmdb.openstack.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.aspire.ums.cmdb.openstack.payload.entity.CmdbOpenstackSubnet;

/**
 * 描述：
 * 
 * @author
 * @date 2020-11-13 17:00:59
 */
@Mapper
public interface CmdbOpenstackSubnetMapper {

    /**
     * 获取所有实例
     * 
     * @return 返回所有实例数据
     */
    List<CmdbOpenstackSubnet> list();

    /**
     * 获取所有实例
     * 
     * @param entity
     *            实例信息
     * @return 返回所有实例数据
     */
    List<CmdbOpenstackSubnet> listByEntity(CmdbOpenstackSubnet entity);

    /**
     * 根据主键ID 获取数据信息
     * 
     * @param entity
     *            实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbOpenstackSubnet get(CmdbOpenstackSubnet entity);

    CmdbOpenstackSubnet getByInstanceId(CmdbOpenstackSubnet entity);

    CmdbOpenstackSubnet findByInstanceId(String instanceId);

    List<CmdbOpenstackSubnet> findByInstanceIdList(List<String> instanceIdList);

    /**
     * 新增实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void insert(CmdbOpenstackSubnet entity);

    void batchInsert(List<CmdbOpenstackSubnet> entityList);

    /**
     * 修改实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void update(CmdbOpenstackSubnet entity);

    void updateByInstanceId(Map<String, Object> params);

    /**
     * 删除实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void delete(CmdbOpenstackSubnet entity);

    void deleteByInstanceId(String instanceId);

    /**
     * 批量删除
     *
     * @param instanceIdList
     *            instanceId列表
     */
    void deleteByInstanceIdList(List<String> instanceIdList);

    List<String> getAllInstanceId();
}
