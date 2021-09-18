package com.aspire.ums.cmdb.openstack.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.ums.cmdb.openstack.mapper.CmdbOpenstackImageAdminRelMapper;
import com.aspire.ums.cmdb.openstack.payload.entity.CmdbOpenstackImageAdminRel;
import com.aspire.ums.cmdb.openstack.service.ICmdbOpenstackImageAdminRelService;

/**
 * 描述：
 * 
 * @author
 * @date 2020-11-13 17:00:56
 */
@Service
public class CmdbOpenstackImageAdminRelServiceImpl implements ICmdbOpenstackImageAdminRelService {

    @Autowired
    private CmdbOpenstackImageAdminRelMapper mapper;

    /**
     * 获取所有实例
     * 
     * @return 返回所有实例数据
     */
    @Override
    public List<CmdbOpenstackImageAdminRel> list() {
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
    public CmdbOpenstackImageAdminRel get(CmdbOpenstackImageAdminRel entity) {
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
    public void insert(CmdbOpenstackImageAdminRel entity) {
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
    public void update(CmdbOpenstackImageAdminRel entity) {
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
    public void delete(CmdbOpenstackImageAdminRel entity) {
        mapper.delete(entity);
    }

    @Override
    public void deleteByInstanceId(CmdbOpenstackImageAdminRel entity) {
        mapper.deleteByInstanceId(entity);

    }
}
