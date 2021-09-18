package com.aspire.ums.cmdb.redis;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICmdbRedisAPI
 * Author:   hangfang
 * Date:     2020/11/23
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("/cmdb/redis")
public interface ICmdbRedisAPI {

    /**
     * 刷新某条数据
     */
    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    @ApiOperation(value = "刷新某条数据", notes = "刷新某条数据", tags = {"CMDB REDIS API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "刷新成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> refresh(@RequestParam("type") String redisType,
                                @RequestParam("key") String redisKey);


}
