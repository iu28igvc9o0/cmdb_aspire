package com.migu.tsg.microservice.atomicservice.composite.service.storage;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.migu.tsg.microservice.atomicservice.composite.service.storage.payload.VolumeCreateResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.storage.payload.CompVolumeDetailResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.storage.payload.VolumeListByNameResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.storage.payload.VolumeListResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.storage.payload.VolumesAddRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
/**
* 项目名称: composite-api
* 包: com.migu.tsg.microservice.atomicservice.composite.service.storage
* 类名称: ICompStorageSchemasService.java
* 类描述:
* 创建人: ZhangRiYue
* 创建时间: 2017年9月11日上午11:20:52
* 版本: v1.0
 */
@RequestMapping("${version}/storage")
@Api(value = "storage service manager interface",
     description = "storage service manager interface")
public interface ICompStorageVolumesService {
    
    /**
     * 查询存储卷列表
     * @param namespace
     * @param project_name
     * @param region_id
     * @param pageno
     * @param size
     * @return
     */
    @ApiOperation(value = "查询存储卷列表", notes = "查询存储卷列表",
                  response = VolumeListResponse.class, tags = {"Storage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "List all volumes", response = VolumeListResponse.class)})
    @GetMapping(value = "/{namespace}/volumes", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    VolumeListResponse listVolumes(@PathVariable("namespace") String namespace,
    		@RequestParam(name = "project_name", required = false) String projectName,
                                    @RequestParam(name = "region_id" , required = true)String regionId ,
                                    @RequestParam(name = "pageno", required = false)Integer pageno,
                                    @RequestParam(name = "size" , required = false)Integer size );
    /**
     * 创建存储卷
     * @param namespace
     * @param project_name
     * @param reqBody
     * @return
     */
    @ApiOperation(value = "创建存储卷", notes = "创建存储卷",
                response = VolumeCreateResponse.class, tags = {"Storage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, 
    message = " Create storage volume", response = VolumeCreateResponse.class)})
    @PostMapping(value = "/{namespace}/volumes", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    VolumeCreateResponse createVolume(@PathVariable("namespace") String namespace,
    		@RequestParam(value = "project_name", required = false) String projectName,
                                  @RequestBody VolumesAddRequest reqBody);
    
    /**
     * 删除存储卷.<br/>
     *
     * 作者： zhangriyue
     * @return
     */
     @ApiOperation(value = "删除存储卷", notes = "删除存储卷", tags = {"Storage API"})
     @ApiResponses(value = {@ApiResponse(code = 204, message = "Deleted successfully")})
     @DeleteMapping(path = "/{namespace}/volumes/{volume_id}", produces = "appplication/json;charset=UTF-8")
     @ResponseStatus(HttpStatus.NO_CONTENT)
     void deleteVolumeById(@PathVariable("namespace") String namespace,
    		 @PathVariable("volume_id") String volumeId);
     
     /**
      * 
      * getVolStorageDetail:(根据名称查询存储卷详情). <br/>
      * 作者： baiwp
      * @param name
      * @param region_id
      * @return
      */
     @ApiOperation(value = "根据名称查询存储卷详情", notes = "根据名称查询存储卷详情",
             response = VolumeListByNameResponse.class, tags = {"Storage API"})
     @ApiResponses(value = {@ApiResponse(code = 200,
     message = "Get volume by volume name", response = VolumeListByNameResponse.class)})
     @GetMapping(value = "/{namespace}/volumes/{name}/name",
     produces = "application/json; charset=UTF-8")
     @ResponseStatus(HttpStatus.OK)
     CompVolumeDetailResponse getVolumeByName(@PathVariable("namespace") String namespace,
    		 									@PathVariable("name") String name,
                                             @RequestParam("region_id") String region_id);
     
     /**
      * 根据id获取存储卷详情
      * @param namespace
      * @param roleName
      * @return
      */
     @ApiOperation(value = "根据id获取存储卷详情", notes = "根据id获取存储卷详情",
             tags = {"Storage API"})
     @ApiResponses(value = {@ApiResponse(code = 200, message = "Return the volumes detail data",
     response = CompVolumeDetailResponse.class)})
     @GetMapping(path = "/{namespace}/volumes/{volume_id}", produces = "application/json;charset=UTF-8")
     @ResponseStatus(HttpStatus.OK)
     CompVolumeDetailResponse getVolumeById(@PathVariable("namespace") String namespace,
    		 								@PathVariable("volume_id") String volume_id);

}
