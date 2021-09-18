package com.aspire.ums.cmdb.serverProject.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.ums.cmdb.serverProject.mapper.CmdbNetworkLineMapper;
import com.aspire.ums.cmdb.serverProject.payload.CmdbNetworkLine;
import com.aspire.ums.cmdb.serverProject.service.ICmdbNetworkLineService;

/**
 * 描述：
 * 
 * @author
 * @date 2021-01-27 17:26:15
 */
@Service
public class CmdbNetworkLineServiceImpl implements ICmdbNetworkLineService {

    @Autowired
    private CmdbNetworkLineMapper mapper;

    /**
     * 获取所有实例
     * 
     * @return 返回所有实例数据
     */
    @Override
    public List<CmdbNetworkLine> list() {
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
    public CmdbNetworkLine get(CmdbNetworkLine entity) {
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
    public void insert(CmdbNetworkLine entity) {
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
    public void update(CmdbNetworkLine entity) {
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
    public void delete(CmdbNetworkLine entity) {
        mapper.delete(entity);
    }
}
