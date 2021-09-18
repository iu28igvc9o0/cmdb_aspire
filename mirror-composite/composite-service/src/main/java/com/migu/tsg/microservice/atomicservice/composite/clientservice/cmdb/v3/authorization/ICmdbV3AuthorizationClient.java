package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.v3.authorization;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.cmdb.v3.authorization.payload.CmdbV3Authorization;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICmdbV3AuthorizationClient
 * Author:   hangfang
 * Date:     2020/2/12
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "CMDB")
public interface ICmdbV3AuthorizationClient {
    /**
     * 获取权限
     * @return 返回实例数据
     */
    @RequestMapping(value = "/v3/cmdb/authorization/list", method = RequestMethod.GET)
    @ApiOperation(value = "获取权限", notes = "获取所有权限", tags = {"CMDB V3 Authorization API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<CmdbV3Authorization> list(@RequestParam(value = "authOwner", required = false) String authOwner);


}
