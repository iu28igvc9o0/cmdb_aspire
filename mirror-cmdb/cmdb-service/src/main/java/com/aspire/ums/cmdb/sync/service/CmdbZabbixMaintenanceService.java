package com.aspire.ums.cmdb.sync.service;

import java.util.List;

import com.aspire.ums.cmdb.sync.entity.CmdbZabbixMaintenance;

/**
 * @since 2020年08月27日 14:51:44
 * @author jiangxuwen
 * @version v1.0
 */
public interface CmdbZabbixMaintenanceService {

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
    void add(CmdbZabbixMaintenance cmdbZabbixMaintenance);

    /**
     * 更新
     * 
     * @param cmdbZabbixMaintenance
     * @since 2020年08月27日 14:51:44
     * @author jiangxuwen
     * @version v1.0
     */
    void modify(CmdbZabbixMaintenance cmdbZabbixMaintenance);

    /**
     * 删除
     * 
     * @param id
     * @since 2020年08月27日 14:51:44
     * @author jiangxuwen
     * @version v1.0
     */
    void delete(String id);

}
