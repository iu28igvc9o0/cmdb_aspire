package com.aspire.ums.cmdb.maintenance.mapper;

import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceSoftwareRecord;
import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceSoftwareRecordQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2019-08-04 18:44:09
*/
@Mapper
public interface CmdbMaintenanceSoftwareRecordMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbMaintenanceSoftwareRecord> list();

    /**
     * 获取所有实例
     * @param query 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbMaintenanceSoftwareRecord> listByEntity(CmdbMaintenanceSoftwareRecordQuery query);

    /**
     * 获取所有实例数量
     * @param query 实例信息
     * @return 返回所有实例数据
     */
    int listCount(CmdbMaintenanceSoftwareRecordQuery query);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbMaintenanceSoftwareRecord get(CmdbMaintenanceSoftwareRecord entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(List<CmdbMaintenanceSoftwareRecord> entity);

    /**
     * 删除实例
     * @param ids 实例数据
     * @return
     */
    void deleteByBatch(List<String> ids);
}