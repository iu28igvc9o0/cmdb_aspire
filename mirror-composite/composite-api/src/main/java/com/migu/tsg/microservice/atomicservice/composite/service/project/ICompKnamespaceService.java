package com.migu.tsg.microservice.atomicservice.composite.service.project;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.migu.tsg.microservice.atomicservice.composite.service.common.payload.BaseResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.project.payload.CompNamespaceBindResourceResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.project.payload.CompNamespaceCreateRequest;
import com.migu.tsg.microservice.atomicservice.composite.service.project.payload.CompNamespaceResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.project.payload.CompNamespaceUpdateRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * endpoint to manage namespace Project Name:composite-api File Project
 * Name:composite-api File Name:ICompNamespaceService.java Package
 * Name:com.migu.tsg.microservice.atomicservice.composite.service.project
 * ClassName: ICompNamespaceService <br/>
 * date: 2017年10月8日 下午2:17:16 <br/>
 * endpoint to manage namespace Project Name:composite-api File
 * 
 * @author baiwp
 * @version
 * @since JDK 1.6
 */
@RequestMapping("${version}/regions")
@Api(value = "Composite Resource management(knamespace)", description = "Composite Resource management(knamespace)")
public interface ICompKnamespaceService {

    /**
     * 
     * listKnamespaces:获取命名空间列表. <br/>
     * 作者： baiwp
     * 
     * @param namespace
     * @param regionName
     * @return
     */
    @ApiOperation(value = "获取命名空间列表", notes = "获取命名空间列表", response = CompNamespaceResponse.class, tags = {
            "Composite Resource management(knamespaces) service API" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List knamespaces", response = CompNamespaceResponse.class) })
    @GetMapping(path = "/{namespace}/region/knamespaces", produces = "application/json;charset=UTF-8")
    public List<CompNamespaceResponse> listKnamespaces(@PathVariable("namespace") String namespace,
            @RequestParam(name="region_name", required=false) String regionName, 
            @RequestParam(name="name", required=false) String name,
            @RequestParam(name="project_name", required=false) String projectName);
    
    /**
     * 
     * createKnamespace:(创建命名空间). <br/>
     * 作者： baiwp
     * 
     * @param namespace
     * @param regionName
     * @param compReq
     * @return
     */
    @ApiOperation(value = "创建命名空间", notes = "创建命名空间", response = CompNamespaceResponse.class, tags = {
            "Composite Resource management(knamespaces) service API" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "create a knamespace", response = CompNamespaceResponse.class) })
    @PostMapping(path = "/{namespace}/{region_name}/knamespaces", produces = "application/json;charset=UTF-8")
    public CompNamespaceResponse createKnamespace(@PathVariable("namespace") String namespace,
            @PathVariable("regionName") String regionName, @RequestBody CompNamespaceCreateRequest compReq);

    /**
     * 
     * getKnamespaceDetail:(获取空间配额信息详情). <br/>
     * 作者： baiwp
     * 
     * @param namespace
     * @param knamespaceName
     * @return
     */
    @ApiOperation(value = "获取空间配额信息详情", notes = "获取空间配额信息详情", response = CompNamespaceResponse.class, tags = {
            "Composite Resource management(knamespaces) service API" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "get knamespace resource quote retail",
                    response = CompNamespaceResponse.class) })
    @GetMapping(path = "/{namespace}/region/knamespaces/{knamespace_name}", produces = "application/json;charset=UTF-8")
    public CompNamespaceResponse getKnamespaceDetail(@PathVariable("namespace") String namespace,
            @PathVariable("knamespace_name") String knamespaceName,
            @RequestParam(name="project_name", required=false) String projectName);

    /**
     * 
     * getKnamespaceResources:(获取空间资源信息). <br/>
     * 作者： baiwp
     * 
     * @param namespace
     * @param knamespaceName
     * @return
     */
    @ApiOperation(value = "获取空间资源信息", notes = "获取空间资源信息",
            response = CompNamespaceBindResourceResponse.class, tags = {
            "Composite Resource management(knamespaces) service API" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "get knamespace bind resources list",
                    response = CompNamespaceBindResourceResponse.class) })
    @GetMapping(path = "/{namespace}/region/knamespaces/{knamespace_name}/resources",
    produces = "application/json;charset=UTF-8")
    public List<CompNamespaceBindResourceResponse> getKnamespaceResources(@PathVariable("namespace") String namespace,
            @PathVariable("knamespace_name") String knamespaceName);

    /**
     * 
     * updateKnamespace:(更新命名空间). <br/>
     * 作者： baiwp
     * 
     * @param namespace
     * @param knamespaceName
     * @param compReq
     * @return
     */
    @ApiOperation(value = "更新命名空间", notes = "更新命名空间", response = CompNamespaceResponse.class, tags = {
            "Composite Resource management(knamespaces) service API" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "update knamespaces resources",
                    response = CompNamespaceResponse.class) })
    @PutMapping(path = "/{namespace}/region/knamespaces/{knamespace_name}", produces = "application/json;charset=UTF-8")
    public CompNamespaceResponse updateKnamespace(@PathVariable("namespace") String namespace,
            @PathVariable("knamespace_name") String knamespaceName, @RequestBody CompNamespaceUpdateRequest compReq);

    /**
     * 
     * deleteKnamespace:(删除命名空间). <br/>
     * 作者： baiwp
     * 
     * @param namespace
     * @param knamespaceName
     */
    @ApiOperation(value = "删除命名空间", notes = "删除命名空间", tags = {
            "Composite Resource management(knamespaces) service API" })
    @ApiResponses(value = { @ApiResponse(code = 200, 
    message = "delete knamespaces by name") })
    @DeleteMapping(path = "/{namespace}/region/knamespaces/{knamespace_name}",
    produces = "application/json;charset=UTF-8")
    public BaseResponse deleteKnamespace(@PathVariable("namespace") String namespace,
            @PathVariable("knamespace_name") String knamespaceName);
}
