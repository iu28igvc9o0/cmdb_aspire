package com.aspire.ums.cmdb.openstack.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.ums.cmdb.openstack.mapper.CmdbOpenstackSubnetMapper;
import com.aspire.ums.cmdb.openstack.payload.entity.CmdbOpenstackSubnet;
import com.aspire.ums.cmdb.openstack.service.ICmdbOpenstackSubnetService;

/**
 * 描述：
 * 
 * @author
 * @date 2020-11-13 17:00:59
 */
@Service
public class CmdbOpenstackSubnetServiceImpl implements ICmdbOpenstackSubnetService {

    @Autowired
    private CmdbOpenstackSubnetMapper mapper;

    /**
     * 获取所有实例
     * 
     * @return 返回所有实例数据
     */
    @Override
    public List<CmdbOpenstackSubnet> list() {
        return mapper.list();
    }

    /**
     * 根据主键ID 获取数据信息
     * 
     * @param entity
     *            实例信息
     * @return 返回指定ID的数据信息
     */
    @Override
    public CmdbOpenstackSubnet get(CmdbOpenstackSubnet entity) {
        return mapper.get(entity);
    }

    @Override
    public CmdbOpenstackSubnet getByInstanceId(CmdbOpenstackSubnet entity) {
        return mapper.getByInstanceId(entity);
    }

    @Override
    public CmdbOpenstackSubnet findByInstanceId(String instanceId) {
        return mapper.findByInstanceId(instanceId);
    }

    @Override
    public List<CmdbOpenstackSubnet> findByInstanceIdList(List<String> instanceIdList) {
        return mapper.findByInstanceIdList(instanceIdList);
    }

    /**
     * 新增实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    @Override
    public void insert(CmdbOpenstackSubnet entity) {
        mapper.insert(entity);
    }

    @Override
    public void batchInsert(List<CmdbOpenstackSubnet> entityList) {
        mapper.batchInsert(entityList);
    }

    /**
     * 修改实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    @Override
    public void update(CmdbOpenstackSubnet entity) {
        mapper.update(entity);
    }

    @Override
    public void updateByInstanceId(Map<String, Object> params) {
        mapper.updateByInstanceId(params);
    }

    /**
     * 删除实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    @Override
    public void delete(CmdbOpenstackSubnet entity) {
        mapper.delete(entity);
    }

    @Override
    public void deleteByInstanceId(String instanceId) {
        mapper.deleteByInstanceId(instanceId);
    }

    @Override
    public void deleteByInstanceIdList(List<String> instanceIdList) {
        mapper.deleteByInstanceIdList(instanceIdList);
    }

    @Override
    public List<String> getAllInstanceId() {
        return mapper.getAllInstanceId();
    }
}
