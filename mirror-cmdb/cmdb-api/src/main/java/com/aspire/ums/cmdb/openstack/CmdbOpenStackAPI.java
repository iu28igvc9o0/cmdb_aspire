package com.aspire.ums.cmdb.openstack;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceCreateRequest;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceDeleteRequest;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceRelationCreateRequest;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceRelationDeleteRequest;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceUpdateRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @Author: jiangxuwen
 * @Datetime: 2020/11/16 16:32
 */
@Api(value = "CMDB openStack接口类")
@RequestMapping("/cmdb/openStack")
public interface CmdbOpenStackAPI {

    @RequestMapping(value = "/instance/create", method = RequestMethod.POST)
    @ApiOperation(value = "新增openStack", notes = "新增openStack", tags = { "CMDB openStack API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo createInstance(@RequestBody InstanceCreateRequest instanceCreateRequest);

    @RequestMapping(value = "/instance/update", method = RequestMethod.POST)
    @ApiOperation(value = "修改openStack", notes = "修改openStack", tags = { "CMDB openStack API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo updateInstance(@RequestBody InstanceUpdateRequest instanceUpdateRequest);

    @RequestMapping(value = "/instance/delete", method = RequestMethod.POST)
    @ApiOperation(value = "删除openStack", notes = "删除openStack", tags = { "CMDB openStack API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo deleteInstance(@RequestBody InstanceDeleteRequest instanceDeleteRequest);

    @RequestMapping(value = "/instance/relation/create", method = RequestMethod.POST)
    @ApiOperation(value = "新增openStack实例关联关系", notes = "新增openStack实例关联关系", tags = { "CMDB openStack API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo createInstanceRel(@RequestBody InstanceRelationCreateRequest instanceCreateRequest);

    @RequestMapping(value = "/instance/relation/delete", method = RequestMethod.POST)
    @ApiOperation(value = "删除openStack实例关联关系", notes = "删除openStack实例关联关系", tags = { "CMDB openStack API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "操作成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    ResultVo deleteInstanceRel(@RequestBody InstanceRelationDeleteRequest instanceDeleteRequest);

}
