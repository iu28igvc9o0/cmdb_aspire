package com.aspire.ums.cmdb.serverProject.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.ums.cmdb.serverProject.mapper.CmdbNetworkLineMgrMapper;
import com.aspire.ums.cmdb.serverProject.payload.CmdbNetworkLineMgr;
import com.aspire.ums.cmdb.serverProject.service.ICmdbNetworkLineMgrService;

/**
 * 描述：
 * 
 * @author
 * @date 2021-01-27 17:26:15
 */
@Service
public class CmdbNetworkLineMgrServiceImpl implements ICmdbNetworkLineMgrService {

    @Autowired
    private CmdbNetworkLineMgrMapper mapper;

    /**
     * 获取所有实例
     * 
     * @return 返回所有实例数据
     */
    @Override
    public List<CmdbNetworkLineMgr> list() {
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
    public CmdbNetworkLineMgr get(CmdbNetworkLineMgr entity) {
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
    public void insert(CmdbNetworkLineMgr entity) {
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
    public void update(CmdbNetworkLineMgr entity) {
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
    public void delete(CmdbNetworkLineMgr entity) {
        mapper.delete(entity);
    }
}
