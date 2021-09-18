package com.aspire.ums.cmdb.serverProject;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.serverProject.payload.CmdbNetworkLine;
import com.aspire.ums.cmdb.serverProject.payload.CmdbNetworkLineRecord;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @Author: jiangxuwen
 * @Datetime: 2021/01/27 16:32
 */
@Api(value = "CMDB 机房项目-网络线路接口类")
@RequestMapping("/cmdb/serverProject/network")
public interface CmdbNetworkLineAPI {

    @RequestMapping(value = "/line/create", method = RequestMethod.POST)
    @ApiOperation(value = "新增网络线路", notes = "新增网络线路", tags = { "CMDB serverProject API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo createNetworkLine(@RequestBody CmdbNetworkLine networkLine);

    @RequestMapping(value = "/line/update", method = RequestMethod.POST)
    @ApiOperation(value = "修改网络线路", notes = "修改网络线路", tags = { "CMDB serverProject API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo updateNetworkLine(@RequestBody CmdbNetworkLine networkLine);

    @RequestMapping(value = "/line/delete/{id}")
    @ApiOperation(value = "删除网络线路", notes = "删除网络线路", tags = { "CMDB serverProject API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo deleteNetworkLine(@PathVariable(name = "id") String id);

    @RequestMapping(value = "/line/batchDelete", method = RequestMethod.POST)
    @ApiOperation(value = "批量删除网络线路", notes = "批量删除网络线路", tags = { "CMDB serverProject API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo batchDeleteNetworkLine(String ids);

    @RequestMapping(value = "/line/query", method = RequestMethod.POST)
    @ApiOperation(value = "查询网络线路", notes = "查询网络线路", tags = { "CMDB serverProject API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo<List<CmdbNetworkLine>> queryCmdbNetworkLine(@RequestBody CmdbNetworkLine networkLine);

    @RequestMapping(value = "/lineRecord/create", method = RequestMethod.POST)
    @ApiOperation(value = "新增网络线路申请信息", notes = "新增网络线路申请信息", tags = { "CMDB serverProject API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo createNetworkLineRecord(@RequestBody CmdbNetworkLineRecord record);

    @RequestMapping(value = "/lineRecord/update", method = RequestMethod.POST)
    @ApiOperation(value = "修改网络线路申请信息", notes = "修改网络线路申请信息", tags = { "CMDB serverProject API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo updateNetworkLineRecord(@RequestBody CmdbNetworkLineRecord record);

    @RequestMapping(value = "/lineRecord/delete/{id}")
    @ApiOperation(value = "删除网络线路申请信息", notes = "删除网络线路申请信息", tags = { "CMDB serverProject API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo deleteNetworkLineRecord(@PathVariable(name = "id") String id);

    @RequestMapping(value = "/lineRecord/batchDelete", method = RequestMethod.POST)
    @ApiOperation(value = "批量删除网络线路申请信息", notes = "批量删除网络线路申请信息", tags = { "CMDB serverProject API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo batchDeleteNetworkLineRecord(@RequestParam(name = "ids") String ids);

    @RequestMapping(value = "/lineRecord/query", method = RequestMethod.POST)
    @ApiOperation(value = "查询网络线路申请信息", notes = "查询网络线路申请信息", tags = { "CMDB serverProject API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo<List<CmdbNetworkLineRecord>> queryNetworkLineRecord(@RequestBody CmdbNetworkLineRecord record);

}
