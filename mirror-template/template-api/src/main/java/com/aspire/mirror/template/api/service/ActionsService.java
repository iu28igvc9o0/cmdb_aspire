package com.aspire.mirror.template.api.service;

import com.aspire.mirror.template.api.dto.ActionsCreateRequest;
import com.aspire.mirror.template.api.dto.ActionsCreateResponse;
import com.aspire.mirror.template.api.dto.ActionsUpdateRequest;
import com.aspire.mirror.template.api.dto.ActionsUpdateResponse;
import com.aspire.mirror.template.api.dto.vo.ActionsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 动作对外暴露接口
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.api.service
 * 类名称:    ActionsService.java
 * 类描述:    动作对外暴露接口层
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 13:48:09
 */
@Api(value = "动作")
public interface ActionsService {
    /**
     * 创建动作信息
     *
     * @param actionsCreateRequest 动作创建请求对象
     * @return ActionsCreateResponse 动作创建响应对象
     */
    @PostMapping(value = "/v1/actions/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建动作", notes = "创建动作", response = ActionsCreateResponse.class, tags = {"/v1/actions"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = ActionsCreateResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ActionsCreateResponse.class)})
    ActionsCreateResponse createdActions(@RequestBody ActionsCreateRequest actionsCreateRequest);

    /**
     * 根据主键删除单条动作信息
     *
     * @param actionId 主键
     * @@return Result 返回结果
     */
    @DeleteMapping(value = "/v1/actions/{action_id}")
    @ApiOperation(value = "删除单条动作信息", notes = "删除单条动作信息",
            tags = {"/v1/actions"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> deleteByPrimaryKey(@PathVariable("action_id") String actionId);

    /**
     * 根据主键删除多条动作信息
     *
     * @param actionIds 主键（以逗号分隔）
     * @@return Result 返回结果
     */
    @DeleteMapping(value = "/v1/actions/batchDelete/{action_ids}")
    @ApiOperation(value = "删除多条动作信息", notes = "删除多条动作信息",
            tags = {"/v1/actions"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> deleteByPrimaryKeyArrays(@PathVariable("action_ids") String actionIds);

    /**
     * 根据主键修改动作信息
     *
     * @param actionsUpdateRequest 动作修改请求对象
     * @return ActionsUpdateResponse 动作修改响应对象
     */
    @PutMapping(value = "/v1/actions/{action_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "修改动作", notes = "修改动作", response = ActionsUpdateResponse.class, tags = {"/v1/actions"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = ActionsUpdateResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ActionsUpdateResponse.class)})
    ActionsUpdateResponse modifyByPrimaryKey(@PathVariable("action_id") String actionId, @RequestBody
            ActionsUpdateRequest actionsUpdateRequest);

    /**
     * 根据主键查找动作详情信息
     *
     * @param actionId 动作主键
     * @return ActionsVO 动作详情响应对象
     */
    @GetMapping(value = "/v1/actions/{action_id}")
    @ApiOperation(value = "详情", notes = "根据actionId获取动作详情", tags = {"/v1/actions"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = ActionsVO.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ActionsVO findByPrimaryKey(@PathVariable("action_id") String actionId);

    /**
     * 根据主键查询动作集合信息
     *
     * @param actionIds 动作主键(多个以逗号分隔)
     * @return List<ActionsVO> 动作查询响应对象
     */
    @GetMapping(value = "/v1/actions/list/{action_id}")
    @ApiOperation(value = "查询", notes = "查询", tags = {"/v1/actions"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    List<ActionsVO> listByPrimaryKeyArrays(@PathVariable("action_id") String actionIds);

}
