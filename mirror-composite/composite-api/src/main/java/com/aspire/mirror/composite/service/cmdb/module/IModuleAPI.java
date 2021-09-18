package com.aspire.mirror.composite.service.cmdb.module;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IModuleAPI
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
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RequestMapping("${version}/cmdb/module")
public interface IModuleAPI {

    /**
     * 获取模型列表
     */
    @RequestMapping(value = "/getTree", method = RequestMethod.GET)
    @ApiOperation(value = "获取模型列表", notes = "获取模型列表", tags = {"CMDB Module API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = " 查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Module> getModuleTree(@RequestParam(value = "catalogId", required = false) String catalogId,
                               @RequestParam(value = "moduleType", required = false) String moduleType);

    /**
     * 获取模型列表
     * @param catalogId 模型分组ID
     * @param moduleId 模型ID
     */
    @RequestMapping(value = "/getModuleTreeByCatalogIdOrModuleId", method = RequestMethod.GET)
    @ApiOperation(value = "获取模型列表", notes = "获取模型列表", tags = {"CMDB Module API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Module> getTreeByCatalogIdOrModuleId(@RequestParam(value = "catalogId", required = false) String catalogId,
                                              @RequestParam(value = "moduleId", required = false) String moduleId);

    /**
     * 根据id获取模型详情
     */
    @RequestMapping(value = "/getModuleDetail", method = RequestMethod.GET)
    @ApiOperation(value = "获取模型详情", notes = "获取模型详情", tags = {"CMDB Module API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Module getModuleDetail(@RequestParam("moduleId") String moduleId);

    /**
     * 根据信息获取模型
     */
    @RequestMapping(value = "/getModuleSelective", method = RequestMethod.POST)
    @ApiOperation(value = "根据信息获取模型", notes = "根据信息获取模型", tags = {"CMDB Module API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Module> getModuleSelective(@RequestBody Module module);

    /**
     * 查询是模型是否存在
     */
    @RequestMapping(value = "/getModuleExist", method = RequestMethod.GET)
    @ApiOperation(value = "查询是模型是否存在", notes = "查询是模型是否存在", tags = {"CMDB Module API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> getModuleExist(@RequestParam("catalogId")String catalogId,
                                       @RequestParam("moduleCode") String moduleCode);

    /**
     * 新增模型
     */
    @RequestMapping(value = "/addModule", method = RequestMethod.POST)
    @ApiOperation(value = "新增模型", notes = "新增模型", tags = {"CMDB Module API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String,Object> addModule(@RequestParam("topCatalogId") String topCatalogId,@RequestBody Module module);

    /**
     * 新增模型
     */
    @RequestMapping(value = "/updateModule", method = RequestMethod.POST)
    @ApiOperation(value = "更新模型", notes = "更新模型", tags = {"CMDB Module API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "更新成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String,Object> updateModule(@RequestParam("topCatalogId") String topCatalogId,@RequestBody Module module);


    /**
     * 更新模型顺序
     */
    @RequestMapping(value = "/updateModuleSort", method = RequestMethod.PUT)
    @ApiOperation(value = "更新模型顺序", notes = "更新模型顺序", tags = {"CMDB Module API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "更新成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String,Object> updateModuleSort(@RequestParam("sortType") String sortType,
                                        @RequestParam("moduleId") String moduleId);

    /**
     * 添加模型字段
     */
    @RequestMapping(value = "/addModuleCode", method = RequestMethod.POST)
    @ApiOperation(value = "新增模型字段", notes = "新增模型字段", tags = {"CMDB Module API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String,Object> addModuleCode(@RequestBody Module module);

    /**
     * 删除模型
     */
    @RequestMapping(value = "/deleteModule/{moduleId}", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除模型", notes = "删除模型", tags = {"CMDB Module API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "删除成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String,Object> deleteModule(@PathVariable("moduleId") String moduleId);

    /**
     * 获取模型tag
     */
    @RequestMapping(value = "/getModuleTag/{moduleId}", method = RequestMethod.GET)
    @ApiOperation(value = "获取模型tag", notes = "获取模型tag", tags = {"CMDB Module API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<ModuleTag> getModuleTag(@PathVariable("moduleId") String moduleId);

    /**
     * 查询表数据
     */
    @RequestMapping(value = "/queryTableData", method = RequestMethod.POST)
    @ApiOperation(value = "查询表数据 ", notes = "查询表数据", tags = {"CMDB Module API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> queryTableData(@RequestBody Map<String, Object> queryData);

    /**
     * 导出模型配置模板
     */
    @RequestMapping(value = "/download/import/template/{moduleId}", method = RequestMethod.POST)
    @ApiOperation(value = "查询表数据 ", notes = "查询表数据", tags = {"CMDB Module API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> downloadImportTemplate(@PathVariable("moduleId") String moduleId,
                                               @RequestParam(value = "tableHeaderCode", required = false) String tableHeaderCode,
                                               HttpServletResponse response);

    /**
     * 根据主机ID获取模型数据
     */
    @RequestMapping(value = "/getModuleByInstanceId", method = RequestMethod.GET)
    @ApiOperation(value = "根据主机ID获取模型数据 ", notes = "根据主机ID获取模型数据", tags = {"CMDB Module API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    FullModule getModuleByInstanceId(@RequestParam("instance_id") String instanceId);

    /**
     * 根据模型ID, 查询模型数据
     */
    @RequestMapping(value = "/module/data", method = RequestMethod.POST)
    @ApiOperation(value = "根据模型ID, 查询模型数据", notes = "根据模型ID, 查询模型数据", tags = {"CMDB Module API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> getModuleData(@RequestBody Map<String, Object> queryParams,
                                            @RequestParam(value = "moduleId", required = false) String moduleId,
                                            @RequestParam(value = "moduleType", required = false) String moduleType);

    /**
     * 获取模型及父模型信息
     */
    @RequestMapping(value = "/getParentInfo", method = RequestMethod.GET)
    @ApiOperation(value = "获取模型及父模型信息", notes = "获取模型及父模型信息", tags = {"CMDB Module API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> getParentInfo(@RequestParam("module_id") String moduleId);

    /**
     * 获取引用模型字典数据
     */
    @RequestMapping(value = "/getRefModuleDict", method = RequestMethod.POST)
    @ApiOperation(value = "获取引用模型字典数据", notes = "获取引用模型字典数据", tags = {"CMDB Module API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> getRefModuleDict(@RequestParam("codeId") String refModuleCodeId);
    /**
     * 获取模型所有简单列信息
     * @param moduleId
     * @return
     */
    @RequestMapping(value = "/getModuleColumns", method = RequestMethod.GET)
    @ApiOperation(value = "获取模型所有简单列信息", notes = "获取模型所有简单列信息", tags = {"CMDB Module API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Map<String, String>> getModuleColumns(@RequestParam("moduleId") String moduleId);

    /**
     * 根据设备分类或设备类型获取模型信息
     * @param deviceType 设备类型
     * @return
     */
    @RequestMapping(value = "/getModuleIdByDeviceType", method = RequestMethod.GET)
    @ApiOperation(value = "根据设备分类或设备类型获取模型信息", notes = "根据设备分类或设备类型获取模型信息", tags = { "CMDB Module API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    Map<String, Object> getModuleIdByDeviceType(@RequestParam(value = "device_type") String deviceType);
}
