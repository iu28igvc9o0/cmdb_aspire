package com.aspire.mirror.inspection.api.service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.aspire.mirror.inspection.api.dto.TaskObjectBatchCreateRequst;
import com.aspire.mirror.inspection.api.dto.TaskObjectDetailResponse;

import java.util.List;

/**
 * 任务设备服务
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.inspection.api.service
 * 类名称:    TaskObjectService.java
 * 类描述:    任务设备服务
 * 创建人:    JinSu
 * 创建时间:  2018/8/11 11:12
 * 版本:      v1.0
 */
@Api(value = "任务设备")
public interface TaskObjectService {

    /**
     * 根据任务ID删除任务设备信息
     * @param taskId 任务ID
     * @return ResponseEntity 返回结果
     */
    @DeleteMapping(value = "/v1/taskObject/deleteByTaskId/{task_id}")
    @ApiOperation(value = "删除单条巡检信息", notes = "删除单条巡检信息",tags = {"/v1/taskObject"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> deleteByTaskId(@PathVariable("task_id") String taskId);

    /**
     * 批量创建任务设备
     * @param batchCreateRequst 批量创建请求
     * @return ResponseEntity 批量创建请求返回
     */
    @PostMapping(value = "/v1/taskObject/batchCreate")
    @ApiOperation(value = "批量创建任务设备", notes = "批量创建任务设备",tags = {"/v1/taskObject"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> batchCreate(@RequestBody TaskObjectBatchCreateRequst batchCreateRequst);

    /**
     * 根据任务Id查询设备
     * @param taskId 任务Id
     * @return TaskObjectDetailResponse 任务设备返回
     */
    @GetMapping(value = "/v1/taskObject/findByTaskId/{task_id}")
    @ApiOperation(value = "查询任务设备", notes = "查询任务设备",tags = {"/v1/taskObject"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = TaskObjectDetailResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<TaskObjectDetailResponse> findByTaskId(@PathVariable("task_id") String taskId);

    /**
     * 根据任务Id查询设备
     * @param taskIds 任务Id集合
     * @return TaskObjectDetailResponse 任务设备返回
     */
    @GetMapping(value = "/v1/taskObject/findByTaskIds/{task_ids}")
    @ApiOperation(value = "查询任务设备", notes = "查询任务设备",tags = {"/v1/taskObject"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = TaskObjectDetailResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<TaskObjectDetailResponse> findByTaskIds(@PathVariable("task_ids") String taskIds);

    /**
     * 根据模板Id获取任务数
     * @param templateId 模板ID
     * @return int 任务设备数量
     */
    @GetMapping(value = "/v1/taskObject/findTaskCountByTemplateId/{template_id}")
    @ApiOperation(value = "根据模板Id获取任务数", notes = "根据模板Id获取任务数",tags = {"/v1/taskObject"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = Integer.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Integer findTaskCountByTemplateId(@PathVariable("template_id") String templateId);
}
