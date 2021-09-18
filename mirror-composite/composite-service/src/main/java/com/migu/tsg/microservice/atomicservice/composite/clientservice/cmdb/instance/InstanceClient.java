package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.instance;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
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

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: InstanceClient
 * Author:   zhu.juwang
 * Date:     2019/5/20 19:44
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "CMDB")
public interface InstanceClient {

    /**
     * 获取CI列表
     */
    @RequestMapping(value = "/cmdb/instance/header", method = RequestMethod.GET)
    List<CmdbSimpleCode> getInstanceHeader(@RequestParam(value = "moduleId",required = false) String moduleId,
                                           @RequestParam(value = "moduleType", required = false) String moduleType);

    /**
     * 获取CI列表
     */
    @RequestMapping(value = "/cmdb/instance/list", method = RequestMethod.POST)
    Result<Map<String, Object>> getInstanceList(@RequestBody Map<String, Object> params);

    /**
     * 获取CI列表
     */
    @RequestMapping(value = "/cmdb/instance/listV3", method = RequestMethod.POST)
    Result<Map<String, Object>> getInstanceListV3(@RequestBody Map<String, Object> params,
                                                  @RequestParam(value = "moduleType", required = false) String moduleType);

    @RequestMapping(value = "/cmdb/instance/osListV3", method = RequestMethod.POST)
    Result<Map<String, Object>> getInstanceOsListV3(@RequestBody Map<String, Object> params,
                                                  @RequestParam(value = "moduleType", required = false) String moduleType);
    /**
     * 获取CI详情
     */
    @RequestMapping(value = "/cmdb/instance/detail", method = RequestMethod.GET)
    Map<String, Object> getInstanceDetail(@RequestParam("module_id") String moduleId,
                                          @RequestParam(value = "instance_id") String instanceId);

    /**
     * 新增CI
     */
    @RequestMapping(value = "/cmdb/instance/add", method = RequestMethod.POST)
    Map<String, Object> addInstance(@RequestParam("username") String username,@RequestBody Map<String, Object> instanceData);

    /**
     * 更新CI
     */
    @RequestMapping(value = "/cmdb/instance/update/{id}", method = RequestMethod.POST)
    Map<String, Object> updateInstance(@PathVariable("id") String id, @RequestParam("username") String username, @RequestBody Map<String, Object> instanceData);

    /**
     * 批量更新CI
     * {
     *   "querys": [
     *     {
     *       "filed": "",
     *       "operator": "",
     *       "value": ""
     *     }
     *   ],
     *   "update": {
     *     "codeId": "",
     *     "filed": "",
     *     "value": ""
     *   }
     * }
     */
    @RequestMapping(value = "/cmdb/instance/batchUpdateInstance", method = RequestMethod.POST)
    Map<String, Object> batchUpdateInstance(@RequestParam("username") String username,
                                            @RequestParam("moduleId") String moduleId,
                                            @RequestBody Map<String, Object> batchUpdate);

    /**
     * 获取需要批量更新的数量
     */
    @RequestMapping(value = "/cmdb/instance/batchUpdateCount", method = RequestMethod.POST)
    Integer batchUpdateCount( @RequestParam("moduleId") String moduleId,@RequestBody Map<String, Object> batchUpdate);
    /**
     * 根据资源池及IP地址查询设备信息
     * @param params 入参信息
     * @return
     */
    @RequestMapping(value = "/cmdb/instance/query/detail", method = RequestMethod.POST)
    Map<String, Object> queryInstanceDetail(@RequestBody Map<String, Object> params);

    /**
     * 删除CI
     * @param deleteInstance 需要删除的CI ID集合
     */
    @RequestMapping(value = "/cmdb/instance/delete", method = RequestMethod.DELETE)
    Map<String, Object> deleteInstance(@RequestParam("userName") String userName,
                                       @RequestBody CmdbDeleteInstance deleteInstance);

    /**
     * 获取指定设备ID的设备集合
     * @param deviceIds 设备ID集合
     */
    @RequestMapping(value = "/cmdb/instance/listInstanceBaseInfo", method = RequestMethod.POST)
    List<CmdbInstance> getInstanceByDeviceIds(@RequestBody String deviceIds);

    /**
     * 获取资源池树
     */
    @RequestMapping(value = "/cmdb/instance/getIdcTree", method = RequestMethod.GET)
    List<Map> getIdcTree();

    /**
     * 获取设备类型树
     */
    @RequestMapping(value = "/cmdb/instance/getDeviceClassTree", method = RequestMethod.GET)
    List<Map> getDeviceClassTree();

    /**
     * 根据资源池获取部门
     */
    @RequestMapping(value = "/cmdb/instance/getDepartmentsByIDC", method = RequestMethod.GET)
    List<String> getDepartmentsByIDC(@RequestParam("idcType") String idcType);

    /**
     * 根据多个资源池ID获取资源池列表
     * @param ids
     * @return
     */
    @RequestMapping(value = "/cmdb/instance/getIdcByIds", method = RequestMethod.GET)
    List<Map<String, String>> getIdcByIds(@RequestParam("ids") String ids);

