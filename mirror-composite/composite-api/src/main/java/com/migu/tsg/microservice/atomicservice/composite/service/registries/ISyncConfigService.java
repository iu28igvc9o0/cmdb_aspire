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

import com.migu.tsg.microservice.atomicservice.composite.service.registries.payload.ListConfigResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 镜像仓库同步配置接口
 * 
 * @author longfeng
 *
 */
@RequestMapping("${version}/sync-registry/{namespace}")
@Api(value = "Composite Resource management(sync-registry-configs)")
public interface ISyncConfigService {
	@ApiOperation(value = "获取镜像同步配置列表", notes = "获取镜像同步配置列表", response = ListConfigResponse.class, tags = {
			"Composite Resource management(sync-registry-configs)" })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "get the sync registry config list", 
					response = ListConfigResponse.class) })
	@GetMapping(path = "/configs", produces = "application/json;charset=UTF-8")
	public List<ListConfigResponse> listConfigs(@PathVariable("namespace") String namespace,
			@RequestParam(value = "project_name", required = false) String projectName);

	@ApiOperation(value = "获取镜像同步配置详情", notes = "获取镜像同步配置详情", response = ListConfigResponse.class, tags = {
			"Composite Resource management(sync-registry-configs)" })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "get the detail of on sync registry config", 
					response = ListConfigResponse.class) })
	@GetMapping(path = "/configs/{config_name}", produces = "application/json;charset=UTF-8")
	public ListConfigResponse retrieve(@PathVariable("namespace") String namespace,
			@PathVariable("config_name") String configName);

	@ApiOperation(value = "新增镜像同步配置", notes = "新增镜像同步配置", 
			tags = {"Composite Resource management(sync-registry-configs)" })
	@ApiResponses(value = { @ApiResponse(code = 200,
				message = "create the sync registry config") })
	@PostMapping(path = "/configs", produces = "application/json;charset=UTF-8")
	public String createSyncConfig(@PathVariable("namespace") String namespace,
			@RequestParam(value = "project_name", required = false) String requestProjectName,
			@RequestBody ListConfigResponse createReq);

	@ApiOperation(value = "删除镜像同步配置详情", notes = "删除镜像同步配置详情",
			tags = {"Composite Resource management(sync-registry-configs)" })
	@ApiResponses(value = { @ApiResponse(code = 200,message = "delete sync registry config") })
	@DeleteMapping(path = "/configs/{config_name}", produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> deleteSyncConfig(@PathVariable("namespace") String namespace,
			@PathVariable("config_name") String configName);

	@ApiOperation(value = "修改镜像同步配置详情", notes = "修改镜像同步配置详情", 
			tags = {"Composite Resource management(sync-registry-configs)" })
	@ApiResponses(value = { @ApiResponse(code = 200,
					message = "update sync registry config") })
	@PutMapping(path = "/configs/{config_name}", produces = "application/json;charset=UTF-8")
	public String updateSyncConfig(@PathVariable("namespace") String namespace,
			@PathVariable("config_name") String configName, @RequestBody ListConfigResponse updateReq);
}
