package com.aspire.ums.cmdb.serverProject.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.ums.cmdb.serverProject.mapper.CmdbCabinetBillConfMapper;
import com.aspire.ums.cmdb.serverProject.payload.CmdbCabinetBillConf;
import com.aspire.ums.cmdb.serverProject.service.ICmdbCabinetBillConfService;

/**
 * 描述：
 * 
 * @author
 * @date 2021-01-27 17:26:15
 */
@Service
public class CmdbCabinetBillConfServiceImpl implements ICmdbCabinetBillConfService {

    @Autowired
    private CmdbCabinetBillConfMapper mapper;

    /**
     * 获取所有实例
     * 
     * @return 返回所有实例数据
     */
    @Override
    public List<CmdbCabinetBillConf> list() {
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
    public CmdbCabinetBillConf get(CmdbCabinetBillConf entity) {
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
    public void insert(CmdbCabinetBillConf entity) {
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
    public void update(CmdbCabinetBillConf entity) {
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
    public void delete(CmdbCabinetBillConf entity) {
        mapper.delete(entity);
    }
}
