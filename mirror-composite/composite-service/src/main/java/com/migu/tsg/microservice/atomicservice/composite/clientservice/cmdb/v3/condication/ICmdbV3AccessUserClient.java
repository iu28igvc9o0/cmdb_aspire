package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.v3.condication;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3AccessUser;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: ICmdbV3CondicationSettingClient
 * Author:   zhu.juwang
 * Date:     2020/1/10 9:39
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "CMDB")
public interface ICmdbV3AccessUserClient {
    /**
     * 获取组织列表
     * @param
     * @return
     */
    @PostMapping("/v3/cmdb/access/user/list")
    @ApiOperation(value = "查询接入用户列表", notes = "查询接入用户列表", tags = {"Cmdb Access User API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<CmdbV3AccessUser> list();
}
