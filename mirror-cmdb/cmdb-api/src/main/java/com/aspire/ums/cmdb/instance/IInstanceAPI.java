package com.aspire.ums.cmdb.instance;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.cmdb.code.payload.CmdbSimpleCode;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.instance.payload.CmdbDeleteInstance;
import com.aspire.ums.cmdb.instance.payload.CmdbDeviceTypeByConditonCount;
import com.aspire.ums.cmdb.instance.payload.CmdbDeviceTypeCount;
import com.aspire.ums.cmdb.instance.payload.CmdbInstance;
import com.aspire.ums.cmdb.instance.payload.CmdbQueryInstance;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司 FileName: IInstanceAPI Author: zhu.juwang Date: 2019/5/20 19:44 Description: ${DESCRIPTION}
 * History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
@RequestMapping("/cmdb/instance/")
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
     * 获取信息列表
     * 
     * @param params
     *            查询条件配置
     * @param moduleType
     *            模型类型
     */
    @RequestMapping(value = "/listV3", method = RequestMethod.POST)
    @ApiOperation(value = "获取CI列表", notes = "获取CI列表", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    Result<Map<String, Object>> getInstanceListV3(@RequestBody Map<String, Object> params,
            @RequestParam(value = "moduleType", required = false) String moduleType);

    /**
     * 获取所有的设备列表, 将所有IP地址作为一条主机记录
     * 
     * @param params
     *            查询条件信息
     */
    @RequestMapping(value = "/getAllIPInstance", method = RequestMethod.POST)
    @ApiOperation(value = "获取CI列表", notes = "获取CI列表", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    Result<Map<String, Object>> getAllIPInstance(@RequestBody Map<String, Object> params);

    @RequestMapping(value = "/osListV3", method = RequestMethod.POST)
    @ApiOperation(value = "获取CI列表", notes = "获取CI列表", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    Result<Map<String, Object>> getInstanceOsListV3(@RequestBody Map<String, Object> params,
            @RequestParam(value = "moduleType", required = false) String moduleType);

    /**
     * 获取CI详情
     * 
     * @param moduleId
     *            模型标识
     * @param instanceId
     *            资源信息标识
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @ApiOperation(value = "获取CI详情", notes = "获取CI详情", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    Map<String, Object> getInstanceDetail(@RequestParam("module_id") String moduleId,
            @RequestParam(value = "instance_id") String instanceId);

    /**
     * 新增资源信息
     * 
     * @param username
     *            操作用户
     * @param instanceData
     *            资源信息
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "新增CI", notes = "新增CI", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "新增成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    Map<String, Object> addInstance(@RequestParam("username") String username, @RequestBody Map<String, Object> instanceData);

    /**
     * 更新资源信息
     * 
     * @param id
     *            资源信息ID
     * @param username
     *            操作用户
     * @param instanceData
     *            更新的资源信息
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ApiOperation(value = "更新CI", notes = "更新CI", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "更新成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    Map<String, Object> updateInstance(@PathVariable("id") String id, @RequestParam("username") String username,
            @RequestBody Map<String, Object> instanceData);

    /**
     * 获取需要批量更新的数量
     */
    @RequestMapping(value = "/batchUpdateCount", method = RequestMethod.POST)
    @ApiOperation(value = "批量更新CI", notes = "批量更新CI", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "更新成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    Integer batchUpdateCount(@RequestParam("moduleId") String moduleId, @RequestBody Map<String, Object> batchUpdate);

    /**
     * 查询设备信息
     * 
     * @param params
     *            查询入参
     * @return
     */
    @RequestMapping(value = "/query/detail", method = RequestMethod.POST)
    @ApiOperation(value = "查询实例详细数据信息", notes = "查询实例详细数据信息", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "获取成功", response = CmdbInstance.class),
            @ApiResponse(code = 500, message = "获取成功") })
    Map<String, Object> queryInstanceDetail(@RequestBody Map<String, Object> params,
            @RequestParam(value = "moduleType", required = false) String moduleType);

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
     * 删除资源信息
     * 
     * @param deleteInstance
     *            需要删除的资源集合
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除CI", notes = "删除CI", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "删除成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误") })
    Map<String, Object> deleteInstance(@RequestParam("userName") String userName, @RequestBody CmdbDeleteInstance deleteInstance);

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

    @RequestMapping(value = "/queryServiceCount", method = RequestMethod.GET)
    @ApiOperation(value = "查询服务器数量", notes = "查询服务器数量", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误") })
    List<CmdbDeviceTypeCount> queryServiceCount(@RequestParam(value = "bizSystem", required = false) String bizSystem);

    @RequestMapping(value = "/queryServiceCountForKG", method = RequestMethod.GET)
    @ApiOperation(value = "查询服务器数量", notes = "查询服务器数量", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误") })
    List<CmdbDeviceTypeCount> queryServiceCountForKG();

    @RequestMapping(value = "/getNetworkAndSafetyDeivce", method = RequestMethod.POST)
    @ApiOperation(value = "查询网络设备安全信息", notes = "查询网络设备安全信息", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误") })
    List<Map<String, Object>> getNetworkAndSafetyDeivce(@RequestBody CmdbQueryInstance cmdbQueryInstance);

    /**
     * 导出设备列表
     * 
     * @param params
     *            查询条件
     * @return
     */
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    @ApiOperation(value = "导出设备列表", notes = "导出设备列表", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "导出成功", response = CmdbInstance.class),
            @ApiResponse(code = 500, message = "导出成功") })
    Map<String, String> exportInstance(@RequestBody Map<String, Object> params,
            @RequestParam(value = "moduleType", required = false) String moduleType);
    // /**
    // * 导出设备列表
    // * @param params 查询条件
    // * @param moduleType 模型类型
    // * @return
    // */
    // @RequestMapping(value = "/exportAll", method = RequestMethod.POST)
    // @ApiOperation(value = "导出设备列表", notes = "导出设备列表", tags = {"CMDB Instance API"})
    // @ApiResponses(value = {@ApiResponse(code = 200, message = "导出成功", response = CmdbInstance.class),
    // @ApiResponse(code = 500, message = "导出成功")})
    // Map<String, String> exportAll(@RequestBody Map<String, Object> params,
    // @RequestParam(value = "moduleType", required = false) String moduleType,
    // HttpServletResponse response);

    @RequestMapping(value = "/queryDeviceCountByBizsystem", method = RequestMethod.GET)
    @ApiOperation(value = "查询服务器数量", notes = "查询服务器数量", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功", response = CmdbDeviceTypeByConditonCount.class),
            @ApiResponse(code = 500, message = "内部错误") })
    List<CmdbDeviceTypeByConditonCount> queryDeviceCountByBizsystem(
            @RequestParam(value = "bizSystem", required = false) String bizSystem,
            @RequestParam(value = "idcType", required = false) String idcType,
            @RequestParam(value = "deviceType", required = false) String deviceType,
            @RequestParam(value = "startTime", required = false) String startTime,
            @RequestParam(value = "endTime", required = false) String endTime,
            @RequestParam(value = "sourceType", required = false) String sourceType);

    @RequestMapping(value = "/queryDeviceCountByIdctype", method = RequestMethod.GET)
    @ApiOperation(value = "查询服务器数量", notes = "查询服务器数量", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功", response = CmdbDeviceTypeByConditonCount.class),
            @ApiResponse(code = 500, message = "内部错误") })
    List<CmdbDeviceTypeByConditonCount> queryDeviceCountByIdctype(@RequestParam(value = "idcType", required = false) String idcType,
            @RequestParam(value = "deviceType", required = false) String deviceType,
            @RequestParam(value = "startTime", required = false) String startTime,
            @RequestParam(value = "endTime", required = false) String endTime);

    @RequestMapping(value = "/filterEmptyCiItem", method = RequestMethod.GET)
    @ApiOperation(value = "按照资源池设备类型,核查空配置项数据", notes = "按照资源池设备类型,核查空配置项数据", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功", response = CmdbDeviceTypeByConditonCount.class),
            @ApiResponse(code = 500, message = "内部错误") })
    Map<String, Map<String, Map<String, Integer>>> filterEmptyCiItem(@RequestParam("ciItem") String ciItem);

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

    /*
     * 根据设备分类获取设备数量
     * 
     * @param param 查询参数
     */
    @RequestMapping(value = "deviceCountByDeviceClass", method = RequestMethod.GET)
    @ApiOperation(value = "根据设备分类获取设备数量", notes = "根据设备分类获取设备数量", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    List<Map<String, Object>> deviceCountByDeviceClass(@RequestParam("deviceClass") String deviceClass);

    @RequestMapping(value = "deviceCountByDeviceType", method = RequestMethod.GET)
    @ApiOperation(value = "根据设备类型获取设备数量", notes = "根据设备类型获取设备数量", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误") })
    Map<String, Object> deviceCountByDeviceType(@RequestParam("deviceClass") String deviceClass,
            @RequestParam(value = "deviceType", required = false) String deviceType);

    /**
     * 获取块存储SIZE
     */
    @RequestMapping(value = "getBlockSize", method = RequestMethod.GET)
    @ApiOperation(value = "获取块存储SIZE", notes = "获取块存储SIZE", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功", response = Object.class),
            @ApiResponse(code = 500, message = "内部错误") })
    Object getBlockSize();

    /**
     * 获取信息列表数量
     * 
     * @param params
     *            查询条件配置
     * @param moduleType
     *            模型类型
     */
    @RequestMapping(value = "/listV3Count", method = RequestMethod.POST)
    @ApiOperation(value = "获取CI列表数量", notes = "获取CI列表数量", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    Integer listV3Count(@RequestBody Map<String, Object> params,
            @RequestParam(value = "moduleType", required = false) String moduleType);

    /**
     * 同步cmdb资产-新增资源信息(不需审批)
     * 
     * @param username
     *            操作用户
     * @param instanceData
     *            资源信息
     * @param operateType
     *            操作类型
     */
    @RequestMapping(value = "/addInstanceNoApprove", method = RequestMethod.POST)
    @ApiOperation(value = "同步cmdb资产-新增CI", notes = "同步cmdb资产-新增CI", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "新增成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    Map<String, Object> addInstanceNoApprove(@RequestParam("username") String username,
            @RequestBody Map<String, Object> instanceData, @RequestParam("operateType") String operateType);

    /**
     * 同步cmdb资产-删除资源信息(不需审批)
     * 
     * @param userName
     *            操作用户
     * @param instanceList
     *            删除的instance
     * @param operateType
     *            操作类型
     */
    @RequestMapping(value = "/deleteInstanceNoApprove", method = RequestMethod.DELETE)
    @ApiOperation(value = "同步cmdb资产-删除CI", notes = "同步cmdb资产-删除CI", tags = { "CMDB Instance API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "删除成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误") })
    Map<String, Object> deleteInstanceNoApprove(@RequestParam("userName") String userName,
            @RequestBody List<Map<String, Object>> instanceList, @RequestParam("operateType") String operateType);

}
