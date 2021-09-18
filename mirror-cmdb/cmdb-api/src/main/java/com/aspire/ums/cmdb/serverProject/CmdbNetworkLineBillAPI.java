package com.aspire.ums.cmdb.serverProject;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.serverProject.payload.CmdbNetworkLineBill;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @Author: jiangxuwen
 * @Datetime: 2021/01/27 16:32
 */
@Api(value = "CMDB 机房项目-网络线路管理接口类")
@RequestMapping("/cmdb/serverProject/networkBill")
public interface CmdbNetworkLineBillAPI {

    @RequestMapping(value = "/billReport/create", method = RequestMethod.POST)
    @ApiOperation(value = "新增网络线路结算统计", notes = "新增网络线路结算统计", tags = { "CMDB serverProject API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo createNetworkLineBill(@RequestBody CmdbNetworkLineBill bill);

    @RequestMapping(value = "/billReport/update", method = RequestMethod.POST)
    @ApiOperation(value = "修改网络线路结算统计", notes = "修改网络线路结算统计", tags = { "CMDB serverProject API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo updateNetworkLineBill(@RequestBody CmdbNetworkLineBill bill);

    @RequestMapping(value = "/billReport/delete/{id}")
    @ApiOperation(value = "删除网络线路结算统计", notes = "删除网络线路结算统计", tags = { "CMDB serverProject API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo deleteNetworkLineBill(@PathVariable(name = "id") String id);

    @RequestMapping(value = "/billReport/batchDelete", method = RequestMethod.POST)
    @ApiOperation(value = "批量删除网络线路信息", notes = "批量删除网络线路信息", tags = { "CMDB serverProject API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo batchDeleteNetworkLineBill(@RequestParam(name = "ids") String ids);

    @RequestMapping(value = "/billReport/query", method = RequestMethod.POST)
    @ApiOperation(value = "查询网络线路结算统计", notes = "查询网络线路结算统计", tags = { "CMDB serverProject API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo<List<CmdbNetworkLineBill>> queryNetworkLineBill(@RequestBody CmdbNetworkLineBill bill);

}
