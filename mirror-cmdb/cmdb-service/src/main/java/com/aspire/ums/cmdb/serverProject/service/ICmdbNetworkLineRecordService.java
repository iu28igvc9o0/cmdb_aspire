package com.aspire.ums.cmdb.serverProject.service;

import java.util.List;

import com.aspire.ums.cmdb.serverProject.payload.CmdbNetworkLineRecord;

/**
 * 描述：
 * 
 * @author
 * @date 2021-01-27 17:26:16
 */
public interface ICmdbNetworkLineRecordService {

    /**
     * 获取所有实例
     * 
     * @return 返回所有实例数据
     */
    List<CmdbNetworkLineRecord> list();

    /**
     * 根据主键ID 获取数据信息
     * 
     * @param entity
     *            实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbNetworkLineRecord get(CmdbNetworkLineRecord entity);

    /**
     * 新增实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void insert(CmdbNetworkLineRecord entity);

    /**
     * 修改实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void update(CmdbNetworkLineRecord entity);

    /**
     * 删除实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void delete(CmdbNetworkLineRecord entity);
}
