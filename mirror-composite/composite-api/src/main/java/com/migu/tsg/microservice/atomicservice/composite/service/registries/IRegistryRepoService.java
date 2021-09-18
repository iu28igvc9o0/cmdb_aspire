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

import com.alibaba.fastjson.JSONObject;
import com.migu.tsg.microservice.atomicservice.composite.service.registries.payload.ListProjectRepoResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.registries.payload.ListRepoTagsResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.registries.payload.RegistryProjectRepoCreatRequest;
import com.migu.tsg.microservice.atomicservice.composite.service.registries.payload.RegistryProjectRepoCreateResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.registries.payload.RegistryProjectRepoUpdateReponse;
import com.migu.tsg.microservice.atomicservice.composite.service.registries.payload.RegistryProjectRepoUpdateRequest;
import com.migu.tsg.microservice.atomicservice.composite.service.registries.payload.TagArtifactResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 镜像仓库接口
 * 
 * @author lf
 *
 */
@RequestMapping("${version}/registries")
@Api(value = "镜像同步配置")
public interface IRegistryRepoService {
	@ApiOperation(value = "获取镜像仓库列表", notes = "获取镜像仓库列表", response = ListProjectRepoResponse.class, tags = {
			"Composite Resource management(registry_project_repositories)" })
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = ListProjectRepoResponse.class
					, message = "list repo under the project") })
	@GetMapping(path = "/{namespace}/{regisitry_name}/projects/{project_name}/repositories",
	produces = "application/json;charset=UTF-8")
	public List<ListProjectRepoResponse> listRepo(@PathVariable("namespace") String namespace,
			@PathVariable("regisitry_name") String registryName, @PathVariable("project_name") String projectName);

	@ApiOperation(value = "新增镜像仓库", notes = "新增镜像仓库", response = RegistryProjectRepoCreateResponse.class, tags = {
			"Composite Resource management(registry_project_repositories)" })
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = RegistryProjectRepoCreateResponse.class,
					message = "create repo under the project") })
	@PostMapping(path = "/{namespace}/{regisitry_name}/projects/{project_name}/repositories", 
	produces = "application/json;charset=UTF-8")
	public RegistryProjectRepoCreateResponse createRepo(@PathVariable("namespace") String namespace,
			@PathVariable("regisitry_name") String registryName, @PathVariable("project_name") String projectName,
			@RequestBody RegistryProjectRepoCreatRequest repoReq);

	@ApiOperation(value = "根据仓库名查询仓库", notes = "根据仓库名查询仓库", response = ListProjectRepoResponse.class, tags = {
			"Composite Resource management(registry_project_repositories)" })
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = ListProjectRepoResponse.class, 
					message = "get the repo under the project") })
	@GetMapping(path = "/{namespace}/{regisitry_name}/projects/{project_name}/repositories/{repo_name}", 
	produces = "application/json;charset=UTF-8")
	public ListProjectRepoResponse retrieveRepo(@PathVariable("namespace") String namespace,
			@PathVariable("regisitry_name") String registryName, @PathVariable("project_name") String projectName,
			@PathVariable("repo_name") String repoName);

	@ApiOperation(value = "删除仓库", notes = "删除仓库", tags = {
			"Composite Resource management(registry_project_repositories)" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "delete repo under the project") })
	@DeleteMapping(path = "/{namespace}/{regisitry_name}/projects/{project_name}/repositories/{repo_name}", 
	produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> deleteRepo(@PathVariable("namespace") String namespace,
			@PathVariable("regisitry_name") String registryName, @PathVariable("project_name") String projectName,
			@PathVariable("repo_name") String repoName);

	@ApiOperation(value = "修改仓库", notes = "修改仓库", response = RegistryProjectRepoUpdateReponse.class, tags = {
			"Composite Resource management(registry_project_repositories)" })
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = RegistryProjectRepoUpdateReponse.class, message = "update the repo") })
	@PutMapping(path = "/{namespace}/{regisitry_name}/projects/{project_name}/repositories/{repo_name}", 
	produces = "application/json;charset=UTF-8")
	public RegistryProjectRepoUpdateReponse updateRepo(@PathVariable("namespace") String namespace,
			@PathVariable("regisitry_name") String registryName, @PathVariable("project_name") String projectName,
			@PathVariable("repo_name") String repoName, @RequestBody RegistryProjectRepoUpdateRequest repoUpdateReq);

	@ApiOperation(value = "查找仓库标签", notes = "查找仓库标签", response = ListRepoTagsResponse.class, tags = {
			"Composite Resource management(registry_project_repositories)" })
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = ListRepoTagsResponse.class,
					message = "get tag list of repo under the project") })
	@GetMapping(path = "/{namespace}/{regisitry_name}/projects/{project_name}/repositories/{repo_name}/tags", 
	produces = "application/json;charset=UTF-8")
	public String listTags(@PathVariable("namespace") String namespace,
			@PathVariable("regisitry_name") String registryName, @PathVariable("project_name") String projectName,
			@PathVariable("repo_name") String repoName,
			@RequestParam("view_type") String viewType,
			@RequestParam("scan_results") String scanResults,
			@RequestParam("artifacts") String artifacts,
			@RequestParam("page_size") Integer pageSize,
			@RequestParam("page") Integer page);

	@ApiOperation(value = "获取标签详情", notes = "获取标签详情", response = JSONObject.class, tags = {
			"Composite Resource management(registry_project_repositories)" })
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = JSONObject.class, 
					message = "get tag info for the repo under the project") })
	@GetMapping(path = "/{namespace}/{regisitry_name}/projects/{project_name}/repositories/{repo_name}/tags/{tag_name}"
	, produces = "application/json;charset=UTF-8")
	public JSONObject retrieveTag(@PathVariable("namespace") String namespace,
			@PathVariable("regisitry_name")String registryName, @PathVariable("project_name") String projectName
			,@PathVariable("repo_name") String repoName,@PathVariable("tag_name")String tagName);

	/*@ApiOperation(value = "新增镜像扫描任务", notes = "新增镜像扫描任务", response = JSONObject.class, tags = {
			"Composite Resource management(registry_project_repositories)" })
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = JSONObject.class, message = "create analyze images's task") })
	@PostMapping(path = "/{namespace}/{regisitry_name}/projects/{project_name}/repositories/{repo_name}/tags/{tag_name}"
	, produces = "application/json;charset=UTF-8")
	public JSONObject createScanTask(String namespace, String registryName, String projectName, String repoName,
			String tagName);*/// TODO

	@ApiOperation(value = "获取镜像仓库列表", notes = "获取镜像仓库列表", response = ListProjectRepoResponse.class, tags = {
			"Composite Resource management(registry_project_repositories)" })
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = ListProjectRepoResponse.class, 
					message = "list the repo under regisitry") })
	@GetMapping(path = "/{namespace}/{regisitry_name}/repositories", produces = "application/json;charset=UTF-8")
	public List<ListProjectRepoResponse> listRepo2(@PathVariable("namespace") String namespace,
			@PathVariable("regisitry_name") String registryName);

	@ApiOperation(value = "新增镜像仓库", notes = "新增镜像仓库", response = RegistryProjectRepoCreateResponse.class, tags = {
			"Composite Resource management(registry_project_repositories)" })
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = RegistryProjectRepoCreateResponse.class, 
					message = "create one repo under regisitry") })
	@PostMapping(path = "/{namespace}/{regisitry_name}/repositories", produces = "application/json;charset=UTF-8")
	public RegistryProjectRepoCreateResponse createRepo2(@PathVariable("namespace") String namespace,
			@PathVariable("regisitry_name") String registryName, @RequestBody RegistryProjectRepoCreatRequest repoReq);

	@ApiOperation(value = "根据仓库名查询仓库", notes = "根据仓库名查询仓库", response = ListProjectRepoResponse.class, tags = {
			"Composite Resource management(registry_project_repositories)" })
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = ListProjectRepoResponse.class, message = "get the repo detail") })
	@GetMapping(path = "/{namespace}/{regisitry_name}/repositories/{repository_name}", 
	produces = "application/json;charset=UTF-8")
	public ListProjectRepoResponse retrieveRepo2(@PathVariable("namespace") String namespace,
			@PathVariable("regisitry_name") String registryName, @PathVariable("repository_name") String repoName);

	@ApiOperation(value = "删除仓库", notes = "删除仓库", tags = {
			"Composite Resource management(registry_project_repositories)" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "delete the repo") })
	@DeleteMapping(path = "/{namespace}/{regisitry_name}/repositories/{repository_name}", 
	produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> deleteRepo2(@PathVariable("namespace") String namespace,
			@PathVariable("regisitry_name") String registryName, @PathVariable("repository_name") String repoName);

	@ApiOperation(value = "修改仓库", notes = "修改仓库", response = RegistryProjectRepoUpdateReponse.class, tags = {
			"Composite Resource management(registry_project_repositories)" })
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = RegistryProjectRepoUpdateReponse.class, message = "update the repo") })
	@PutMapping(path = "/{namespace}/{regisitry_name}/repositories/{repository_name}", 
	produces = "application/json;charset=UTF-8")
	public RegistryProjectRepoUpdateReponse updateRepo2(@PathVariable("namespace") String namespace,
			@PathVariable("regisitry_name") String registryName, @PathVariable("repository_name") String repoName,
			@RequestBody RegistryProjectRepoUpdateRequest repoUpdateReq);

	@ApiOperation(value = "查找仓库标签", notes = "查找仓库标签", response = ListRepoTagsResponse.class, tags = {
			"Composite Resource management(registry_project_repositories)" })
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = ListRepoTagsResponse.class, 
					message = "list tag with different type under the repo") })
	@GetMapping(path = "/{namespace}/{regisitry_name}/repositories/{repository_name}/tags", 
	produces = "application/json;charset=UTF-8")
	public String listTags2(@PathVariable("namespace") String namespace
			,@PathVariable("regisitry_name") String registryName,
			@PathVariable("repository_name") String repoName,
			@RequestParam("view_type") String viewType,
			@RequestParam("scan_results") String scanResults,
			@RequestParam("artifacts") String artifacts,
			@RequestParam("page_size") Integer pageSize,
			@RequestParam("page") Integer page);

	@ApiOperation(value = "获取标签详情", notes = "获取标签详情", response = JSONObject.class, tags = {
			"Composite Resource management(registry_project_repositories)" })
	@ApiResponses(value = { @ApiResponse(code = 200, response = JSONObject.class, message = "get the detail of tag") })
	@GetMapping(path = "/{namespace}/{regisitry_name}/repositories/{repository_name}/tags/{tag_name}", 
	produces = "application/json;charset=UTF-8")
	public JSONObject retrieveTag2(@PathVariable("namespace") String namespace,
			@PathVariable("regisitry_name")String registryName,
			@PathVariable("repository_name") String repoName,@PathVariable("tag_name")String tagName);

	@ApiOperation(value = "删除标签", notes = "删除标签", tags = {
			"Composite Resource management(registry_project_repositories)" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "delete the tag") })
	@DeleteMapping(path = "/{namespace}/{regisitry_name}/repositories/{repository_name}/tags/{tag_name}", 
	produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> deleteTag(@PathVariable("namespace") String namespace,
			@PathVariable("regisitry_name")String registryName,
			@PathVariable("repository_name") String repoName,@PathVariable("tag_name")String tagName);
	
	@ApiOperation(value = "获取镜像产出物", notes = "获取镜像产出物", response = TagArtifactResponse.class, tags = {
	"Composite Resource management(registry_project_repositories)" })
	@ApiResponses(value = { @ApiResponse(code = 200, response = TagArtifactResponse.class, 
	message = "get artifacts of the tag.") })
	@GetMapping(path = "/{namespace}/{regisitry_name}/repositories/{repository_name}"
			+ "/tags/{tag_name}/artifacts", 
	produces = "application/json;charset=UTF-8")
	public TagArtifactResponse retrieveTagArtifacts2(@PathVariable("namespace") String namespace,
    		@PathVariable("regisitry_name") String registryName, @PathVariable("repository_name") String repoName,
            @PathVariable("tag_name") String tagName);
	
	@ApiOperation(value = "获取镜像产出物", notes = "获取镜像产出物", response = TagArtifactResponse.class, tags = {
	"Composite Resource management(registry_project_repositories)" })
	@ApiResponses(value = { @ApiResponse(code = 200, response = TagArtifactResponse.class, 
	message = "get artifacts of the tag.") })
	@GetMapping(path = "/{namespace}/{regisitry_name}/projects/{project_name}/repositories/{repo_name}"
			+ "/tags/{tag_name}/artifacts", 
	produces = "application/json;charset=UTF-8")
	public TagArtifactResponse retrieveTagArtifacts(@PathVariable("namespace") String namespace,
    		@PathVariable("regisitry_name") String registryName, @PathVariable("project_name") String projectName,
    		@PathVariable("repo_name") String repoName,
            @PathVariable("tag_name") String tagName);

}
