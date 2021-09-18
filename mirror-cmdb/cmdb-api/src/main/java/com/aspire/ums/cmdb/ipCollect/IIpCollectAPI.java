package com.aspire.ums.cmdb.ipCollect;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.ipCollect.payload.entity.*;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceCreateRequest;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceDeleteRequest;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceUpdateRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/5/19 18:32
 */
@Api(value = "CMDB IP采集接口类")
@RequestMapping("/cmdb/ipCollect")
public interface IIpCollectAPI {

    @RequestMapping(value = "/instance/create", method = RequestMethod.POST)
    @ApiOperation(value = "接收新增推送接口", notes = "接收新增推送接口", tags = {"CMDB ipCollect API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ResultVo create(@RequestBody InstanceCreateRequest instanceCreateRequest);

    @RequestMapping(value = "/instance/update", method = RequestMethod.POST)
    @ApiOperation(value = "接收修改推送接口", notes = "接收修改推送接口", tags = {"CMDB ipCollect API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ResultVo update(@RequestBody InstanceUpdateRequest instanceUpdateRequest);

    @RequestMapping(value = "/instance/delete", method = RequestMethod.POST)
    @ApiOperation(value = "接收删除推送接口", notes = "接收删除推送接口", tags = {"CMDB ipCollect API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ResultVo delete(@RequestBody InstanceDeleteRequest instanceDeleteRequest);

    //=========================存活IP接口=============================
    @RequestMapping(value = "/survival/findList", method = RequestMethod.POST)
    @ApiOperation(value = "查询存活IP列表接口", notes = "查询存活IP列表接口", tags = {"CMDB ipCollect API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ResultVo<List<CmdbIpCollectResponse>> findListS(@RequestBody CmdbIpCollectRequest cmdbIpCollectRequest);

    @RequestMapping(value = "/survival/findPage", method = RequestMethod.POST)
    @ApiOperation(value = "查询存活IP列表及头栏统计接口", notes = "查询存活IP列表及头栏统计接口", tags = {"CMDB ipCollect API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CmdbIpCollectResult findPageS(@RequestBody CmdbIpCollectRequest cmdbIpCollectRequest);

    @RequestMapping(value = "/survival/getResource", method = RequestMethod.GET)
    @ApiOperation(value = "存活IP资源池下拉框接口", notes = "存活IP资源池下拉框接口", tags = {"CMDB ipCollect API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ResultVo getResourceS();

    @RequestMapping(value = "/survival/getSource", method = RequestMethod.GET)
    @ApiOperation(value = "存活IP来源下拉框接口", notes = "存活IP来源下拉框接口", tags = {"CMDB ipCollect API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ResultVo getSourceS();

    @RequestMapping(value = "/survival/export", method = RequestMethod.POST)
    @ApiOperation(value = "存活IP列表-导出", notes = "存活IP列表-导出", tags = {"CMDB ipCollect API"})
    void exportS(/*@RequestBody*/ CmdbIpCollectRequest cmdbIpCollectRequest, HttpServletResponse response);

    //=========================虚拟IP接口=============================
    @RequestMapping(value = "/fictitious/findList", method = RequestMethod.POST)
    @ApiOperation(value = "查询虚拟IP列表接口", notes = "查询虚拟IP列表接口", tags = {"CMDB ipCollect API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ResultVo<List<CmdbVipCollectEntity>> findListF(@RequestBody CmdbVipCollectRequest cmdbVipCollectRequest);

    @RequestMapping(value = "/fictitious/findPage", method = RequestMethod.POST)
    @ApiOperation(value = "查询虚拟IP列表接口", notes = "查询虚拟IP列表接口", tags = {"CMDB ipCollect API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<CmdbVipCollectEntity> findPageF(@RequestBody CmdbVipCollectRequest cmdbVipCollectRequest);

    @RequestMapping(value = "/fictitious/getResource", method = RequestMethod.GET)
    @ApiOperation(value = "虚拟IP资源池下拉框接口", notes = "虚拟IP资源池下拉框接口", tags = {"CMDB ipCollect API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ResultVo getResourceF();

    @RequestMapping(value = "/fictitious/getUserType", method = RequestMethod.GET)
    @ApiOperation(value = "虚拟IP使用类型下拉框接口", notes = "虚拟IP使用类型下拉框接口", tags = {"CMDB ipCollect API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ResultVo getUserTypeF();

    @RequestMapping(value = "/fictitious/export", method = RequestMethod.POST)
    @ApiOperation(value = "虚拟IP列表-导出", notes = "虚拟IP列表-导出", tags = {"CMDB ipCollect API"})
    void exportF(/*@RequestBody*/ CmdbVipCollectRequest cmdbVipCollectRequest, HttpServletResponse response);

    @GetMapping(value = "/fictitious/updateIpInfoByTask")
    @ApiOperation(value = "定时更新ip的存活信息到地址库", notes = "定时更新ip的存活信息到地址库", tags = {"CMDB ipCollect API"})
    ResultVo updateIpInfoByTask();

}
