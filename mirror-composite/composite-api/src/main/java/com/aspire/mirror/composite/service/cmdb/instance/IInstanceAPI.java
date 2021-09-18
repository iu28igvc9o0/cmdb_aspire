package com.aspire.mirror.composite.service.cmdb.instance;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.ums.cmdb.code.payload.CmdbSimpleCode;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.instance.payload.CmdbDeleteInstance;
import com.aspire.ums.cmdb.instance.payload.CmdbDeviceTypeCount;
import com.aspire.ums.cmdb.instance.payload.CmdbInstance;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司 FileName: IInstanceAPI Author: zhu.juwang Date: 2019/5/20 19:44 Description: ${DESCRIPTION}
 * History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
@RequestMapping("${version}/cmdb/instance")
public interface IInstanceAPI {

    /**
     * 获取CI列表
     */
    @RequestMapping(value = "/header", method = RequestMethod.GET)
    @ApiOperation(value = "获取CI表头", notes = "获取CI表头", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    List<CmdbSimpleCode> getInstanceHeader(@RequestParam(value = "moduleId", required = false) String moduleId,
            @RequestParam(value = "moduleType", required = false) String moduleType);

    /**
     * 获取CI列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation(value = "获取CI列表", notes = "获取CI列表", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    Result<Map<String, Object>> getInstanceList(@RequestBody Map<String, Object> params);

    /**
     * 获取CI列表
     */
    @RequestMapping(value = "/listV3", method = RequestMethod.POST)
    @ApiOperation(value = "获取CI列表", notes = "获取CI列表", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    Result<Map<String, Object>> getInstanceListV3(@RequestBody Map<String, Object> params,
            @RequestParam(value = "moduleType", required = false) String moduleType);

    /**
     * 获取CI列表
     */
    @RequestMapping(value = "/osListV3", method = RequestMethod.POST)
    @ApiOperation(value = "获取CI列表", notes = "获取CI列表", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    Result<Map<String, Object>> getInstanceOsListV3(@RequestBody Map<String, Object> params,
            @RequestParam(value = "moduleType", required = false) String moduleType);

    @ApiOperation(value = "导出", notes = "导出", response = PageResponse.class, tags = { "CMDB Instance API" })
    @RequestMapping(value = "/exportOsListV3", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    void exportOsListV3(@RequestBody Map<String, Object> params,
            @RequestParam(value = "moduleType", required = false) String moduleType, HttpServletResponse response) throws Exception;

    /**
     * 获取CI详情
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @ApiOperation(value = "获取CI详情", notes = "获取CI详情", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    Map<String, Object> getInstanceDetail(@RequestParam("module_id") String moduleId,
            @RequestParam(value = "instance_id") String instanceId);

    /**
     * 新增CI
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "新增CI", notes = "新增CI", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "新增成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    Map<String, Object> addInstance(@RequestBody Map<String, Object> instanceData);

    /**
     * 更新CI
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ApiOperation(value = "更新CI", notes = "更新CI", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "更新成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    Map<String, Object> updateInstance(@PathVariable("id") String id, @RequestBody Map<String, Object> instanceData);

    /**
     * 获取需要批量更新的数量
     */
    @RequestMapping(value = "/batchUpdateCount", method = RequestMethod.POST)
    @ApiOperation(value = "批量更新CI", notes = "批量更新CI", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "更新成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    Integer batchUpdateCount(@RequestParam("moduleId") String moduleId, @RequestBody Map<String, Object> batchUpdate);

    /**
     * 根据资源池及IP地址查询设备信息
     * 
     * @param params
     *            查询入参
     * @return
     */
    @RequestMapping(value = "/query/detail", method = RequestMethod.POST)
    @ApiOperation(value = "查询实例详细数据信息", notes = "查询实例详细数据信息", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "获取成功", response = CmdbInstance.class),
            @ApiResponse(code = 500, message = "获取成功") })
    Map<String, Object> queryInstanceDetail(@RequestBody Map<String, Object> params);

    /**
     * 导出设备列表
     * 
     * @param params
     *            查询条件
     * @param moduleType
     *            主机类型
     * @return
     */
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    @ApiOperation(value = "根据资源池及IP地址查询设备信息", notes = "根据资源池及IP地址查询设备信息", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "获取成功", response = CmdbInstance.class),
            @ApiResponse(code = 500, message = "获取成功") })
    Map<String, String> exportInstance(HttpServletResponse response, @RequestBody Map<String, Object> params,
            @RequestParam(value = "moduleType", required = false) String moduleType);

    /**
     * 删除CI
     * 
     * @param deleteInstance
     *            需要删除的CI集合
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除CI", notes = "删除CI", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "删除成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误") })
    Map<String, Object> deleteInstance(@RequestBody CmdbDeleteInstance deleteInstance);

    /**
     * 获取指定设备ID的设备集合
     * 
     * @param deviceIds
     *            设备ID集合
     */
    @RequestMapping(value = "/listInstanceBaseInfo", method = RequestMethod.POST)
    @ApiOperation(value = "获取指定设备ID的设备集合", notes = "获取指定设备ID的设备集合", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误") })
    List<CmdbInstance> getInstanceByDeviceIds(@RequestBody String deviceIds);

    /**
     * 获取资源池树
     */
    @RequestMapping(value = "/getIdcTree", method = RequestMethod.GET)
    @ApiOperation(value = "获取资源池树", notes = "获取资源池树", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误") })
    List<Map> getIdcTree();

    /**
     * 获取设备类型树
     */
    @RequestMapping(value = "/getDeviceClassTree", method = RequestMethod.GET)
    @ApiOperation(value = "获取资源池树", notes = "获取资源池树", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误") })
    List<Map> getDeviceClassTree();

    /**
     * 根据资源池获取部门
     */
    @RequestMapping(value = "/getDepartmentsByIDC", method = RequestMethod.GET)
    @ApiOperation(value = "根据资源池获取部门", notes = "根据资源池获取部门", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误") })
    List<String> getDepartmentsByIDC(@RequestParam("idcType") String idcType);

    /*********************************************************************************************************
     *
     * 后期关于资源池、机房、POD的接口 需要移植对应的Service/dao类中
     *
     ********************************************************************************************************/
    /**
     * 根据多个资源池ID获取资源池列表
     * 
     * @param ids
     * @return
     */
    @RequestMapping(value = "/getIdcByIds", method = RequestMethod.GET)
    @ApiOperation(value = "根据多个资源池ID获取资源池列表", notes = "根据多个资源池ID获取资源池列表", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误") })
    List<Map<String, String>> getIdcByIds(@RequestParam("ids") String ids);

    /**
     * 根据多个podID获取pod列表
     * 
     * @param ids
     * @return
     */
    @RequestMapping(value = "/getPodByIds", method = RequestMethod.GET)
    @ApiOperation(value = "根据多个podID获取pod列表", notes = "根据多个podID获取pod列表", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误") })
    List<Map<String, String>> getPodByIds(@RequestParam("ids") String ids);

    /**
     * 根据多个机房ID获取机房列表
     * 
     * @param ids
     * @return
     */
    @RequestMapping(value = "/getRoomByIds", method = RequestMethod.GET)
    @ApiOperation(value = "根据多个机房ID获取机房列表", notes = "根据多个机房ID获取机房列表", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误") })
    List<Map<String, String>> getRoomByIds(@RequestParam("ids") String ids);

    /**
     * 根据设备序列号查询设备信息
     * 
     * @param deviceSn
     *            设备序列号
     * @return
     */
    @RequestMapping(value = "/queryDeviceByDeviceSn", method = RequestMethod.GET)
    @ApiOperation(value = "根据设备序列号查询设备信息", notes = "根据设备序列号查询设备信息", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "获取成功", response = CmdbInstance.class),
            @ApiResponse(code = 500, message = "获取成功") })
    CmdbInstance queryDeviceByDeviceSn(@RequestParam(value = "deviceSn") String deviceSn,
            @RequestParam(value = "deviceArea") String deviceArea);

    /**
     * 查询服务器数量
     */
    @RequestMapping(value = "/queryServiceCount", method = RequestMethod.GET)
    @ApiOperation(value = "查询服务器数量", notes = "查询服务器数量", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "获取成功", response = CmdbInstance.class),
            @ApiResponse(code = 500, message = "获取成功") })
    List<CmdbDeviceTypeCount> queryServiceCount(@RequestParam(value = "bizSystem", required = false) String bizSystem);

    /**
     * 根据资源池及IP地址查询设备信息
     * 
     * @param params
     *            查询入参
     * @return
     */
    @RequestMapping(value = "/queryDeviceByIdcTypeAndIP", method = RequestMethod.POST)
    @ApiOperation(value = "根据资源池和IP", notes = "查询实例详细数据信息", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "获取成功", response = CmdbInstance.class),
            @ApiResponse(code = 500, message = "获取成功") })
    Map<String, Object> queryDeviceByIdcTypeAndIP(@RequestBody Map<String, Object> params);

    /**
     * 根据资源池及IP地址查询设备信息
     * 
     * @param idc
     *            资源池
     * @param deviceIp
     *            设备IP
     * @return
     */
    @RequestMapping(value = "/queryDeviceByRoomAndIP", method = RequestMethod.GET)
    @ApiOperation(value = "根据资源池及IP地址查询设备信息", notes = "根据资源池及IP地址查询设备信息", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "获取成功", response = CmdbInstance.class),
            @ApiResponse(code = 500, message = "获取成功") })
    CmdbInstance queryDeviceByRoomIdAndIP(@RequestParam(value = "idc", required = false) String idc,
            @RequestParam("deviceIp") String deviceIp);

