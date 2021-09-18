package com.aspire.mirror.composite.service.inspection;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.service.inspection.payload.*;
import com.migu.tsg.microservice.atomicservice.composite.service.common.payload.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 任务暴露接口
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.inspection
 * 类名称:    ICompTaskService.java
 * 类描述:    任务暴露接口
 * 创建人:    JinSu
 * 创建时间:  2018/8/10 14:00
 * 版本:      v1.0
 */
@Api(value = "监控项信息管理")
@RequestMapping("${version}/task")
public interface ICompTaskService {
    /**
     * 根据主键删除单条task信息
     *
     * @param taskId 主键
     * @return ResponseEntity 返回结果
     */
    @DeleteMapping(value = "/{task_id}")
    @ApiOperation(value = "删除单条巡检信息", notes = "删除单条巡检信息", tags = {"Task API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    BaseResponse deleteByPrimaryKey(@PathVariable("task_id") String taskId);

    /**
     * 根据主键修改task信息
     *
     * @param taskUpdateRequest inspection_task修改请求对象
     * @return TaskUpdateResponse inspection_task修改响应对象
     */
    @PutMapping(value = "/{task_id}", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "修改任务", notes = "修改任务", response = CompTaskUpdateResponse.class, tags = {"Task API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = CompTaskUpdateResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = CompTaskUpdateResponse.class)})
    CompTaskUpdateResponse modifyByPrimaryKey(@PathVariable("task_id") String taskId, @RequestBody CompTaskUpdateRequest taskUpdateRequest);

    /**
     * 创建task信息
     *
     * @param taskCreateRequest task创建请求对象
     * @return TaskCreateResponse task创建响应对象
     */
    @PostMapping(value = "insert", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "创建task", notes = "创建task", response = CompTaskCreateResponse.class, tags = {"Task API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = CompTaskCreateResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = CompTaskCreateResponse.class)})
    CompTaskCreateResponse createdTask(@RequestBody CompTaskCreateRequest taskCreateRequest);

    /**
     * 根据主键查找inspection_task详情信息
     *
     * @param taskId inspection_task主键
     * @return TaskVO inspection_task详情响应对象
     */
    @GetMapping(value = "/{task_id}")
    @ApiOperation(value = "详情", notes = "根据taskId获取inspection_task详情", tags = {"Task API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = CompTaskDetailResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CompTaskDetailResponse findByPrimaryKey(@PathVariable("task_id") String taskId);

    /**
     * 查询巡检任务分页列表
     *
     * @param taskPageRequest:分页查询参数封装对象
     * @return PageResponse<TaskDetailResponse>:查询结果封装对象
     */
    @PostMapping(value = "/list")
    @ApiOperation(value = "查询", notes = "查询", tags = {"Task API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    PageResponse<CompTaskListResponse> list(@RequestBody CompTaskPageRequest taskPageRequest);


    /**
     * 获取用户信息
     */
    @GetMapping(value = "/userList/{namespace}")
    @ApiOperation(value = "查询用户", notes = "查询", tags = {"Task API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    PageResponse<CompUserDetailResponse> getUserList(@PathVariable("namespace") String namespace, @RequestParam(value = "username", required = false) String username,
                                             @RequestParam(value = "order_by", required = false, defaultValue = "-createTime") String orderBy,
                                             @RequestParam(value = "page_size", required = false, defaultValue = "20") Integer pageSize,
                                             @RequestParam(value = "page", required = false, defaultValue = "1") Integer page);
    /**
     * 任务调度
     *
     * @param taskId:任务ID
     * @return BaseResponse:查询结果封装对象
     */
    @PostMapping(value = "/addScheduling/{task_id}")
    @ApiOperation(value = "任务调度新增", notes = "任务调度新增", tags = {"Task API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    BaseResponse addScheduling(@PathVariable("task_id") String taskId);

    /**
     * 任务立即执行
     *
     * @param taskId:任务ID
     * @return BaseResponse:查询结果封装对象
     */
    @PostMapping(value = "/execute/{task_id}")
    @ApiOperation(value = "任务调度新增", notes = "任务调度新增", tags = {"Task API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    BaseResponse exec(@PathVariable("task_id") String taskId);
    /**
     * 停止任务调度
     *
     * @param taskId:任务ID
     * @return BaseResponse:查询结果封装对象
     */
    @PostMapping(value = "/stopScheduling/{task_id}")
    @ApiOperation(value = "任务调度停止", notes = "任务调度停止", tags = {"Task API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    BaseResponse stopScheduling(@PathVariable("task_id") String taskId);

    @GetMapping(value="/getItemListByTaskId/{task_id}")
    @ApiOperation(value = "获取巡检项列表", notes = "获取巡检项列表", tags = {"Task API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    List<CompItemData> getItemListByTaskId (@PathVariable("task_id") String taskId);
}
