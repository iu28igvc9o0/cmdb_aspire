package com.migu.tsg.microservice.atomicservice.composite.service.jobs;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.migu.tsg.microservice.atomicservice.composite.service.jobs.payload.CreateScheduleCallbackJobRequest;
import com.migu.tsg.microservice.atomicservice.composite.service.jobs.payload.CreateScheduleCallbackJobResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
* schedule_callback  create job
* Project Name:composite-api
* File Name:IScheduleCallbackJobsService.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.jobs
* ClassName: IScheduleCallbackJobsService <br/>
* date: 2017年10月13日 下午2:20:12 <br/>
* schedule_callback  create job
* @author weishuai
* @version 
* @since JDK 1.8
*/
@RequestMapping("${version}")
@Api(value = "Composite schedule callback job )")    
public interface IScheduleCallbackJobsService {

    /**
     * 创建schedule_callback_job.<br/>
     * 作者： weishuai
     * @param 
     * @return
     */
    @ApiOperation(value = "创建schedule_callback_job", notes = "创建schedule_callback_job", 
                  response = CreateScheduleCallbackJobResponse.class, 
                  tags = {"Composite schedule callback job API"})
    @ApiResponses(value = {@ApiResponse(code=201, 
                message = "Create a new schedule callback job", 
                response = CreateScheduleCallbackJobResponse.class)})
    @PostMapping(path = "/schedule_callback/jobs", 
                 produces = "application/json;charset=UTF-8")
    public CreateScheduleCallbackJobResponse craeateScheduleCallbackJobs(
            @RequestParam("config_uuid") String config_uuid,
            @RequestBody CreateScheduleCallbackJobRequest request);
}
