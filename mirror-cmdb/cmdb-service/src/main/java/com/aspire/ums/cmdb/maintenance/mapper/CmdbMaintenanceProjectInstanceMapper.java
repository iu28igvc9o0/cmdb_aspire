package com.aspire.ums.cmdb.maintenance.mapper;

import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceProjectBindInstance;
import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceProjectBindInstanceQuery;
import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceProjectInstance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 描述：
* @author
* @date 2019-07-29 22:31:45
*/
@Mapper
public interface CmdbMaintenanceProjectInstanceMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbMaintenanceProjectInstance> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbMaintenanceProjectInstance> listByEntity(CmdbMaintenanceProjectInstance entity);

    /**
     * 获取所有实例
     * @param projectId 维保项目ID
     * @return 返回所有实例数据
     */
    List<CmdbMaintenanceProjectInstance> getProjectInstanceListByProjectId(@Param("projectId") String projectId);

    /**
     * 查询绑定设备
     * @param query 查询绑定设备查询条件
     * @return 返回所有实例数据
     */
    List<CmdbMaintenanceProjectBindInstance> getProjectBindInstanceList(CmdbMaintenanceProjectBindInstanceQuery query);

    /**
     * 查询绑定设备数量
     * @param query 查询绑定设备查询条件
     * @return 返回所有实例数
     */
    Integer getProjectBindInstanceListCount(CmdbMaintenanceProjectBindInstanceQuery query);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbMaintenanceProjectInstance get(CmdbMaintenanceProjectInstance entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbMaintenanceProjectInstance entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbMaintenanceProjectInstance entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbMaintenanceProjectInstance entity);
}