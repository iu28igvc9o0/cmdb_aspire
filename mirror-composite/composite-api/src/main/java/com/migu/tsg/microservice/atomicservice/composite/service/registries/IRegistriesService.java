package com.migu.tsg.microservice.atomicservice.composite.service.registries;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.alibaba.fastjson.JSONObject;
import com.migu.tsg.microservice.atomicservice.composite.service.registries.payload.CreateRegistryResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.registries.payload.ListRegistriesResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.registries.payload.Notification;
import com.migu.tsg.microservice.atomicservice.composite.service.registries.payload.RegisterCreateRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 镜像管理接口
 * 
 * @author longfeng
 *
 */
@Api(value = "Composite Resource management(registry)", description = "Composite Resource management(registry)")
public interface IRegistriesService {
	@ApiOperation(value = "获取根账号下面的镜像列表（私有+公有）", notes = "获取镜像列表", response = ListRegistriesResponse.class,
			tags = {
			"Composite Resource management(registry)" })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "List Registries resources for the namespace", 
					response = ListRegistriesResponse.class) })
	@GetMapping(path = "${version}/registries/{namespace}", produces = "application/json;charset=UTF-8")
	public List<ListRegistriesResponse> listRegistries(@PathVariable("namespace") String namespace);

	/**
	 * 删除镜像
	 * 
	 * @param namespace
	 * @param regionId
	 */
	@ApiOperation(value = "删除集群下面的私有镜像", notes = "根据uuids删除镜像", 
			tags = { "Composite Resource management(registry)" })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "delete Registries resources for the namespace in the region") })
	@DeleteMapping(path = "${version}/registries/{namespace}", produces = "application/json;charset=UTF-8")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<String> removeRigistry(@PathVariable("namespace") String namespace,
			@RequestParam(value="region_id") String regionId);

	@ApiOperation(value = "新增镜像", notes = "新增镜像", response = CreateRegistryResponse.class, tags = {
			"Composite Resource management(registry)" })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "create Registries resources ", 
					response = CreateRegistryResponse.class) })
	@PostMapping(path = "${version}/registries/{namespace}", produces = "application/json;charset=UTF-8")
	@ResponseStatus(HttpStatus.CREATED)
	public CreateRegistryResponse createRegistry(@PathVariable("namespace") String namespace,
			@RequestBody RegisterCreateRequest registryReq);
	
	@ApiOperation(value = "镜像上传下载事件", notes = "镜像上传下载事件", response = CreateRegistryResponse.class, tags = {
	"Composite Resource management(registry notification)" })
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "create Registriy notification ", 
				response = CreateRegistryResponse.class) })
	@PostMapping(path = "${version}/notification/{namespace}/{private_registry_name}"
	, produces = "application/json;charset=UTF-8")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> notificationPrivateRegistry(@PathVariable("namespace") String namespace,
			@PathVariable("private_registry_name") String privateRegistryName, @RequestBody Notification notification);
	
	@ApiOperation(value = "镜像鉴权", notes = "镜像鉴权", response = List.class, tags = {
	"Composite Resource management(registry notification)" })
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "registry auth", 
				response = List.class) })
	@GetMapping(path = "${version}/docker_auth/token/{namespace}/{private_registry_name}"
	, produces = "application/json;charset=UTF-8")
	public JSONObject registryAuth(@PathVariable("namespace") String namespace,
			@PathVariable("private_registry_name") String privateRegistryName,@RequestParam("service") String service
			,@RequestParam("scope") String scope);

}
