package com.aspire.ums.cmdb.serverProject;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.serverProject.payload.CmdbCabinet;
import com.aspire.ums.cmdb.serverProject.payload.CmdbCabinetRecord;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @Author: jiangxuwen
 * @Datetime: 2021/01/27 16:32
 */
@Api(value = "CMDB 机房项目-机柜接口类")
@RequestMapping("/cmdb/serverProject/cabinet")
public interface CmdbServerCabinatAPI {

    @RequestMapping(value = "/cabinet/create", method = RequestMethod.POST)
    @ApiOperation(value = "新增机柜信息", notes = "新增机柜信息", tags = { "CMDB serverProject API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo createCmdbCabinet(@RequestBody CmdbCabinet cmdbCabinet);

    @RequestMapping(value = "/cabinet/update", method = RequestMethod.POST)
    @ApiOperation(value = "修改机柜信息", notes = "修改机柜信息", tags = { "CMDB serverProject API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo updateCmdbCabinet(@RequestBody CmdbCabinet cmdbCabinet);

    @RequestMapping(value = "/cabinet/delete/{id}")
    @ApiOperation(value = "删除机柜信息", notes = "删除机柜信息", tags = { "CMDB serverProject API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo deleteCmdbCabinet(@PathVariable(name = "id") String id);

    @RequestMapping(value = "/cabinet/batchDelete", method = RequestMethod.POST)
    @ApiOperation(value = "批量删除机柜信息", notes = "批量删除机柜信息", tags = { "CMDB serverProject API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo batchDeleteCmdbCabinet(String ids);

    @RequestMapping(value = "/cabinet/query", method = RequestMethod.POST)
    @ApiOperation(value = "查询机柜信息", notes = "查询机柜信息", tags = { "CMDB serverProject API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo<List<CmdbCabinet>> queryCmdbCabinet(@RequestBody CmdbCabinet cmdbCabinet);

    @RequestMapping(value = "/cabinetRecord/create", method = RequestMethod.POST)
    @ApiOperation(value = "新增机柜申请信息", notes = "新增机柜申请信息", tags = { "CMDB serverProject API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo createCmdbcabinetRecord(@RequestBody CmdbCabinetRecord cmdbCabinet);

    @RequestMapping(value = "/cabinetRecord/update", method = RequestMethod.POST)
    @ApiOperation(value = "修改机柜信息", notes = "修改机柜信息", tags = { "CMDB serverProject API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo updateCmdbCabinetRecord(@RequestBody CmdbCabinetRecord cmdbCabinet);

    @RequestMapping(value = "/cabinetRecord/delete/{id}")
    @ApiOperation(value = "删除机柜信息", notes = "删除机柜信息", tags = { "CMDB serverProject API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo deleteCmdbCabinetRecord(@PathVariable(name = "id") String id);

    @RequestMapping(value = "/cabinetRecord/batchDelete", method = RequestMethod.POST)
    @ApiOperation(value = "批量删除机柜信息", notes = "批量删除机柜信息", tags = { "CMDB serverProject API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo batchDeleteCmdbCabinetRecord(@RequestParam(name = "ids") String ids);

    @RequestMapping(value = "/cabinetRecord/query", method = RequestMethod.POST)
    @ApiOperation(value = "查询机柜信息", notes = "查询机柜信息", tags = { "CMDB serverProject API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo<List<CmdbCabinetRecord>> queryCmdbCabinetRecord(@RequestBody CmdbCabinetRecord cmdbCabinet);

}
