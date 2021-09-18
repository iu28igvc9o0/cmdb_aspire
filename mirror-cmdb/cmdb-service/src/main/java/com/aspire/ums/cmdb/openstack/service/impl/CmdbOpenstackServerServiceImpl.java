package com.aspire.ums.cmdb.openstack.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.ums.cmdb.openstack.mapper.CmdbOpenstackServerMapper;
import com.aspire.ums.cmdb.openstack.payload.entity.CmdbOpenstackServer;
import com.aspire.ums.cmdb.openstack.service.ICmdbOpenstackServerService;

/**
 * 描述：
 * 
 * @author
 * @date 2020-11-13 17:00:58
 */
@Service
public class CmdbOpenstackServerServiceImpl implements ICmdbOpenstackServerService {

    @Autowired
    private CmdbOpenstackServerMapper mapper;

    /**
     * 获取所有实例
     * 
     * @return 返回所有实例数据
     */
    @Override
    public List<CmdbOpenstackServer> list() {
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
    public CmdbOpenstackServer get(CmdbOpenstackServer entity) {
        return mapper.get(entity);
    }

    @Override
    public CmdbOpenstackServer findByInstanceId(String instanceId) {
        return mapper.findByInstanceId(instanceId);
    }

    /**
     * 新增实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    @Override
    public void insert(CmdbOpenstackServer entity) {
        mapper.insert(entity);
    }

    @Override
    public void batchInsert(List<CmdbOpenstackServer> entityList) {
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
    public void update(CmdbOpenstackServer entity) {
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
    public void delete(CmdbOpenstackServer entity) {
        mapper.delete(entity);
    }
}
