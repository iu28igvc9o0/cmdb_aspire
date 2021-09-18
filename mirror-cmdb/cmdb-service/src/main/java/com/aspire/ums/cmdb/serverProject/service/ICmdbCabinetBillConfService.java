package com.aspire.ums.cmdb.serverProject.service;

import java.util.List;

import com.aspire.ums.cmdb.serverProject.payload.CmdbCabinetBillConf;

/**
 * 描述：
 * 
 * @author
 * @date 2021-01-27 17:26:15
 */
public interface ICmdbCabinetBillConfService {

    /**
     * 获取所有实例
     * 
     * @return 返回所有实例数据
     */
    List<CmdbCabinetBillConf> list();

    /**
     * 根据主键ID 获取数据信息
     * 
     * @param entity
     *            实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbCabinetBillConf get(CmdbCabinetBillConf entity);

    /**
     * 新增实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void insert(CmdbCabinetBillConf entity);

    /**
     * 修改实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void update(CmdbCabinetBillConf entity);

    /**
     * 删除实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void delete(CmdbCabinetBillConf entity);
}
