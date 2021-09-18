package com.aspire.ums.cmdb.maintenance.service;

import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceSoftwareRecord;
import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceSoftwareRecordQuery;

import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2019-08-04 18:44:09
*/
public interface ICmdbMaintenanceSoftwareRecordService {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbMaintenanceSoftwareRecord> list();

    /**
     * 根据主键ID 获取数据信息
     * @param query 实例信息
     * @return 返回实例信息的数据信息
     */
    List<CmdbMaintenanceSoftwareRecord> listByEntity(CmdbMaintenanceSoftwareRecordQuery query);


    /**
     * 根据主键ID 获取数据信息
     * @param query 实例信息
     * @return 返回实例信息的数据信息
     */
    int listCount(CmdbMaintenanceSoftwareRecordQuery query);


    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(List<CmdbMaintenanceSoftwareRecord> entity);

//    /**
//     * 修改实例
//     * @param entity 实例数据
//     * @return
//     */
//    void update(CmdbMaintenanceSoftwareRecord entity);

    /**
     * 删除实例
     * @param ids 实例数据
     * @return
     */
    void delete(List<String> ids);
}