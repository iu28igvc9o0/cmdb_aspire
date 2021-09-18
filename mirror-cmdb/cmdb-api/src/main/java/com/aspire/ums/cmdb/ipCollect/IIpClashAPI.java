package com.aspire.ums.cmdb.ipCollect;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.ipCollect.payload.entity.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 冲突IP接口
 *
 * @Author: fanshenquan
 * @Datetime: 2020/5/27 20:36
 */
@Api(value = "CMDB 冲突IP接口类")
@RequestMapping("/cmdb/ipClash")
public interface IIpClashAPI {

    @RequestMapping(value = "/findPageList", method = RequestMethod.POST)
    @ApiOperation(value = "查询列表接口", notes = "查询列表接口", tags = {"CMDB ipClash API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ResultVo<List<CmdbIpClashFindPageResponse>> findPageList(@RequestBody CmdbIpClashFindPageRequest request);

    @RequestMapping(value = "/findPageAndTotal", method = RequestMethod.POST)
    @ApiOperation(value = "查询列表及头栏统计接口", notes = "查询列表及头栏统计接口", tags = {"CMDB ipClash API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CmdbIpClashResult findPage(@RequestBody CmdbIpClashFindPageRequest request);

    @RequestMapping(value = "/updateHandleStatus", method = RequestMethod.POST)
    @ApiOperation(value = "更新处理状态接口", notes = "更新处理状态接口", tags = {"CMDB ipClash API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作成功", response = ResultVo.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ResultVo updateHandleStatus(@RequestBody CmdbIpClashUpdateRequest request);


    @RequestMapping(value = "/createIpClash", method = RequestMethod.POST)
    @ApiOperation(value = "冲突IP推送接收接口", notes = "冲突IP推送接收接口", tags = {"CMDB ipClash API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作成功", response = ResultVo.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ResultVo createIpClash(@RequestBody Result<CmdbIpClashCreateRequest> request);

    @RequestMapping(value = "/rebuildClashList", method = RequestMethod.POST,produces={"application/json;charset=utf-8"})
    @ApiOperation(value = "冲突IP二次校验接口", notes = "冲突IP二次校验接口", tags = {"CMDB ipClash API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作成功", response = ResultVo.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ResultVo rebuildClashList(@RequestBody(required = false) List<CmdbIpClashRebuildRequest> list);

}
