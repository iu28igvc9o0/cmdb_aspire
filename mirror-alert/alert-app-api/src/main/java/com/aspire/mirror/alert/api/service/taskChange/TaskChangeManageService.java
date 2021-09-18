package com.aspire.mirror.alert.api.service.taskChange;


import com.aspire.mirror.alert.api.dto.taskChange.TaskDetail;
import com.aspire.mirror.alert.api.dto.taskChange.TaskQueryRequest;
import com.aspire.mirror.alert.api.dto.taskChange.TaskRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RequestMapping("${version}/alerts/task")
@Api("变更计划管理")
public interface TaskChangeManageService {

    @PostMapping(value = "/addTask")
    @ApiOperation(value = "新建任务", notes = "新建任务", tags = {"Task Change Manage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    String addTask(@RequestBody TaskRequest compTaskRequest);

    @GetMapping(value = "/getTaskDetail")
    @ApiOperation(value = "获取任务详情", notes = "获取任务详情", tags = {"Task Change Manage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    TaskDetail getTaskDetail(@RequestParam("uuid") String uuid);

    @PostMapping(value = "/updateTask")
    @ApiOperation(value = "编辑任务", notes = "编辑任务", tags = {"Task Change Manage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    void updateTask(@RequestBody TaskRequest compTaskRequest);

    @DeleteMapping(value = "/deleteTask")
    @ApiOperation(value = "删除任务", notes = "删除任务", tags = {"Task Change Manage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    void deleteTask(@RequestParam("userName") String userName,
                    @RequestParam("uuid") String uuid);

    @PostMapping(value = "/editTaskMessage")
    @ApiOperation(value = "编辑任务反馈意见", notes = "编辑任务反馈意见", tags = {"Task Change Manage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    void editTaskMessage(@RequestBody Map<String, String> request);

    @PostMapping(value = "/startTask")
    @ApiOperation(value = "开始执行任务", notes = "开始执行任务", tags = {"Task Change Manage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    void startTask(@RequestBody TaskRequest compTaskRequest);

    @PostMapping(value = "/stopTask")
    @ApiOperation(value = "结束任务", notes = "结束任务", tags = {"Task Change Manage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    void stopTask(@RequestBody Map<String, String> request);

    @GetMapping(value = "/getTaskActionList")
    @ApiOperation(value = "获取任务操作列表", notes = "获取任务操作列表", tags = {"Task Change Manage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    List<Map<String, Object>> getTaskActionList(@RequestParam("uuid") String uuid);

    @GetMapping(value = "/getTaskMessageList")
    @ApiOperation(value = "获取任务反馈意见列表", notes = "获取任务反馈意见列表", tags = {"Task Change Manage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    Map<String, Object> getTaskMessageList(@RequestParam("uuid") String uuid,
                                           @RequestParam("pageNum") Integer pageNum,
                                           @RequestParam("pageSize") Integer pageSize);

    @PostMapping(value = "/exportTask")
    @ApiOperation(value = "导出任务", notes = "导出任务", tags = {"Task Change Manage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    List<Map<String, Object>> exportTask(@RequestBody Map<String, String> request);

    @GetMapping(value = "/taskNotify")
    @ApiOperation(value = "任务通知", notes = "任务通知", tags = {"Task Change Manage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    void taskNotify(@RequestParam("uuid") String uuid);

    @PostMapping(value = "/getTaskList")
    @ApiOperation(value = "获取任务列表", notes = "获取任务列表", tags = {"Task Change Manage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    Map<String, Object> getTaskList(@RequestBody TaskQueryRequest compTaskQueryRequest);

    @PostMapping(value = "/autoStopTask")
    @ApiOperation(value = "自动执行成功", notes = "自动执行成功", tags = {"Task Change Manage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    void autoStopTask();
}
