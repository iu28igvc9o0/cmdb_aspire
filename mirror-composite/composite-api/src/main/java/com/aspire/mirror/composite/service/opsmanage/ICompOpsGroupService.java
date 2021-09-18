package com.aspire.mirror.composite.service.opsmanage;

import com.aspire.mirror.ops.api.domain.GeneralResponse;
import com.aspire.mirror.ops.api.domain.GroupObjectDetail;
import com.aspire.mirror.ops.api.domain.GroupRelationBatchAddReq;
import com.aspire.mirror.ops.api.domain.GroupRelationDetail;
import com.aspire.mirror.ops.api.domain.GroupRelationQueryModel;
import com.aspire.mirror.ops.api.domain.OpsGroup;
import com.aspire.mirror.ops.api.domain.OpsResource;
import com.aspire.mirror.ops.api.domain.OpsScript;
import com.aspire.mirror.ops.api.domain.PageListQueryResult;
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

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.opsmanage
 * 类名称:    ICompOpsGroupService.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/3/9 17:24
 * 版本:      v1.0
 */
@Api(value = "ops分组管理")
@RequestMapping(value = "/v1/ops-service/opsGroup/")
public interface ICompOpsGroupService {
    @PostMapping(value = "/querGroupTree", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询分组树", notes = "查询分组树", response = GeneralResponse.class, tags = {"Group service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = OpsScript.class),
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

    @PostMapping(value = "/exportGroupRelation", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "导出分组对象关联", notes = "导出分组对象关联", response = PageListQueryResult.class, tags = {"Group service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = PageListQueryResult.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    void exportGroupRelation(@RequestBody GroupRelationQueryModel groupRelationQueryModel, HttpServletResponse response);

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
}
