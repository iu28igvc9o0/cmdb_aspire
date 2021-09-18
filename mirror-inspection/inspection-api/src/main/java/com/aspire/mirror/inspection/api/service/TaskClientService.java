package com.aspire.mirror.inspection.api.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.xml.ws.Response;

/** 
* @author ZhangSheng 
* @version 2018年8月12日 下午5:09:11 
* @describe 自动巡检任务Client端服务接口
*/
@Api(value = "scheduling_task")
public interface TaskClientService {
    /**
     * 添加任务调度
     * @param taskId 巡检任务主键
     * @return TaskVO 巡检任务详情响应对象
     */
	@GetMapping(value = "/v1/task/addScheduling/{task_id}")
	@ApiOperation(value = "调度", notes = "根据taskId添加巡检任务调度", tags = {"/v1/task"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "任务调度成功", response = ResponseEntity.class),
    @ApiResponse(code = 500, message = "任务调度失败")})
	ResponseEntity<String> addScheduling(@PathVariable("task_id") String taskId);
    
    /**
     * 停止巡检任务调度
     * @param taskId
     * @return
     */
    @GetMapping(value = "/v1/task/stopScheduling/{task_id}")
    @ApiOperation(value = "停止调度", notes = "根据taskId停止巡检任务调度", tags = {"/v1/task"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = ResponseEntity.class),
    @ApiResponse(code = 500, message = "内部错误")})
    ResponseEntity<String> stopScheduling(@PathVariable("task_id") String taskId);
    
    /**
     * 巡检任务调度
     * @param taskId
     * @return
     */
    @GetMapping(value = "/v1/task/execute/{task_id}")
    @ApiOperation(value = "立即执行", notes = "根据taskId立刻执行任务", tags = {"/v1/task"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = ResponseEntity.class),
    @ApiResponse(code = 500, message = "内部错误")})
    ResponseEntity<String> execute(@PathVariable("task_id") String taskId);
    
}
