package com.migu.tsg.microservice.atomicservice.composite.service.registries;

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

import com.migu.tsg.microservice.atomicservice.composite.service.jobs.payload.JobHisLogsResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.registries.payload.ListHistoryResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.registries.payload.SyncHistoryCreateRequest;
import com.migu.tsg.microservice.atomicservice.composite.service.registries.payload.SyncHistoryDetailResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.registries.payload.SyncHistoryUpdateRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
/**
 * 镜像同步历史接口
 * @author longfeng
 *
 */
@RequestMapping("${version}/sync-registry/{namespace}")
@Api(value = "Composite Resource management(sync-registry-history)")
public interface ISyncHistoryService {
	@ApiOperation(value = "获取同步历史详情", notes = "获取同步历史详情", response = SyncHistoryDetailResponse.class,
			tags = {
			"Composite Resource management(sync-registry-history)" })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "get a build history detail", 
					response = SyncHistoryDetailResponse.class) })
	@GetMapping(path = "/histories/{history_id}", produces = "application/json;charset=UTF-8")
	public SyncHistoryDetailResponse retrieveHistories(@PathVariable("namespace") String namespace,
			@PathVariable("history_id") String historyId);
	
	@ApiOperation(value = "删除构建历史", notes = "删除构建历史",
			tags = {
			"Composite Resource management(sync-registry-history)" })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "delete a build history") })
	@DeleteMapping(path = "/histories/{history_id}", produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> deleteHistories(@PathVariable("namespace") String namespace,
			@PathVariable("history_id") String historyId);
	
	@ApiOperation(value = "新增一个同步任务", notes = "新增一个同步任务", response = SyncHistoryCreateRequest.class,
			tags = {"Composite Resource management(sync-registry-history)" })
	@ApiResponses(value = {@ApiResponse(code = 200,response = SyncHistoryCreateRequest.class
	, message = "create a sync task") })
	@PostMapping(path = "/histories", produces = "application/json;charset=UTF-8")
	public SyncHistoryCreateRequest createtHistories(@PathVariable("namespace") String namespace,
			@RequestBody SyncHistoryCreateRequest createReq);
	
	@ApiOperation(value = "修改同步历史状态", notes = "修改同步历史状态",
			tags = {"Composite Resource management(sync-registry-history)" })
	@ApiResponses(value = {@ApiResponse(code = 200,
					message = "update a build history's status") })
	@PutMapping(path = "/histories/{history_id}", produces = "application/json;charset=UTF-8")
	public String updateHistories(@PathVariable("namespace") String namespace,
			@PathVariable("history_id") String historyId, @RequestBody SyncHistoryUpdateRequest updateReq);
	
	@ApiOperation(value = "查询同步历史", notes = "查询同步历史", response = ListHistoryResponse.class,
			tags = {"Composite Resource management(sync-registry-history)" })
	@ApiResponses(value = {@ApiResponse(code = 200, message = "create a sync task"
	, response = ListHistoryResponse.class) })
	@GetMapping(path = "/histories", produces = "application/json;charset=UTF-8")
	public ListHistoryResponse listHistories(@PathVariable("namespace") String namespace,
			@RequestParam("registry_name") String registryName,
			@RequestParam("repository_project_name") String repositoryProjectName,
			@RequestParam("repo_name") String repoName, @RequestParam("config_name") String configName,
			@RequestParam("page") Integer page, @RequestParam("num_per_page") Integer numPerPage,
			@RequestParam("project_name") String projectName, @RequestParam("search") String search);
	
	@ApiOperation(value = "查询同步历史日志", notes = "查询同步历史日志", response = JobHisLogsResponse.class,
			tags = {"Composite Resource management(sync-registry-history)" })
	@ApiResponses(value = {@ApiResponse(code = 200, message = "create a sync task"
	, response = JobHisLogsResponse.class) })
	@GetMapping(path = "/histories/{history_id}/logs", produces = "application/json;charset=UTF-8")
	public List<JobHisLogsResponse> listLogs(@PathVariable("namespace") String namespace,
			@PathVariable("history_id") String historyId, @RequestParam("start_time") Long startTime,
			@RequestParam("end_time") Long endTime, @RequestParam("limit") Integer limit);
}
