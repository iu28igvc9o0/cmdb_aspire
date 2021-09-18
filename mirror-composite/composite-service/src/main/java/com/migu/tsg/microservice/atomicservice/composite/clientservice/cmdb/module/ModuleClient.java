package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.module;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ModuleClient
 * Author:   zhu.juwang
 * Date:     2019/5/13 17:24
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */

import com.aspire.ums.cmdb.module.payload.FullModule;
import com.aspire.ums.cmdb.module.payload.Module;
import com.aspire.ums.cmdb.module.payload.ModuleTag;
import com.migu.tsg.microservice.atomicservice.composite.config.FeignConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(value = "CMDB", configuration = FeignConfiguration.class)
public interface ModuleClient {

    /**
     * 获取模型列表
     */
    @RequestMapping(value = "/cmdb/module/getTree", method = RequestMethod.GET)
    List<Module> getModuleTree(@RequestParam(value = "catalogId", required = false) String catalogId,
                               @RequestParam(value = "moduleType", required = false) String moduleType);

    /**
     * 根据id获取模型详情
     */
    @RequestMapping(value = "/cmdb/module/getModuleDetail", method = RequestMethod.GET)
    Module getModuleDetail(@RequestParam("moduleId") String moduleId);

    /**
     * 根据信息获取模型
     */
    @RequestMapping(value = "/cmdb/module/getModuleSelective", method = RequestMethod.POST)
    List<Module> getModuleSelective(@RequestBody Module module);

    /**
     * 根据信息获取模型
     */
    @RequestMapping(value = "/cmdb/module/getModuleExist", method = RequestMethod.GET)
    Map<String,Object> getModuleExist(@RequestParam("catalogId") String catalogId,
                                @RequestParam("moduleCode") String moduleCode);

    /**
     * 新增模型
     */
    @RequestMapping(value = "/cmdb/module/addModule", method = RequestMethod.POST)
    Map<String,Object> addModule(@RequestParam("topCatalogId")String topCatalogId, @RequestBody Module module);

    /**
     * 新增模型
     */
    @RequestMapping(value = "/cmdb/module/updateModule", method = RequestMethod.POST)
    Map<String,Object> updateModule(@RequestParam("topCatalogId") String topCatalogId, @RequestBody Module module);

    /**
     * 更新模型顺序
     */
    @RequestMapping(value = "/cmdb/module/updateModuleSort", method = RequestMethod.PUT)
    Map<String,Object> updateModuleSort(@RequestParam("sortType") String sortType,
                                        @RequestParam("moduleId") String moduleId);


    /**
     * 添加模型字段
     */
    @RequestMapping(value = "/cmdb/module/addModuleCode", method = RequestMethod.POST)
    Map<String,Object> addModuleCode(@RequestBody Module module);

    /**
     * 删除模型
     */
    @RequestMapping(value = "/cmdb/module/deleteModule/{moduleId}", method = RequestMethod.DELETE)
    Map<String,Object> deleteModule(@PathVariable("moduleId") String moduleId);

    /**
     * 获取模型tag
     */
    @RequestMapping(value = "/cmdb/module/getModuleTag/{moduleId}", method = RequestMethod.GET)
    List<ModuleTag> getModuleTag(@PathVariable("moduleId") String moduleId);

    /**
     * 查询表数据
     */
    @RequestMapping(value = "/cmdb/module/queryTableData", method = RequestMethod.POST)
    List<Map<String, Object>> queryTableData(@RequestBody Map<String, Object> queryData);

    /**
     * 根据主机ID获取模型数据
     */
    @RequestMapping(value = "/cmdb/module/getModuleByInstanceId", method = RequestMethod.GET)
    FullModule getModuleByInstanceId(@RequestParam("instance_id") String instanceId);


    /**
     * 根据模型分组找到对应的模型信息
     */
    @RequestMapping(value = "/cmdb/module/getModuleByCatalogId", method = RequestMethod.GET)
    Module getModuleDetailByCatalogId(@RequestParam("catalogId") String catalogId);

    /**
     * 根据模型ID, 查询模型数据
     */
    @RequestMapping(value = "/cmdb/module/module/data", method = RequestMethod.POST)
    List<Map<String, Object>> getModuleData(@RequestBody Map<String, Object> queryParams,
                                            @RequestParam(value = "moduleId", required = false) String moduleId,
                                            @RequestParam(value = "moduleType", required = false) String moduleType);

    /**
     * 获取模型及父模型信息
     */
    @RequestMapping(value = "/cmdb/module/getParentInfo", method = RequestMethod.GET)
    Map<String, Object> getParentInfo(@RequestParam("module_id") String moduleId);

    /**
     * 获取模型列表
     * @param catalogId 模型分组ID
     * @param moduleId 模型ID
     */
    @RequestMapping(value = "/cmdb/module/getModuleTreeByCatalogIdOrModuleId", method = RequestMethod.GET)
    List<Module> getTreeByCatalogIdOrModuleId(@RequestParam(value = "catalogId", required = false) String catalogId,
                                              @RequestParam(value = "moduleId", required = false) String moduleId);

    /**
     * 获取引用模型字典数据
     */
    @RequestMapping(value = "/cmdb/module/getRefModuleDict", method = RequestMethod.POST)
    List<Map<String, Object>> getRefModuleDict(@RequestParam("codeId") String refModuleCodeId);

    /**
     * 获取模型所有简单列信息
     * @param moduleId
     * @return
     */
    @RequestMapping(value = "/cmdb/module/getModuleColumns", method = RequestMethod.GET)
    Map<String, Map<String, String>> getModuleColumns(@RequestParam("moduleId") String moduleId);

    /**
     * 根据设备类型获取模型信息
     *
     * @param deviceType 设备类型
     * @return
     */
    @RequestMapping(value = "/cmdb/module/getModuleIdByDeviceType", method = RequestMethod.GET)
    Map<String, Object> getModuleIdByDeviceType(@RequestParam(value = "device_type") String deviceType);
}
