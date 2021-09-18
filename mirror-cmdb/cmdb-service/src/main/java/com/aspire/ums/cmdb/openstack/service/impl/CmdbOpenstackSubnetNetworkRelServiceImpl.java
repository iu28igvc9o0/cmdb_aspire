package com.aspire.ums.cmdb.openstack.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.ums.cmdb.openstack.mapper.CmdbOpenstackSubnetNetworkRelMapper;
import com.aspire.ums.cmdb.openstack.payload.entity.CmdbOpenstackSubnetNetworkRel;
import com.aspire.ums.cmdb.openstack.service.ICmdbOpenstackSubnetNetworkRelService;

/**
 * 描述：
 * 
 * @author
 * @date 2020-11-13 17:01:00
 */
@Service
public class CmdbOpenstackSubnetNetworkRelServiceImpl implements ICmdbOpenstackSubnetNetworkRelService {

    @Autowired
    private CmdbOpenstackSubnetNetworkRelMapper mapper;

    /**
     * 获取所有实例
     * 
     * @return 返回所有实例数据
     */
    @Override
    public List<CmdbOpenstackSubnetNetworkRel> list() {
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
    public CmdbOpenstackSubnetNetworkRel get(CmdbOpenstackSubnetNetworkRel entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    @Override
    public void insert(CmdbOpenstackSubnetNetworkRel entity) {
        mapper.insert(entity);
    }

    /**
     * 修改实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    @Override
    public void update(CmdbOpenstackSubnetNetworkRel entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    @Override
    public void delete(CmdbOpenstackSubnetNetworkRel entity) {
        mapper.delete(entity);
    }

    @Override
    public void deleteByInstanceId(CmdbOpenstackSubnetNetworkRel entity) {
        mapper.deleteByInstanceId(entity);
    }
}
