package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.mainten;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceProject;
import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceProjectBindInstance;
import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceProjectBindInstanceQuery;
import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceProjectInstance;
import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceProjectQuery;
import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceServiceNum;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: MaintenanceProjectClient
 * Author:   zhu.juwang
 * Date:     2019/8/5 10:16
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "CMDB")
public interface MaintenanceProjectClient {
    /**
     * 获取所有实例,部分列表属性
     * @return 返回所有实例数据
     */
    @PostMapping(value = "/cmdb/maintenance/project/getSimpleList" )
    Result<Map<String,Object>> getSimpleList(@RequestBody CmdbMaintenanceProjectQuery entity);

    /**
     *  保存维保项目
     * @return
     */
    @PostMapping(value = "/cmdb/maintenance/project/save" )
    JSONObject insertMaintenanceProject(@RequestBody CmdbMaintenanceProject maintenanceProject);

    /**
     *  查询维保项目
     * @return
     */
    @PostMapping(value = "/cmdb/maintenance/project/list" )
    Result<CmdbMaintenanceProject> getMaintenanceProjectList(@RequestBody CmdbMaintenanceProjectQuery query);

    /**
     *  查询维保项目
     * @return
     */
    @PostMapping(value = "/cmdb/maintenance/project/listNotMoney" )
    Result<Map<String,Object>> getMaintenanceProjectListNotMoney(@RequestBody CmdbMaintenanceProjectQuery query);

    /**
     *  查询维保项目
     * @return
     */
    @GetMapping(value = "/cmdb/maintenance/project/get" )
    CmdbMaintenanceProject getMaintenanceProjectInfo(@RequestParam("projectId") String projectId);

    /**
     *  查询维保项目
     * @return
     */
    @GetMapping(value = "/cmdb/maintenance/project/getByProjectName" )
    CmdbMaintenanceProject getMaintenanceProjectInfoByName(@RequestParam("projectName") String projectName);

    /**
     * 维保项目绑定设备
     */
    @PostMapping(value = "/cmdb/maintenance/project/bindInstance" )
    JSONObject bindProjectInstance(@RequestBody List<CmdbMaintenanceProjectInstance> projectInstanceList,
                                   @RequestParam("project_id") String projectId);

    /**
     * 维保项目绑定设备
     */
    @DeleteMapping(value = "/cmdb/maintenance/project/removeBindInstance" )
    JSONObject removeBindInstance(@RequestParam("project_instance_id") String projectInstanceId,
                                  @RequestParam("project_id") String projectId);

    /**
     *  获取维保项目绑定设备列表
     * @return
     */
    @PostMapping(value = "/cmdb/maintenance/project/listBindInstance" )
    Result<CmdbMaintenanceProjectBindInstance> listBindInstance(@RequestBody CmdbMaintenanceProjectBindInstanceQuery query);

    /**
     *  删除维保项目
     * @return
     */
    @DeleteMapping(value = "/cmdb/maintenance/project/delete" )
    JSONObject deleteMaintenanceProject(@RequestParam("project_id") String projectId);

    /**
     *  获取维保项目绑定设备列表
     * @return
     */
    @PostMapping(value = "/cmdb/maintenance/project/export" )
    List<Map<String, Object>> exportProjectList(@RequestBody CmdbMaintenanceProjectQuery query);

    /**
     * 根据设备序列号查询有效的维保项目
     * @param deviceSn 设备序列号
     * @return
     */
    @GetMapping(value = "/cmdb/maintenance/project/getValidProjectByDeviceSn" )
    CmdbMaintenanceProject getValidProjectByDeviceSn(@RequestParam("device_sn") String deviceSn);

    /**
     *  增加维保服务数量
     * @return
     */
    @PostMapping(value = "/cmdb/maintenance/project/addServiceNum" )
    JSONObject addServiceNum(@RequestBody List<CmdbMaintenanceServiceNum> request);

    /**
     *  依据服务时间和序列号反查维保项目
     * @return
     */
    @PostMapping(value = "/cmdb/maintenance/project/getMaintenObjByTimeAndSn" )
    List<Map<String, Object>> getMaintenObjByTimeAndSn(@RequestBody Map<String,String> mpValue);
}
