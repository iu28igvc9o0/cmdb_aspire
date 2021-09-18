package com.aspire.ums.cmdb.maintenance.service;


import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceProjectBindInstance;
import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceProjectBindInstanceQuery;
import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceProjectInstance;

import java.util.List;

/**
* 描述：
* @author
* @date 2019-07-29 22:31:45
*/
public interface ICmdbMaintenanceProjectInstanceService {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbMaintenanceProjectInstance> list();

    /**
     * 获取所有已绑定设备列表
     * @return 返回所有实例数据
     */
    Result<CmdbMaintenanceProjectBindInstance> getProjectBindInstanceList(CmdbMaintenanceProjectBindInstanceQuery query);

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
     * 新增实例
     * @param listEntity 实例数据
     * @param projectId 项目ID
     * @return
     */
    void batchInsert(List<CmdbMaintenanceProjectInstance> listEntity, String projectId);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbMaintenanceProjectInstance entity);

    /**
     * 根据项目, 获取所有绑定的设备列表
     * @param projectId
     * @return
     */
    List<CmdbMaintenanceProjectInstance> getProjectInstanceListByProjectId(String projectId);

    /**
     * 删除关联设备
     * @param projectInstanceId
     * @param projectId
     */
    JSONObject delete(String projectInstanceId, String projectId);

    /**
     * 批量移除绑定
     * @param projectInstanceList
     * @param projectId
     */
    void batchRemove(List<CmdbMaintenanceProjectInstance> projectInstanceList, String projectId);
}