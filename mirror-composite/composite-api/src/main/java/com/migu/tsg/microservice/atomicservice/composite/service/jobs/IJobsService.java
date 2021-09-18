package com.migu.tsg.microservice.atomicservice.composite.service.jobs;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.migu.tsg.microservice.atomicservice.composite.service.jobs.payload.JobHisLogsResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.jobs.payload.ListJobBaseResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.jobs.payload.ListJobResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.jobs.payload.StartJobRequest;
import com.migu.tsg.microservice.atomicservice.composite.service.jobs.payload.StartJobResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.jobs.payload.UpdateJobStatusRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RequestMapping("${version}/jobs")
@Api(value = "Composite Resource management(jobs)")
public interface IJobsService {

	@ApiOperation(value = "查询任务列表", notes = "查询任务列表", response = ListJobResponse.class, tags = {
			"Composite Resource management(jobs)" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "get all jobs", response = ListJobResponse.class) })
	@GetMapping(path = "/{namespace}", produces = "application/json;charset=UTF-8")
	public ListJobResponse listJob(@PathVariable("namespace") String namespace,
			@RequestParam(value = "page_size", required = false) Integer pageSize,
			@RequestParam(value = "config_name", required = false) String configName,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "project_name", required = false) String projectName,
			@RequestParam(value = "search", required = false) String search);

	@ApiOperation(value = "开始任务", notes = "开始任务", response = StartJobResponse.class, tags = {
			"Composite Resource management(jobs)" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "start job", response = StartJobResponse.class) })
	@PostMapping(path = "/{namespace}", produces = "application/json;charset=UTF-8")
	public StartJobResponse startJob(@PathVariable("namespace") String namespace,
			@RequestBody StartJobRequest createReq);

	@ApiOperation(value = "查询任务详情", notes = "查询任务详情", response = ListJobBaseResponse.class, tags = {
			"Composite Resource management(jobs)" })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "get a job detail message", response = ListJobBaseResponse.class) })
	@GetMapping(path = "/{namespace}/{job_uuid}", produces = "application/json;charset=UTF-8")
	public ListJobBaseResponse retrieveJob(@PathVariable("namespace") String namespace,
			@PathVariable("job_uuid") String jobUuid);

	@ApiOperation(value = "停止任务", notes = "停止任务",
			tags = { "Composite Resource management(jobs)" })
	@ApiResponses(value = { @ApiResponse(code = 200,message = "stop a job") })
	@PutMapping(path = "/{namespace}/{job_uuid}", produces = "application/json;charset=UTF-8")
	public String stopJob(@PathVariable("namespace") String namespace, @PathVariable("job_uuid") String jobUuid);

	@ApiOperation(value = "删除任务", notes = "删除任务", tags = { "Composite Resource management(jobs)" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "delete a job") })
	@DeleteMapping(path = "/{namespace}/{job_uuid}", produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> deleteJob(@PathVariable("namespace") String namespace
			, @PathVariable("job_uuid") String jobUuid);

	@ApiOperation(value = "查询任务日志", notes = "查询任务日志", response = JobHisLogsResponse.class, tags = {
			"Composite Resource management(jobs)" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "get job logs", response = JobHisLogsResponse.class) })
	@GetMapping(path = "/{namespace}/{job_uuid}/logs", produces = "application/json;charset=UTF-8")
	public List<JobHisLogsResponse> getLogs(@PathVariable("namespace") String namespace,
			@PathVariable("job_uuid") String jobUuid,
			@RequestParam(value = "start_time") Long startTime,
			@RequestParam(value = "end_time") Long endTime,
			@RequestParam(value = "limit") Integer limit);

	@ApiOperation(value = "修改任务状态", notes = "修改任务状态",
			tags = { "Composite Resource management(jobs)" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "update job status") })
	@PutMapping(path = "/{namespace}/{job_uuid}/status", produces = "application/json;charset=UTF-8")
	public String updateJobStatus(@PathVariable("namespace") String namespace, @PathVariable("job_uuid") String jobUuid,
			@RequestBody UpdateJobStatusRequest updateJobStatusRequest);
	
	@ApiOperation(value = "查询任务状态", notes = "查询任务状态",response = ResponseEntity.class,
			tags = { "Composite Resource management(jobs)" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "get job status", response = ResponseEntity.class) })
	@GetMapping(path = "/{namespace}/{job_uuid}/status", produces = "application/json;charset=UTF-8")
	public ResponseEntity<JSONObject> getJobStatus(@PathVariable("namespace") String namespace,
			@PathVariable("job_uuid") String jobUuid, @RequestBody UpdateJobStatusRequest updateJobStatusRequest);
}
