package com.migu.tsg.microservice.atomicservice.composite.service.storage;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.migu.tsg.microservice.atomicservice.composite.service.storage.payload.DriverResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
* 项目名称: composite-api
* 包: com.migu.tsg.microservice.atomicservice.composite.service.storage
* 类名称: ICompPermissionService.java
* 接口描述:驱动接口
* 创建人: ZhangRiYue
* 创建时间: 2017年9月11日上午10:29:01
* 版本: v1.0
 */
@RequestMapping("${version}/storage")
@Api(value = "${version}/Driver", description = "View the driver type")
public interface ICompDriverService {

    /**
     * 获取驱动类型的操作集合. <br/>
     * 作者： 张日月
     * @param namespace
     * @param resourceType
     * @return   获取驱动类型的操作集合   如   [get：get_driver_list]
     */
    @ApiOperation(value = "驱动类型的操作集合", notes = "驱动类型的操作集合",
            response = DriverResponse.class, tags = {"Storage API"})
    @ApiResponses(value = {@ApiResponse(code = 200,
    message = "Returns the drive collection", response = DriverResponse.class)})
    @GetMapping(value = "/{namespace}/{region_name}/drivers",
    produces = "application/json; charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    DriverResponse driverStorage(@PathVariable("region_name") String region_name,
    							@PathVariable("namespace") String namespace);
}