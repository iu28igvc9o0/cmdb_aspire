package com.migu.tsg.microservice.atomicservice.composite.service.resmgr;

import com.migu.tsg.microservice.atomicservice.composite.service.common.payload.BaseResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload.CompRegionDetailResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload.CompZoneCreateRequest;
import com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload.CompZoneUpdateRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
* composite层服务资源对外接口
* Project Name:composite-api
* File Name:ICompResServicesService.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resMgr
* ClassName: ICompResServicesService <br/>
* date: 2017年9月14日 上午11:02:30 <br/>
* 
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@RequestMapping(path = "/v1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(value = "Composite Resource management(Services)", description = "Composite Resource management(Services)")
public interface ICompZoneService {


    /**
     * 获取域列表
     * @return
     */
    @ApiOperation(value = "获取域列表", notes = "获取域列表", tags = {"Composite Resource management(Regions) service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Get zone resources for the namespace",
            response = List.class)})
    @GetMapping(value = "/regions/zone/", consumes = MediaType.ALL_VALUE)
    List<CompZoneCreateRequest> getZoneList();


    /**
     * 获取域列表
     * @return
     */
    @ApiOperation(value = "获取域列表", notes = "获取域列表", tags = {"Composite Resource management(Regions) service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Get zone resources for the namespace",
            response = CompZoneCreateRequest.class)})
    @GetMapping(value = "/regions/zone/{zone_name}", consumes = MediaType.ALL_VALUE)
    CompZoneCreateRequest getZoneByName(@PathVariable(value = "zone_name") String zoneName);

    /**
     * 创建域信息
     * @param req 创建域的请求信息
     * @return
     */
    @RequestMapping(value = "/regions/zone/", method = RequestMethod.POST)
    @ApiOperation(value = "创建域信息", notes = "创建域信息", response = CompZoneCreateRequest.class,
            tags = {"Composite Resource management(Regions) service API"})
    CompZoneCreateRequest createZone(@RequestBody CompZoneCreateRequest req);

    /**
     * 修改域信息
     * @param req
     * @return
     */
    @ApiOperation(value = "修改域信息", notes = "修改域信息",response = CompZoneUpdateRequest.class,
            tags = {"Composite Resource management(Regions) service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update zone resources for the namespace",
            response = CompZoneUpdateRequest.class)})
    @PutMapping(value = "/regions/zone/{zone_name}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    CompZoneUpdateRequest putZone(@PathVariable(value = "zone_name") String zoneName,
            @Valid @RequestBody CompZoneUpdateRequest req);

    /**
     * 删除域信息
     * @param req
     * @return
     */
    @ApiOperation(value = "删除域信息", notes = "删除域信息",response = BaseResponse.class,
            tags = {"Composite Resource management(Regions) service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Delete zone resources for the namespace",
            response = BaseResponse.class)})
    @DeleteMapping(value = "/regions/zone/{zone_name}", consumes = MediaType.ALL_VALUE)
    BaseResponse deleteZone(@PathVariable(value = "zone_name") String zoneName);

    /**
     * 获取zone下的集群列表
     * @param zoneName
     * @return
     */
    @ApiOperation(value = "获取zone下的集群列表", notes = "获取zone下的集群列表",response = BaseResponse.class,
            tags = {"Composite Resource management(Regions) service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Get region list resources for the namespace",
            response = BaseResponse.class)})
    @GetMapping(value = "/regions/zone/{zone_name}/regions/", consumes = MediaType.ALL_VALUE)
    List<CompRegionDetailResponse> getRegionListByName(@PathVariable(value = "zone_name") String zoneName);
}
