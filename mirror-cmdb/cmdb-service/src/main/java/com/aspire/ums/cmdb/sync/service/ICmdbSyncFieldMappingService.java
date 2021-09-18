package com.aspire.ums.cmdb.sync.service;

import java.util.List;

import com.aspire.ums.cmdb.sync.entity.CmdbSyncFieldMapping;

/**
 * 描述：
 * 
 * @author
 * @date 2020-05-19 19:25:57
 */
public interface ICmdbSyncFieldMappingService {

    /**
     * 获取所有实例
     * 
     * @return 返回所有实例数据
     */
    List<CmdbSyncFieldMapping> list();

    List<CmdbSyncFieldMapping> list(CmdbSyncFieldMapping entity);

    /**
     * 根据主键ID 获取数据信息
     * 
     * @param entity
     *            实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbSyncFieldMapping get(CmdbSyncFieldMapping entity);

    CmdbSyncFieldMapping getOne(CmdbSyncFieldMapping entity);

    /**
     * 新增实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void insert(CmdbSyncFieldMapping entity);

    /**
     * 修改实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void update(CmdbSyncFieldMapping entity);

    /**
     * 删除实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void delete(CmdbSyncFieldMapping entity);

    void redisRefresh(String mappingType, String mappingKey);

    void redisSet(String mappingType, String mappingKey);

    List<CmdbSyncFieldMapping> redisGet(String mappingType, String mappingKey);
}
