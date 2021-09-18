package com.aspire.mirror.inspection.api.service ;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.inspection.api.dto.TaskCreateRequest;
import com.aspire.mirror.inspection.api.dto.TaskCreateResponse;
import com.aspire.mirror.inspection.api.dto.TaskDetailResponse;
import com.aspire.mirror.inspection.api.dto.TaskPageRequest;
import com.aspire.mirror.inspection.api.dto.TaskRunDetailResponse;
import com.aspire.mirror.inspection.api.dto.TaskRunRequest;
import com.aspire.mirror.inspection.api.dto.TaskUpdateRequest;
import com.aspire.mirror.inspection.api.dto.TaskUpdateResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 自动化巡检任务对外暴露接口
 *
 * 项目名称:  微服务运维平台
 * 包:       com.aspire.mirror.inspection.api.service
 * 类名称:    TaskService.java
 * 类描述:    自动化巡检任务对外暴露接口层
 * 创建人:    ZhangSheng
 * 创建时间:  2018-07-27 13:48:08
 */
@Api(value = "inspection_task")
public interface TaskService{
	
    /**
     * 根据主键删除单条巡检任务信息
     * @param taskId 主键
     * @return ResponseEntity 返回结果
     */
    @DeleteMapping(value = "/v1/task/delete/{task_ids}")
    @ApiOperation(value = "删除巡检任务信息", notes = "删除巡检任务信息",tags = {"/v1/task"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> deleteByPrimaryKey(@PathVariable("task_ids") String taskIds);
    
    /**
     * 根据主键修改巡检任务信息
     * @param taskUpdateRequest mod修改请求对象
     * @return TaskUpdateResponse inspection_task修改响应对象
     */
    @PutMapping(value="/v1/task/mod/{task_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value="修改",notes="修改巡检任务",response=TaskUpdateResponse.class,tags={ "/v1/task" })
    @ApiResponses(value = {
    @ApiResponse(code = 200, message = "返回", response =  TaskUpdateResponse.class),
    @ApiResponse(code = 500, message = "Unexpected error", response = TaskUpdateResponse.class) })
    TaskUpdateResponse modifyByPrimaryKey(@PathVariable("task_id") String taskId, @RequestBody TaskUpdateRequest taskUpdateRequest);
    
    /**
     * 创建巡检任务信息
     * @param taskCreateRequest 巡检任务创建请求对象
     * @return TaskCreateResponse 巡检任务创建响应对象
     */
    @PostMapping(value="/v1/task/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value="创建",notes="创建巡检任务",response=TaskCreateResponse.class,tags={ "/v1/task" })
    @ApiResponses(value = {
    @ApiResponse(code = 200, message = "返回", response =  TaskCreateResponse.class),
    @ApiResponse(code = 500, message = "Unexpected error", response = TaskCreateResponse.class) })
    TaskCreateResponse createdTask(@Validated @RequestBody TaskCreateRequest taskCreateRequest);
    
    /**
     * 根据主键查找巡检任务详情信息
     * @param taskId 巡检任务主键
     * @return TaskVO 巡检任务详情响应对象
     */
    @GetMapping(value = "/v1/task/get/{task_id}")
    @ApiOperation(value = "详情", notes = "根据taskId获取巡检任务详情", tags = {"/v1/task"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = TaskDetailResponse.class),
    @ApiResponse(code = 500, message = "内部错误")})
    TaskDetailResponse findByPrimaryKey(@PathVariable("task_id") String taskId);
    
    /**
     * 任务运行列表查询
     * @param taskPageRequest
     * @return
     */
    @PostMapping(value = "/v1/task/runList")
    @ApiOperation(value = "任务运行列表", notes = "查询巡检任务运行列表", tags = {"/v1/task"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
    @ApiResponse(code = 500, message = "内部错误")})
    PageResponse<TaskRunDetailResponse> runList(@Validated @RequestBody TaskRunRequest taskRunRequest);
    
    /**
     * 查询巡检任务分页列表
     * @param taskPageRequest:分页查询参数封装对象
     * @return PageResponse<TaskDetailResponse>:查询结果封装对象
     */
    @PostMapping(value = "/v1/task/list")
    @ApiOperation(value = "查询", notes = "查询巡检任务列表", tags = {"/v1/task"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
    @ApiResponse(code = 500, message = "内部错误")})
    PageResponse<TaskDetailResponse> list(final @Validated @RequestBody TaskPageRequest taskPageRequest);
}