    /**
     * 根据资源池及IP地址查询设备信息
     * 
     * @param idc
     *            资源池
     * @param deviceIp
     *            设备IP
     * @return
     */
    @RequestMapping(value = "/queryDeviceByRoomAndIP", method = RequestMethod.POST)
    @ApiOperation(value = "根据资源池及IP地址查询设备信息", notes = "根据资源池及IP地址查询设备信息", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "获取成功", response = CmdbInstance.class),
            @ApiResponse(code = 500, message = "获取成功") })
    CmdbInstance queryDeviceByRoomIdAndIP2(@RequestParam(value = "idc", required = false) String idc,
            @RequestParam("deviceIp") String deviceIp);

    /*
     * 查询CI基本属性信息列表
     * 
     * @param param 查询参数
     */
    @RequestMapping(value = "/listBaseInfo", method = RequestMethod.POST)
    @ApiOperation(value = "查询CI基本属性信息列表", notes = "查询CI基本属性信息列表", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    List<Map<String, Object>> getInstanceBaseInfo(@RequestBody Map<String, Object> param);

    /**
     * 获取CI列表数量
     */
    @RequestMapping(value = "/listV3Count", method = RequestMethod.POST)
    @ApiOperation(value = "获取CI列表数量", notes = "获取CI列表数量", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    Integer listV3Count(@RequestBody Map<String, Object> params,
            @RequestParam(value = "moduleType", required = false) String moduleType);
}
