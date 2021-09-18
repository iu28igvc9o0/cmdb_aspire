package com.migu.tsg.microservice.atomicservice.composite.service.jobs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.migu.tsg.microservice.atomicservice.composite.PaginateResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.jobs.payload.CreateJobConfigRequest;
import com.migu.tsg.microservice.atomicservice.composite.service.jobs.payload.CreateJobConfigResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.jobs.payload.JobConfigDetailResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.jobs.payload.JobConfigUpdateRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RequestMapping("${version}/job_configs")
@Api(value = "Composite Resource management(job-configs)")
public interface IJobConfigService {

	@ApiOperation(value = "查询任务配置列表", notes = "查询任务配置列表", response = PaginateResponse.class, tags = {
			"Composite Resource management(jobs-configs)" })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "get the job config list", response = PaginateResponse.class) })
	@GetMapping(path = "/{namespace}", produces = "application/json;charset=UTF-8")
	public PaginateResponse listJobConfig(@PathVariable("namespace") String namespace,
			@RequestParam("page_size") Integer pageSize,
			@RequestParam("page") Integer page,
			@RequestParam("project_name") String projectName,
			@RequestParam("search") String search);

	@ApiOperation(value = "新增任务配置", notes = "新增任务配置", response = CreateJobConfigResponse.class, tags = {
			"Composite Resource management(jobs-configs)" })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "create job config", response = CreateJobConfigResponse.class) })
	@PostMapping(path = "/{namespace}", produces = "application/json;charset=UTF-8")
	public CreateJobConfigResponse createJobConfig(@PathVariable("namespace") String namespace,
			@RequestParam("project_name") String projectName,
			@RequestBody CreateJobConfigRequest createJobConfigRequest);

	@ApiOperation(value = "查询一个任务配置", notes = "查询一个任务配置", response = JobConfigDetailResponse.class, tags = {
			"Composite Resource management(jobs-configs)" })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "get one job config", response = JobConfigDetailResponse.class) })
	@GetMapping(path = "/{namespace}/{config_name}", produces = "application/json;charset=UTF-8")
	public JobConfigDetailResponse retrieve(@PathVariable("namespace") String namespace,
			@PathVariable("config_name") String configName);

	@ApiOperation(value = "删除一个任务配置", notes = "删除一个任务配置", 
			tags = { "Composite Resource management(jobs-configs)" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "delete a job config") })
	@DeleteMapping(path = "/{namespace}/{config_name}", produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> deleteJobConfig(@PathVariable("namespace") String namespace,
			@PathVariable("config_name") String configName);

	@ApiOperation(value = "修改任务配置", notes = "修改任务配置",
			tags = { "Composite Resource management(jobs-configs)" })
	@ApiResponses(value = { @ApiResponse(code = 200,message = "update one job config") })
	@PutMapping(path = "/{namespace}/{config_name}", produces = "application/json;charset=UTF-8")
	public String updateJobConfig(@PathVariable("namespace") String namespace,
			@PathVariable("config_name") String configName, @RequestBody JobConfigUpdateRequest updateReq);

}