    /**
     * 根据多个podID获取pod列表
     * @param ids
     * @return
     */
    @RequestMapping(value = "/cmdb/instance/getPodByIds", method = RequestMethod.GET)
    List<Map<String, String>> getPodByIds(@RequestParam("ids") String ids);

    /**
     * 根据多个机房ID获取机房列表
     * @param ids
     * @return
     */
    @RequestMapping(value = "/cmdb/instance/getRoomByIds", method = RequestMethod.GET)
    List<Map<String, String>> getRoomByIds(@RequestParam("ids") String ids);

    /**
     * 根据设备序列号查询设备信息
     * @param deviceSn 设备序列号
     * @return
     */
    @RequestMapping(value = "/cmdb/instance/queryDeviceByDeviceSn", method = RequestMethod.GET)
    CmdbInstance queryDeviceByDeviceSn(@RequestParam(value = "deviceSn") String deviceSn,@RequestParam(value = "deviceArea") String deviceArea);

    @RequestMapping(value = "/cmdb/instance/queryServiceCount", method = RequestMethod.GET)
    List<CmdbDeviceTypeCount> queryServiceCount(@RequestParam(value = "bizSystem", required = false) String bizSystem);

    @RequestMapping(value = "/cmdb/instance/queryServiceCountForKG", method = RequestMethod.GET)
    List<CmdbDeviceTypeCount> queryServiceCountForKG();

    @RequestMapping(value = "/cmdb/instance/getNetworkAndSafetyDeivce", method = RequestMethod.POST)
    List<Map<String,Object>> getNetworkAndSafetyDeivce(@RequestBody CmdbQueryInstance cmdbQueryInstance);

    /**
     * 导出设备列表
     * @param queryInstance 查询条件
     * @return
     */
    @RequestMapping(value = "/cmdb/instance/export", method = RequestMethod.POST)
    Map<String, String> exportInstance(@RequestBody Map<String, Object> params,
                                       @RequestParam(value = "moduleType", required = false) String moduleType);
    
    @RequestMapping(value = "/cmdb/instance/queryDeviceCountByIdctype", method = RequestMethod.GET)
    List<CmdbDeviceTypeByConditonCount> queryDeviceCountByIdctype(@RequestParam(value = "idcType", required = false)String idcType
    		,@RequestParam(value = "deviceType", required = false)String deviceType,
    		@RequestParam(value = "startTime", required = false)String startTime
    		,@RequestParam(value = "endTime", required = false)String endTime);
    
    @RequestMapping(value = "/cmdb/instance/queryDeviceCountByBizsystem", method = RequestMethod.GET)
    List<CmdbDeviceTypeByConditonCount> queryDeviceCountByBizsystem(@RequestParam(value = "bizSystem", required = false)String bizSystem,
    		@RequestParam(value = "idcType", required = false)String idcType,
    		@RequestParam(value = "deviceType", required = false)String deviceType,
    		@RequestParam(value = "startTime", required = false)String startTime,
    		@RequestParam(value = "endTime", required = false)String endTime,
    		@RequestParam(value = "sourceType", required = false)String sourceType);

    @RequestMapping(value = "/cmdb/instance/queryDeviceByIdcTypeAndIP", method = RequestMethod.POST)
    Map<String, Object> queryDeviceByIdcTypeAndIP(@RequestBody Map<String, Object> params);
    /**
     * 根据资源池及IP地址查询设备信息
     * @param idc 资源池
     * @param deviceIp 设备IP
     * @return
     */
    @RequestMapping(value = "/cmdb/instance/queryDeviceByRoomAndIP", method = RequestMethod.GET)
    CmdbInstance queryDeviceByRoomIdAndIP(@RequestParam(value = "idc", required = false) String idc,
                                          @RequestParam("deviceIp") String deviceIp);
    /**
     * 根据资源池及IP地址查询设备信息
     * @param idc 资源池
     * @param deviceIp 设备IP
     * @return
     */
    @RequestMapping(value = "/cmdb/instance/queryDeviceByRoomAndIP", method = RequestMethod.POST)
    CmdbInstance queryDeviceByRoomIdAndIP2(@RequestParam(value = "idc", required = false) String idc,
                                          @RequestParam("deviceIp") String deviceIp);

    /**
     * 查询CI基本属性信息列表
     * @param param 查询参数
     * */
    @RequestMapping(value = "/cmdb/instance/listBaseInfo", method = RequestMethod.POST)
    List<Map<String,Object>> getInstanceBaseInfo(@RequestBody Map<String, Object> param);

    /**
     * 获取块存储
     * */
    @RequestMapping(value = "/cmdb/instance/getBlockSize", method = RequestMethod.GET)
    Object getBlockSize();

    /**
     * 获取CI列表
     */
    @RequestMapping(value = "/cmdb/instance/listV3Count", method = RequestMethod.POST)
    Integer listV3Count(@RequestBody Map<String, Object> params,
                        @RequestParam(value = "moduleType", required = false) String moduleType);
}
