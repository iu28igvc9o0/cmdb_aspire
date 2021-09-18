package com.aspire.ums.cmdb.heartbeat;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICmdbHeartBeatAPI
 * Author:   zhu.juwang
 * Date:     2019/12/13 14:05
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Api(value = "CMDB心跳接口")
@RequestMapping("/cmdb/heart")
public interface ICmdbHeartBeatAPI {
    @GetMapping("/beat")
    @ApiOperation(value = "CMDB心跳接口", notes = "检测CMDB是否存活", tags = {"CMDB Heart Beat API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "存活"),
            @ApiResponse(code = 500, message = "非存活")})
    int heartbeat();
}
