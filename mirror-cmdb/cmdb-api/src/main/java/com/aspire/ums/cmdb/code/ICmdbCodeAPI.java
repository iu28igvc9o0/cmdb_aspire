package com.aspire.ums.cmdb.code;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.code.payload.CmdbCodeGroup;
import com.aspire.ums.cmdb.code.payload.CmdbCodeQuery;
import com.aspire.ums.cmdb.code.payload.CmdbSimpleCode;
import com.aspire.ums.cmdb.common.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICmdbCodeAPI
 * Author:   zhu.juwang
 * Date:     2019/5/13 18:43
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("/cmdb/code")
public interface ICmdbCodeAPI {

    /**
     * 获取码表信息
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation(value = "获取码表信息列表", notes = "获取码表信息列表", tags = {"CMDB Code API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<CmdbCode> list(@RequestBody CmdbCodeQuery query);

    /**
     * 获取码表信息
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ApiOperation(value = "获取码表信息列表", notes = "获取码表信息列表", tags = {"CMDB Code API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CmdbCode get(@RequestParam(value = "codeId", required = false) String codeId,
                             @RequestParam(value = "moduleCatalogId", required = false) String moduleCatalogId,
                             @RequestParam(value = "filedCode", required = false) String filedCode);

    /**
     * 获取码表信息
     */
    @RequestMapping(value = "/getCodeByCodeId", method = RequestMethod.GET)
    @ApiOperation(value = "获取码表信息列表", notes = "获取码表信息列表", tags = {"CMDB Code API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CmdbCode getCodeByCodeId(@RequestParam(value = "codeId") String codeId);

    /**
     * 新增码表信息
     * @param cmdbCode 码表信息
     */
    @RequestMapping(value = "/saveCode", method = RequestMethod.POST)
    @ApiOperation(value = "新增码表信息", notes = "新增码表信息", tags = {"CMDB Code API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "新增成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> saveCode(HttpServletResponse response, @RequestBody CmdbCode cmdbCode);

    /**
     * 修改码表信息
     * @param cmdbCode 码表信息
     */
    @RequestMapping(value = "/updateCode", method = RequestMethod.PUT)
    @ApiOperation(value = "修改码表信息", notes = "修改码表信息", tags = {"CMDB Code API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "修改成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> updateCode(HttpServletResponse response, @RequestBody CmdbCode cmdbCode);

    /**
     * 修改码表信息
     * @param cmdbCode 码表信息
     */
    @RequestMapping(value = "/deleteCode", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除码表信息", notes = "删除码表信息", tags = {"CMDB Code API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "修改成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> delete(@RequestBody CmdbCode cmdbCode);

    /**
     * 按照分组展示码表列表
     *
     */
    @RequestMapping(value = "/list/formatGroup", method = RequestMethod.GET)
    @ApiOperation(value = "按照分组展示码表列表", notes = "按照分组展示码表列表", tags = {"CMDB Code API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<CmdbCodeGroup> queryCodeListFormatGroup(@RequestParam(value = "catalogId", required = false) String catalogId);

    /**
     * 根据模型ID查询码表列表
     *
     */
    @RequestMapping(value = "/list/{moduleId}", method = RequestMethod.GET)
    @ApiOperation(value = "根据模型ID查询码表列表", notes = "根据模型ID查询码表列表", tags = {"CMDB Code API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<CmdbCode> getCodeListByModuleId(@PathVariable("moduleId") String moduleId);

    /**
     * 根据模型ID查询码表列表(自有)
     * @param moduleId 模型ID
     * @return
     */
    @RequestMapping(value = "/getSelfCodeListByModuleId", method = RequestMethod.GET)
    @ApiOperation(value = "根据模型ID查询码表列表", notes = "根据模型ID查询码表列表", tags = {"CMDB Code API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> getSelfCodeListByModuleId(@RequestParam("moduleId") String moduleId);
    /**
     * 修改码表字段长度
     *
     */
    @RequestMapping(value = "/change/codeLength", method = RequestMethod.GET)
    @ApiOperation(value = "根据模型ID查询码表列表", notes = "根据模型ID查询码表列表", tags = {"CMDB Code API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    String changeCodeLength(@RequestParam("filedCode") String filedCode, @RequestParam("changeLength") Integer changeLength);

    /**
     * 获取码表编码及名称集合
     *
     */
    @RequestMapping(value = "/getDistinctCodeList", method = RequestMethod.GET)
    @ApiOperation(value = "获取码表编码及名称集合", notes = "获取码表编码及名称集合", tags = {"CMDB Code API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, String>> getDistinctCodeList();

    /**
     * 验证码表填写的值是否正确
     * @param paramsList 请求参数 [{"codeId": "", "value": ""}]
     * @return
     */
    @RequestMapping(value = "/value/valid", method = RequestMethod.POST)
    @ApiOperation(value = "验证填写的码表值是否正确", notes = "验证填写的码表值是否正确", tags = {"CMDB Code API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Map<String, String>> validCodeValue(@RequestBody Map<String, Object> paramsList);

    /**
     * 自动审核码表中的值
     * @param paramsList 请求参数 [{"codeId": "CmdbCollectApproval实体"}]
     * @return
     */
    @RequestMapping(value = "/value/aprrove", method = RequestMethod.POST)
    @ApiOperation(value = "自动审核码表中的值", notes = "自动审核码表中的值", tags = {"CMDB Code API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Map<String, Object>> approveCodeValue(@RequestBody Map<String, Object> paramsList);

    /**
     * 验证码表编码和模型分组的唯一性
     * @param filedCode 码表编码
     * @param moduleCatalogId 模型分组ID
     * @return
     */
    @RequestMapping(value = "/validate/unique", method = RequestMethod.GET)
    @ApiOperation(value = "验证码表编码和模型分组的唯一性", notes = "验证码表编码和模型分组的唯一性", tags = {"CMDB Code API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject validateCmdbCodeUnique(@RequestParam("filedCode") String filedCode,
                                      @RequestParam("moduleCatalogId") String moduleCatalogId);

    /**
     * 获取配置项的数据源数据
     * @param params 入参
     * @return
     */
    @RequestMapping(value = "/getCodeDataSource", method = RequestMethod.POST)
    @ApiOperation(value = "获取配置项的数据源数据", notes = "获取配置项的数据源数据", tags = {"CMDB Code API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> getCodeDataSource(@RequestBody Map<String, Object> params);

    /**
     * 根据codeId查询父级级联code
     * @param codeIds 码表编码
     * @return
     */
    @RequestMapping(value = "/getCasParentCodes", method = RequestMethod.POST)
    @ApiOperation(value = "根据codeId查询父级级联code", notes = "根据codeId查询父级级联code", tags = {"CMDB Code API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<CmdbCode> getCasParentCodes(@RequestBody List<String> codeIds);


    /**
     *  获取简单的CmdbCode对象
     * @param entity
     * @return
     */
    @RequestMapping(value = "/getSimpleCode", method = RequestMethod.POST)
    @ApiOperation(value = "获取简单的CmdbCode对象", notes = "获取简单的CmdbCode对象", tags = {"CMDB Code API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = CmdbSimpleCode.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CmdbSimpleCode getByEntity(@RequestBody CmdbCode entity);
}
