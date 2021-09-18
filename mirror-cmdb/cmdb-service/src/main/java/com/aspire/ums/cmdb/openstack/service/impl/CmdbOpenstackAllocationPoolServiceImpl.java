package com.aspire.ums.cmdb.openstack.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.ums.cmdb.openstack.mapper.CmdbOpenstackAllocationPoolMapper;
import com.aspire.ums.cmdb.openstack.payload.entity.CmdbOpenstackAllocationPool;
import com.aspire.ums.cmdb.openstack.service.ICmdbOpenstackAllocationPoolService;

/**
 * 描述：
 * 
 * @author
 * @date 2020-11-13 17:00:54
 */
@Service
public class CmdbOpenstackAllocationPoolServiceImpl implements ICmdbOpenstackAllocationPoolService {

    @Autowired
    private CmdbOpenstackAllocationPoolMapper mapper;

    /**
     * 获取所有实例
     * 
     * @return 返回所有实例数据
     */
    @Override
    public List<CmdbOpenstackAllocationPool> list() {
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
    public CmdbOpenstackAllocationPool get(CmdbOpenstackAllocationPool entity) {
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
    public void insert(CmdbOpenstackAllocationPool entity) {
        mapper.insert(entity);
    }

    @Override
    public void batchInsert(List<CmdbOpenstackAllocationPool> entityList) {
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
    public void update(CmdbOpenstackAllocationPool entity) {
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
    public void delete(CmdbOpenstackAllocationPool entity) {
        mapper.delete(entity);
    }

    @Override
    public void deleteBySubnetId(CmdbOpenstackAllocationPool entity) {
        mapper.deleteBySubnetId(entity);
    }
}
