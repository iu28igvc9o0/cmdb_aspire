package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.code;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.code.payload.CmdbCodeGroup;
import com.aspire.ums.cmdb.code.payload.CmdbCodeQuery;
import com.aspire.ums.cmdb.common.Result;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbCodeController
 * Author:   zhu.juwang
 * Date:     2019/5/13 18:58
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "CMDB")
public interface CmdbCodeClient {
    /**
     * 获取码表信息
     */
    @PostMapping("/cmdb/code/list")
    Result<CmdbCode> list(@RequestBody CmdbCodeQuery query);

    /**
     * 获取码表信息
     */
    @RequestMapping(value = "/cmdb/code/get", method = RequestMethod.GET)
    CmdbCode get(@RequestParam(value = "codeId", required = false) String codeId,
                             @RequestParam(value = "moduleCatalogId", required = false) String moduleCatalogId,
                             @RequestParam(value = "filedCode", required = false) String filedCode);

    /**
     * 获取码表信息
     */
    @RequestMapping(value = "/cmdb/code/getCodeByCodeId", method = RequestMethod.GET)
    CmdbCode getCodeByCodeId(@RequestParam(value = "codeId") String codeId);

    /**
     * 验证码表编码或名称是否已经存在
     * return {"flag": true} / {"flag": false}
     */
    @RequestMapping(value = "/cmdb/code/valid", method = RequestMethod.GET)
    Map<String, String> valid(@RequestParam("filed") String filed, @RequestParam("value") String value);

    /**
     * 新增码表信息
     * @param cmdbCode 码表信息
     */
    @RequestMapping(value = "/cmdb/code/saveCode", method = RequestMethod.POST)
    Map<String, String> saveCode(@RequestBody CmdbCode cmdbCode);

    /**
     * 修改码表信息
     * @param cmdbCode 码表信息
     */
    @RequestMapping(value = "/cmdb/code/updateCode", method = RequestMethod.PUT)
    Map<String, String> updateCode(@RequestBody CmdbCode cmdbCode);

    /**
     * 修改码表信息
     * @param cmdbCode 码表集合
     */
    @RequestMapping(value = "/cmdb/code/deleteCode", method = RequestMethod.DELETE)
    Map<String, String> delete(@RequestBody CmdbCode cmdbCode);

    /**
     * 显示分组列表
     *
     */
    @RequestMapping(value = "/cmdb/code/group/list", method = RequestMethod.GET)
    List<String> queryGroupList();

    /**
     * 按照分组展示码表列表
     *
     */
    @RequestMapping(value = "/cmdb/code/list/formatGroup", method = RequestMethod.GET)
    List<CmdbCodeGroup> queryCodeListFormatGroup(@RequestParam(value = "catalogId", required = false) String catalogId);

    /**
     * 按照分组展示码表列表
     *
     */
    @RequestMapping(value = "/cmdb/code/list/{moduleId}", method = RequestMethod.GET)
    List<CmdbCode> getCodeListByModuleId(@PathVariable("moduleId") String moduleId);

    /**
     * 根据模型ID查询码表列表(自有)
     * @param moduleId 模型ID
     * @return
     */
    @RequestMapping(value = "/cmdb/code/getSelfCodeListByModuleId", method = RequestMethod.GET)
    Map<String, Object> getSelfCodeListByModuleId(@RequestParam("moduleId") String moduleId);
    /**
     * 获取码表编码及名称集合
     * @return
     */
    @RequestMapping(value = "/cmdb/code/getDistinctCodeList", method = RequestMethod.GET)
    List<Map<String, String>> getDistinctCodeList();

    /**
     * 验证码表填写的值是否正确
     * @param params 请求参数 {"codeId": "", "value": ""}
     * @return
     */
    @RequestMapping(value = "/cmdb/code/value/valid", method = RequestMethod.POST)
    Map<String, Map<String, String>> validCodeValue(@RequestBody Map<String, Object> params);

    /**
     * 验证码表编码和模型分组的唯一性
     * @param filedCode 码表编码
     * @param moduleCatalogId 模型分组ID
     * @return
     */
    @RequestMapping(value = "/cmdb/code/validate/unique", method = RequestMethod.GET)
    JSONObject validateCmdbCodeUnique(@RequestParam("filedCode") String filedCode,
                                      @RequestParam("moduleCatalogId") String moduleCatalogId);

    /**
     * 获取配置项的数据源数据
     * @param params 入参
     * @return
     */
    @RequestMapping(value = "/cmdb/code/getCodeDataSource", method = RequestMethod.POST)
    @ApiOperation(value = "获取配置项的数据源数据", notes = "获取配置项的数据源数据", tags = {"CMDB Code API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> getCodeDataSource(@RequestBody Map<String, Object> params);

    /**
     * 根据codeId查询父级级联code
     * @param codeIds 码表编码
     * @return
     */
    @RequestMapping(value = "/cmdb/code/getCasParentCodes", method = RequestMethod.POST)
    LinkedList<CmdbCode> getCasParentCodes(@RequestBody List<String> codeIds);
}
