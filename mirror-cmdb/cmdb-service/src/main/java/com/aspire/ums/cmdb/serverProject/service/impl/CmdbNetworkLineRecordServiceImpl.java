package com.aspire.ums.cmdb.serverProject.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.ums.cmdb.serverProject.mapper.CmdbNetworkLineRecordMapper;
import com.aspire.ums.cmdb.serverProject.payload.CmdbNetworkLineRecord;
import com.aspire.ums.cmdb.serverProject.service.ICmdbNetworkLineRecordService;

/**
 * 描述：
 * 
 * @author
 * @date 2021-01-27 17:26:16
 */
@Service
public class CmdbNetworkLineRecordServiceImpl implements ICmdbNetworkLineRecordService {

    @Autowired
    private CmdbNetworkLineRecordMapper mapper;

    /**
     * 获取所有实例
     * 
     * @return 返回所有实例数据
     */
    @Override
    public List<CmdbNetworkLineRecord> list() {
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
    public CmdbNetworkLineRecord get(CmdbNetworkLineRecord entity) {
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
    public void insert(CmdbNetworkLineRecord entity) {
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
    public void update(CmdbNetworkLineRecord entity) {
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
    public void delete(CmdbNetworkLineRecord entity) {
        mapper.delete(entity);
    }
}
