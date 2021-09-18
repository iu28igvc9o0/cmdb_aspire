package com.aspire.mirror.inspection.api.service ;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
* 巡检任务执行接口    <br/>
* Project Name:inspection-api
* File Name:TaskExecuteApiService.java
* Package Name:com.aspire.mirror.inspection.api.service
* ClassName: TaskExecuteApiService <br/>
* date: 2018年8月30日 下午3:51:49 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Api(value = "inspection_task_execute")
public interface TaskExecuteApiService {
	
    /**
     * 执行巡检任务
     * @param taskId 
     */
    @PutMapping(value = "/v1/taskExecute/inspection/{taskId}")
    @ApiOperation(value = "执行巡检任务", notes = "执行巡检任务", tags = {"/v1/taskExecute"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功")})
    void executeInspectionTask(@PathVariable("taskId") String taskId);
}
