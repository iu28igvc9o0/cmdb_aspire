package com.aspire.ums.cmdb.serverProject;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.serverProject.payload.CmdbCabinetBill;
import com.aspire.ums.cmdb.serverProject.payload.CmdbCabinetBillConf;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @Author: jiangxuwen
 * @Datetime: 2021/01/27 16:32
 */
@Api(value = "CMDB 机房项目-机柜结算管理接口类")
@RequestMapping("/cmdb/serverProject/cabinetBill")
public interface CmdbServerCabinatBillAPI {

    @RequestMapping(value = "/billConfig/create", method = RequestMethod.POST)
    @ApiOperation(value = "新增机柜结算配置信息", notes = "新增机柜结算配置信息", tags = { "CMDB serverProject API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo createCmdbCabinetBillConfig(@RequestBody CmdbCabinetBillConf billConf);

    @RequestMapping(value = "/billConfig/update", method = RequestMethod.POST)
    @ApiOperation(value = "修改机柜结算配置信息", notes = "修改机柜结算配置信息", tags = { "CMDB serverProject API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo updateCmdbCabinetBillConfig(@RequestBody CmdbCabinetBillConf cmdbCabinet);

    @RequestMapping(value = "/billConfig/delete/{id}")
    @ApiOperation(value = "删除机柜结算配置信息", notes = "删除机柜结算配置信息", tags = { "CMDB serverProject API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo deleteCmdbCabinetBillConfig(@PathVariable(name = "id") String id);

    @RequestMapping(value = "/billConfig/batchDelete", method = RequestMethod.POST)
    @ApiOperation(value = "批量删除机柜结算配置信息", notes = "批量删除机柜结算配置信息", tags = { "CMDB serverProject API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo batchDeleteCmdbCabinetBillConfig(String ids);

    @RequestMapping(value = "/billConfig/query", method = RequestMethod.POST)
    @ApiOperation(value = "查询机柜结算配置信息", notes = "查询机柜结算配置信息", tags = { "CMDB serverProject API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo<List<CmdbCabinetBillConf>> queryCmdbCabinetBillConfig(@RequestBody CmdbCabinetBillConf cmdbCabinet);

    @RequestMapping(value = "/billReport/create", method = RequestMethod.POST)
    @ApiOperation(value = "新增机柜结算统计", notes = "新增机柜结算统计", tags = { "CMDB serverProject API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo createCmdbcabinetBillReport(@RequestBody CmdbCabinetBill bill);

    @RequestMapping(value = "/billReport/update", method = RequestMethod.POST)
    @ApiOperation(value = "修改机柜结算统计", notes = "修改机柜结算统计", tags = { "CMDB serverProject API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo updateCmdbCabinetBillReport(@RequestBody CmdbCabinetBill bill);

    @RequestMapping(value = "/billReport/delete/{id}")
    @ApiOperation(value = "删除机柜结算统计", notes = "删除机柜结算统计", tags = { "CMDB serverProject API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo deleteCmdbCabinetBillReport(@PathVariable(name = "id") String id);

    @RequestMapping(value = "/billReport/batchDelete", method = RequestMethod.POST)
    @ApiOperation(value = "批量删除机柜信息", notes = "批量删除机柜信息", tags = { "CMDB serverProject API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo batchDeleteCmdbCabinetBillReport(@RequestParam(name = "ids") String ids);

    @RequestMapping(value = "/billReport/query", method = RequestMethod.POST)
    @ApiOperation(value = "查询机柜结算统计", notes = "查询机柜结算统计", tags = { "CMDB serverProject API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo<List<CmdbCabinetBill>> queryCmdbCabinetBillReport(@RequestBody CmdbCabinetBill bill);

}
