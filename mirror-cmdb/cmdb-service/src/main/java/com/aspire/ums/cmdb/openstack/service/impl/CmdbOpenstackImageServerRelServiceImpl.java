package com.aspire.ums.cmdb.openstack.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.ums.cmdb.openstack.mapper.CmdbOpenstackImageServerRelMapper;
import com.aspire.ums.cmdb.openstack.payload.entity.CmdbOpenstackImageServerRel;
import com.aspire.ums.cmdb.openstack.service.ICmdbOpenstackImageServerRelService;

/**
 * 描述：
 * 
 * @author
 * @date 2020-11-13 17:00:56
 */
@Service
public class CmdbOpenstackImageServerRelServiceImpl implements ICmdbOpenstackImageServerRelService {

    @Autowired
    private CmdbOpenstackImageServerRelMapper mapper;

    /**
     * 获取所有实例
     * 
     * @return 返回所有实例数据
     */
    @Override
    public List<CmdbOpenstackImageServerRel> list() {
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
    public CmdbOpenstackImageServerRel get(CmdbOpenstackImageServerRel entity) {
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
    public void insert(CmdbOpenstackImageServerRel entity) {
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
    public void update(CmdbOpenstackImageServerRel entity) {
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
    public void delete(CmdbOpenstackImageServerRel entity) {
        mapper.delete(entity);
    }

    @Override
    public void deleteByInstanceId(CmdbOpenstackImageServerRel entity) {
        mapper.deleteByInstanceId(entity);
    }
}
