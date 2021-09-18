package com.aspire.mirror.composite.service.taskChange;

import com.aspire.mirror.composite.payload.taskChange.CompTaskDetail;
import com.aspire.mirror.composite.payload.taskChange.CompTaskQueryRequest;
import com.aspire.mirror.composite.payload.taskChange.CompTaskRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RequestMapping("${version}/alerts/task")
@Api("变更计划管理")
public interface ICompTaskChangeManageService {

    @PostMapping(value = "/addTask")
    @ApiOperation(value = "新建任务", notes = "新建任务", tags = {"Task Change Manage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> addTask(@RequestBody CompTaskRequest compTaskRequest);

    @GetMapping(value = "/getTaskDetail")
    @ApiOperation(value = "获取任务详情", notes = "获取任务详情", tags = {"Task Change Manage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    CompTaskDetail getTaskDetail(@RequestParam("uuid") String uuid);

    @PostMapping(value = "/updateTask")
    @ApiOperation(value = "编辑任务", notes = "编辑任务", tags = {"Task Change Manage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> updateTask(@RequestBody CompTaskRequest compTaskRequest);

    @DeleteMapping(value = "/deleteTask")
    @ApiOperation(value = "删除任务", notes = "删除任务", tags = {"Task Change Manage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> deleteTask(@RequestParam("uuid") String uuid);

    @PostMapping(value = "/editTaskMessage")
    @ApiOperation(value = "编辑任务反馈意见", notes = "编辑任务反馈意见", tags = {"Task Change Manage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> editTaskMessage(@RequestBody Map<String, String> request);

    @PostMapping(value = "/startTask")
    @ApiOperation(value = "开始执行任务", notes = "开始执行任务", tags = {"Task Change Manage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> startTask(@RequestBody CompTaskRequest compTaskRequest);

    @PostMapping(value = "/stopTask")
    @ApiOperation(value = "结束任务", notes = "结束任务", tags = {"Task Change Manage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> stopTask(@RequestBody Map<String, String> request);

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
    void exportTask(@RequestBody Map<String, String> request, HttpServletResponse response);

    @GetMapping(value = "/taskNotify")
    @ApiOperation(value = "任务通知", notes = "任务通知", tags = {"Task Change Manage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> taskNotify(@RequestParam("uuid") String uuid);

    @PostMapping(value = "/getTaskList")
    @ApiOperation(value = "获取任务列表", notes = "获取任务列表", tags = {"Task Change Manage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    Map<String, Object> getTaskList(@RequestBody CompTaskQueryRequest compTaskQueryRequest);

    @PostMapping(value = "/downloadTaskTemplate")
    @ApiOperation(value = "下载任务模板", notes = "下载任务模板", tags = {"Task Change Manage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    void downloadTaskTemplate(HttpServletResponse response);

    @PostMapping(value = "/importTaskTemplate")
    @ApiOperation(value = "导入任务", notes = "导入任务", tags = {"Task Change Manage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> importTaskTemplate(@RequestParam("file") MultipartFile file,HttpServletRequest request);
}
