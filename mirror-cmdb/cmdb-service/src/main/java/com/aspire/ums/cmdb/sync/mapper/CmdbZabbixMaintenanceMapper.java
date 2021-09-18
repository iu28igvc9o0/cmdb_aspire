package com.aspire.ums.cmdb.sync.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aspire.ums.cmdb.sync.entity.CmdbZabbixMaintenance;

/**
 * @since 2020年08月27日 14:51:44
 * @author jiangxuwen
 * @version v1.0
 */
@Mapper
public interface CmdbZabbixMaintenanceMapper {

    /**
     * 根据主键查询对象
     * 
     * @param id
     * @return
     * @since 2020年08月27日 14:51:44
     * @author jiangxuwen
     * @version v1.0
     */
    CmdbZabbixMaintenance findById(String id);

    /**
     * 分页查询
     * 
     * @param cmdbZabbixMaintenance
     * @return
     * @since 2020年08月27日 14:51:44
     * @author jiangxuwen
     * @version v1.0
     */
    List<CmdbZabbixMaintenance> findPage(CmdbZabbixMaintenance cmdbZabbixMaintenance);

    /**
     * 新增
     * 
     * @param cmdbZabbixMaintenance
     * @since 2020年08月27日 14:51:44
     * @author jiangxuwen
     * @version v1.0
     */
    int insert(CmdbZabbixMaintenance cmdbZabbixMaintenance);

    /**
     * 批量插入
     * 
     * @param entityList
     *            待插入的实体列表
     * @since 2020年08月27日 14:51:44
     * @author jiangxuwen
     * @version v1.0
     */
    void batchInsert(List<CmdbZabbixMaintenance> entityList);

    /**
     * 更新
     * 
     * @param cmdbZabbixMaintenance
     * @since 2020年08月27日 14:51:44
     * @author jiangxuwen
     * @version v1.0
     */
    int update(CmdbZabbixMaintenance cmdbZabbixMaintenance);

    /**
     * 删除
     * 
     * @param id
     * @since 2020年08月27日 14:51:44
     * @author jiangxuwen
     * @version v1.0
     */
    int delete(String id);

    /**
     * 批量删除
     * 
     * @param idList
     *            id列表
     * @since 2020年08月27日 14:51:44
     * @author jiangxuwen
     * @version v1.0
     */
    void batchDelete(List<String> idList);
}
