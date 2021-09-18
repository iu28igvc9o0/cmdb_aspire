package com.aspire.ums.cmdb.maintenance.mapper;

import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceProjectMfrs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 描述：
* @author
* @date 2019-07-29 22:31:45
*/
@Mapper
public interface CmdbMaintenanceProjectMfrsMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbMaintenanceProjectMfrs> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbMaintenanceProjectMfrs> listByEntity(CmdbMaintenanceProjectMfrs entity);

    /**
     * 获取所有实例
     * @param projectId 维保项目ID
     * @return 返回所有实例数据
     */
    List<CmdbMaintenanceProjectMfrs> getMfrsListByProjectId(@Param("projectId") String projectId);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbMaintenanceProjectMfrs get(CmdbMaintenanceProjectMfrs entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbMaintenanceProjectMfrs entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbMaintenanceProjectMfrs entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbMaintenanceProjectMfrs entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void deleteByProjectId(CmdbMaintenanceProjectMfrs entity);
}