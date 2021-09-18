package com.migu.tsg.microservice.atomicservice.composite.service.storage;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.migu.tsg.microservice.atomicservice.composite.service.storage.payload.CompClusterDetailResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * 存储卷中集群管理
 * Project Name:composite-api
 * File Name:ICompStorageClusterService.java
 * Package Name:com.migu.tsg.microservice.atomicservice.composite.service.storage
 * ClassName: ICompStorageClusterService <br/>
 * date: 2018年1月8日 上午11:15:34 <br/>
 * @author baiwp
 * @version 
 * @since JDK 1.6
 */
@RequestMapping("${version}/storage")
@Api(value = "storage clusetr service manager interface",
     description = "storage clusetr service manager interface")
public interface ICompStorageClusterService {

    /**
     * 
     * getClusterList:(获取存储卷集群列表). <br/>
     * 作者： baiwp
     * @return
     */
    @ApiOperation(value = "获取存储卷集群列表", notes = "获取存储卷集群列表",
            tags = {"Storage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Return the cluster list")})
    @GetMapping(path = "/{namespace}/cluster/", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    List<String> getClusterList (@PathVariable("namespace") String namespace,
                                 @RequestParam("region_id") String regionId);
    
    /**
     * 
     * getClusterSize:(获取存储卷集群节点大小). <br/>
     * 作者： baiwp
     * @param id
     * @return
     */
    @ApiOperation(value = "获取存储卷集群节点大小", notes = "获取存储卷集群节点大小",
            tags = {"Storage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Return the cluster size",
    response = Integer.class)})
    @GetMapping(path = "/{namespace}/cluster/{clutser_id}/size", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    Integer getClusterSize (@PathVariable("clutser_id") String id,
    		@PathVariable("namespace") String namespace,
                            @RequestParam("region_id") String regionId);
    
    /**
     * 
     * getClusterInfo:(获取存储卷集群详情). <br/>
     * 作者： baiwp
     * @param id
     * @return
     */
    @ApiOperation(value = "获取存储卷集群详情", notes = "获取存储卷集群详情",
            tags = {"Storage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Return the cluster detail data",
    response = CompClusterDetailResponse.class)})
    @GetMapping(path = "/{namespace}/cluster/{clutser_id}", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    CompClusterDetailResponse getClusterInfo(@PathVariable("clutser_id") String id,
    		@PathVariable("namespace") String namespace,
                                             @RequestParam("region_id") String regionId);
}
