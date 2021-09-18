package com.aspire.mirror.ops.api.service;

import com.aspire.mirror.ops.api.domain.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 分组管理功能
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api.service
 * 类名称:    IOpsGroupService.java
 * 类描述:    分组管理功能
 * 创建人:    JinSu
 * 创建时间:  2020/3/5 11:23
 * 版本:      v1.0
 */
@Api(value = "ops服务管理")
@RequestMapping(value = "/v1/ops-service/opsGroup/")
public interface IOpsGroupService {
    @PostMapping(value = "/querGroupTree", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询分组树", notes = "查询分组树", response = GeneralResponse.class, tags = {"Group service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = OpsGroup.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    List<OpsGroup> queryGroupTree(@RequestBody OpsGroup opsGroup);

    @PostMapping(value = "/saveGroup", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "保存分组信息", notes = "保存分组信息", response = GeneralResponse.class, tags = {"Group service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse saveGroup(@RequestBody OpsGroup opsGroup);

    @DeleteMapping(value = "/deleteGroup", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "删除分组信息", notes = "删除分组信息", response = GeneralResponse.class, tags = {"Group service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse deleteGroup(@RequestParam("group_id") Long groupId);

    @PostMapping(value = "/saveBatchGroupRelation", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "批量新增分组对象关联", notes = "批量新增分组对象关联", response = GeneralResponse.class, tags = {"Group service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse saveBatchGroupRelation(@RequestBody GroupRelationBatchAddReq groupRelationBatchAddReq);

    @PostMapping(value = "/querGroupRelationList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询分组对象关联", notes = "查询分组对象关联", response = PageListQueryResult.class, tags = {"Group service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = PageListQueryResult.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    PageListQueryResult<GroupRelationDetail> querGroupRelationList(@RequestBody GroupRelationQueryModel groupRelationQueryModel);

    @PostMapping(value = "/queryObjectList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询分组对象", notes = "查询分组对象", response = PageListQueryResult.class, tags = {"Group service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = PageListQueryResult.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    PageListQueryResult<GroupObjectDetail> queryObjectList(@RequestBody GroupRelationQueryModel groupRelationQueryModel);

    @DeleteMapping(value = "/deleteGroupRelation", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "删除分组关联", notes = "删除分组关联信息", response = GeneralResponse.class, tags = {"Group service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse deleteGroupRelation(@RequestParam("group_relation_ids") String groupRelationIds);

    @PostMapping(value = "/querOpsResourceTree", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询分组资源树", notes = "查询分组资源树", response = OpsGroup.class, tags = {"Group service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = OpsGroup.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    List<OpsResource> querOpsResourceTree(@RequestParam("objectType") String objectType);

    @PostMapping(value = "/queryAllChildGroup", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取所有子分组", notes = "获取所有子分组",
            response = List.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = List.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    List<String> queryAllChildGroup(@RequestBody ChildGroupQueryModel childGroupQueryModel);

}
