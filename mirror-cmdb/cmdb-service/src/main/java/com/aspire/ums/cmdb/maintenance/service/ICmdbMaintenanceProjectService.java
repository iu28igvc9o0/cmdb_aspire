package com.aspire.ums.cmdb.maintenance.service;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceProject;
import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceProjectQuery;
import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceServiceNum;

import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2019-07-29 22:31:46
*/
public interface ICmdbMaintenanceProjectService {
    /**
     * 获取所有实例,部分列表属性
     * @return 返回所有实例数据
     */
    Result<Map<String,Object>> getSimpleList(CmdbMaintenanceProjectQuery entity);

     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbMaintenanceProject> list();

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    Result<CmdbMaintenanceProject> listByEntity(CmdbMaintenanceProjectQuery query);

    /**
     * 获取所有实例(不包括与金额相关的字段)
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    Result<Map<String,Object>> listNotMoney(CmdbMaintenanceProjectQuery entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbMaintenanceProject get(CmdbMaintenanceProject entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void save(CmdbMaintenanceProject entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbMaintenanceProject entity);

    /**
     * 删除实例
     * @param projectId 实例数据
     * @return
     */
    JSONObject deleteMaintenanceProject(String projectId);

    /**
     *  导出维保项目数据源
     * @return
     */
    List<Map<String, Object>> exportProjectList(CmdbMaintenanceProjectQuery query);

    /**
     * 根据设备序列号查询有效的维保项目
     * @param deviceSn 设备序列号
     * @return
     */
    CmdbMaintenanceProject getValidProjectByDeviceSn(String deviceSn);

    /*
    * 添加维保项目的服务数量
    * */
    void insertServiceNum(List<CmdbMaintenanceServiceNum> data);

    /**
     *  同步维保数据到CI详细信息
     * @return
     */
    String syncInstanceMaintenanceInfo(String projectId,List<String> projectInstanceList);

    /**
     *  依据设备序列号和服务时间反查维保项目
     * @return
     */
    List<Map<String, Object>> getMaintenObjByTimeAndSn(Map<String,String> mpValue);
}