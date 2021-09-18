package com.aspire.ums.cmdb.sync.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aspire.ums.cmdb.sync.entity.CmdbSyncFieldMapping;

/**
 * 描述：
 * 
 * @author
 * @date 2020-05-19 19:25:57
 */
@Mapper
public interface CmdbSyncFieldMappingMapper {

    /**
     * 获取所有实例
     * 
     * @return 返回所有实例数据
     */
    List<CmdbSyncFieldMapping> list();

    /**
     * 获取所有实例
     * 
     * @param entity
     *            实例信息
     * @return 返回所有实例数据
     */
    List<CmdbSyncFieldMapping> listByEntity(CmdbSyncFieldMapping entity);

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
}
