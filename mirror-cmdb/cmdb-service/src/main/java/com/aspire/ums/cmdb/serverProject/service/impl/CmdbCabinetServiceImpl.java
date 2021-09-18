package com.aspire.ums.cmdb.serverProject.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.ums.cmdb.serverProject.mapper.CmdbCabinetMapper;
import com.aspire.ums.cmdb.serverProject.payload.CmdbCabinet;
import com.aspire.ums.cmdb.serverProject.service.ICmdbCabinetService;

/**
 * 描述：
 * 
 * @author
 * @date 2021-01-27 17:26:14
 */
@Service
public class CmdbCabinetServiceImpl implements ICmdbCabinetService {

    @Autowired
    private CmdbCabinetMapper mapper;

    /**
     * 获取所有实例
     * 
     * @return 返回所有实例数据
     */
    @Override
    public List<CmdbCabinet> list() {
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
    public CmdbCabinet get(CmdbCabinet entity) {
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
    public void insert(CmdbCabinet entity) {
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
    public void update(CmdbCabinet entity) {
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
    public void delete(CmdbCabinet entity) {
        mapper.delete(entity);
    }
}
