package com.migu.tsg.microservice.atomicservice.composite.service.registries;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.migu.tsg.microservice.atomicservice.composite.service.registries.payload.ListRegistriesResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.registries.payload.ListRegistryProjectResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.registries.payload.RegistryProjectCreateRequest;
import com.migu.tsg.microservice.atomicservice.composite.service.registries.payload.RegistryProjectCreateResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 镜像项目接口
 * 
 * @author longfeng
 *
 */
@RequestMapping("${version}/registries/{namespace}/{regisitry_name}")
@Api(value = "Composite Resource management(registry_project)")
public interface IRegistryProjectService {

	@ApiOperation(value = "根据镜像名获取项目列表", notes = "根据镜像名获取项目列表", response = ListRegistriesResponse.class, 
			tags = {
			"Composite Resource management(registry_project)" })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "list the project under registry", 
					response = ListRegistryProjectResponse.class) })
	@GetMapping(path = "/projects", produces = "application/json;charset=UTF-8")
	public List<ListRegistryProjectResponse> listProject(@PathVariable("namespace") String namespace,
			@PathVariable("regisitry_name") String registryName,
			@RequestParam(value = "project_name", required = false) String projectName);

	@ApiOperation(value = "新增项目", notes = "新增项目", response = ListRegistriesResponse.class, tags = {
			"Composite Resource management(registry_project)" })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "create project", response = RegistryProjectCreateResponse.class) })
	@PostMapping(path = "/projects", produces = "application/json;charset=UTF-8")
	public RegistryProjectCreateResponse createProject(@PathVariable("namespace") String namespace,
			@PathVariable("regisitry_name") String registryName,
			@RequestParam(value = "project_name", required = false) String projectName,
			@RequestBody RegistryProjectCreateRequest projectReq);

	@ApiOperation(value = "删除项目（只能删除空的）", notes = "删除项目", 
			tags = { "Composite Resource management(registry_project)" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "delete project, only can delete empty project") })
	@DeleteMapping(path = "/projects/{project_name}", produces = "application/json;charset=UTF-8")
	public ResponseEntity<String>  deleteProject(@PathVariable("namespace") String namespace,
			@PathVariable("regisitry_name") String registryName, @PathVariable("project_name") String projectName);

}
