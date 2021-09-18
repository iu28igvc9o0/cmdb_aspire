package com.aspire.ums.cmdb.cmic;

import com.aspire.ums.cmdb.cmic.payload.CmdbModelTabsBase;
import com.aspire.ums.cmdb.cmic.payload.CmdbModelTabsReq;
import com.aspire.ums.cmdb.cmic.payload.CmdbModelTabsResp;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.common.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author :  fanshenquan
 * @CreateDate :  2020/5/12 17:43
 */
@Api(value = " CMDB资源模型tab标签管理接口类")
@RequestMapping("/cmdb/modelTabs")
public interface IModelTabsAPI {

    @RequestMapping(value = "/getModelTabsList", method = RequestMethod.POST)
    @ApiOperation(value = "获取模型tab标签列表", notes = "获取模型tab标签列表", tags = {"CMDB modelTabs API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<CmdbModelTabsResp> getModelTabsList(@RequestBody CmdbModelTabsReq cmdbModelTabsReq);

    @RequestMapping(value = "/getModelTabsById", method = RequestMethod.GET)
    @ApiOperation(value = "根据ID获取tab标签详情", notes = "根据ID获取tab标签详情", tags = {"CMDB modelTabs API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    ResultVo<CmdbModelTabsResp> getModelTabsById(@RequestParam(value = "tabsId") String tabsId);

    @RequestMapping(value = "/saveModelTabs", method = RequestMethod.POST)
    @ApiOperation(value = "新增或更新模型tab标签", notes = "新增或更新模型tab标签", tags = {"CMDB modelTabs API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    ResultVo saveModelTabs(@RequestBody CmdbModelTabsBase cmdbModelTabsBase);

    @RequestMapping(value = "/deleteModelTabsById", method = RequestMethod.GET)
    @ApiOperation(value = "根据tabsId删除模型tab标签", notes = "根据tabsId删除模型tab标签", tags = {"CMDB modelTabs API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    ResultVo deleteModelTabsById(@RequestParam(value = "idList") String[] idList);

}
