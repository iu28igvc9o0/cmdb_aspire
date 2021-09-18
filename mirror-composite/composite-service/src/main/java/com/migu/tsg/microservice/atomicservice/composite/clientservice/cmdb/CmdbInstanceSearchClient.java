package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.aspire.ums.cmdb.common.PageBean;
import com.aspire.ums.cmdb.deviceStatistic.payload.InstanceSearchRequest;
import com.aspire.ums.cmdb.deviceStatistic.payload.InstanceSearchResp;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


@FeignClient( "CMDB")
public interface CmdbInstanceSearchClient {

      
    @PostMapping(value = "/v1/cmdb/instanceSearch/selectInstanceByPage")
    public PageBean<InstanceSearchResp> selectInstanceByPage( @RequestBody InstanceSearchRequest instanceSearchRequest );

    /**
     * 查询特殊权限数据实例数据
     */
    @RequestMapping(value = "/v1/cmdb/instanceSearch/getAuthDeviceData", method = RequestMethod.GET)
    @ApiOperation(value = "获取特殊权限实例数据", notes = "获取特殊权限实例数据", tags = {"CMDB Instance API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, String>> getAuthDeviceData(@RequestParam(value = "idcs", required = false) String idcs,
                                                @RequestParam(value = "rooms", required = false) String rooms,
                                                @RequestParam(value = "bisSyss", required = false) String bizSys,
                                                @RequestParam(value = "deviceTypes", required = false) String
                                                        deviceType);
    
}
