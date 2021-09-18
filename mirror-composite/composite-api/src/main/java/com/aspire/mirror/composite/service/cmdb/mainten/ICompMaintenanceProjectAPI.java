package com.aspire.mirror.composite.service.cmdb.mainten;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.maintenance.payload.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICmdbMaintenanceProjectAPI
 * Author:   zhu.juwang
 * Date:     2019/7/29 17:32
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Api(value = "维保项目管理接口")
@RequestMapping("/${version}/cmdb/maintenance/project")
public interface ICompMaintenanceProjectAPI {

    /**
     * 获取所有实例,部分列表属性
     * @return 返回所有实例数据
     */
    @PostMapping(value = "/getSimpleList" )
    @ApiOperation(value = "获取所有实例,部分列表属性", notes = "获取所有实例,部分列表属性", tags = {"Cmdb Maintenance Project API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "保存成功", response = Result.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<Map<String,Object>> getSimpleList(@RequestBody CmdbMaintenanceProjectQuery entity);

    /**
     *  保存维保项目
     * @return
     */
    @PostMapping(value = "/save" )
    @ApiOperation(value = "保存维保项目", notes = "保存维保项目", tags = {"Cmdb Maintenance Project API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "保存成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject insertMaintenanceProject(@RequestBody CmdbMaintenanceProject maintenanceProject);

    /**
     *  查询维保项目
     * @return
     */
    @PostMapping(value = "/list" )
    @ApiOperation(value = "获取维保项目列表", notes = "获取维保项目列表", tags = {"Cmdb Maintenance Project API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<CmdbMaintenanceProject> getMaintenanceProjectList(@RequestBody CmdbMaintenanceProjectQuery query);

    /**
     *  查询维保项目（没有金额字段）
     * @return
     */
    @PostMapping(value = "/listNotMoney" )
    @ApiOperation(value = "获取维保项目列表（没有金额字段）", notes = "获取维保项目列表（没有金额字段）", tags = {"Cmdb Maintenance Project API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<Map<String,Object>> getMaintenanceProjectListNotMoney(@RequestBody CmdbMaintenanceProjectQuery query);

    /**
     *  查询维保项目
     * @return
     */
    @GetMapping(value = "/get" )
    @ApiOperation(value = "获取维保项目详细信息", notes = "获取维保项目详细信息", tags = {"Cmdb Maintenance Project API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CmdbMaintenanceProject getMaintenanceProjectInfo(@RequestParam("projectId") String projectId);

    /**
     *  根据项目名称查询维保项目
     * @return
     */
    @GetMapping(value = "/getByProjectName")
    @ApiOperation(value = "根据项目名称查询维保项目", notes = "根据项目名称查询维保项目", tags = {"Cmdb Maintenance Project API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CmdbMaintenanceProject getMaintenanceProjectInfoByName(@RequestParam("projectName") String projectName);

    /**
     * 维保项目绑定设备
     */
    @PostMapping(value = "/bindInstance" )
    @ApiOperation(value = "维保项目绑定设备", notes = "维保项目绑定设备", tags = {"Cmdb Maintenance Project API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject bindProjectInstance(@RequestBody List<CmdbMaintenanceProjectInstance> projectInstanceList,
                                   @RequestParam("project_id") String projectId);

    /**
     * 维保项目绑定设备
     */
    @DeleteMapping(value = "/removeBindInstance" )
    @ApiOperation(value = "取消维保项目绑定设备序列号", notes = "取消维保项目绑定设备序列号", tags = {"Cmdb Maintenance Project API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject removeBindInstance(@RequestParam("project_instance_id") String projectInstanceId,
                                  @RequestParam("project_id") String projectId);
    /**
     *  获取维保项目绑定设备列表
     * @return
     */
    @PostMapping(value = "/listBindInstance" )
    @ApiOperation(value = "获取维保项目绑定设备列表", notes = "获取维保项目绑定设备列表", tags = {"Cmdb Maintenance Project API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<CmdbMaintenanceProjectBindInstance> listBindInstance(@RequestBody CmdbMaintenanceProjectBindInstanceQuery query);

    /**
     *  删除维保项目
     * @return
     */
    @DeleteMapping(value = "/delete" )
    @ApiOperation(value = "删除维保项目", notes = "删除维保项目", tags = {"Cmdb Maintenance Project API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "删除成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject deleteMaintenanceProject(@RequestParam("project_id") String projectId);

    /**
     *  删除维保项目
     * @return
     */
    @PostMapping(value = "/export" )
    @ApiOperation(value = "导出维保项目", notes = "导出维保项目", tags = {"Cmdb Maintenance Project API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "导出成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject exportProject(@RequestBody CmdbMaintenanceProjectQuery query, HttpServletResponse response);

    /**
     * 根据设备序列号查询有效的维保项目
     * @param deviceSn 设备序列号
     * @return
     */
    @GetMapping(value = "/getValidProjectByDeviceSn" )
    @ApiOperation(value = "根据设备序列号查询有效的维保项目", notes = "根据设备序列号查询有效的维保项目", tags = {"Cmdb Maintenance Project API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CmdbMaintenanceProject getValidProjectByDeviceSn(@RequestParam("device_sn") String deviceSn);

    /**
     *  增加维保服务数量
     * @return
     */
    @PostMapping(value = "/addServiceNum" )
    @ApiOperation(value = "增加维保服务数量", notes = "增加维保服务数量", tags = {"Cmdb Maintenance Project API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject addServiceNum(@RequestBody List<CmdbMaintenanceServiceNum> request);

    /**
     *  依据服务时间和序列号反查维保项目
     * @return
     */
    @PostMapping(value = "/getMaintenObjByTimeAndSn" )
    @ApiOperation(value = "依据服务时间和序列号反查维保项目", notes = "依据服务时间和序列号反查维保项目", tags = {"Cmdb Maintenance Project API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> getMaintenObjByTimeAndSn(@RequestBody Map<String,String> mpValue);
}

