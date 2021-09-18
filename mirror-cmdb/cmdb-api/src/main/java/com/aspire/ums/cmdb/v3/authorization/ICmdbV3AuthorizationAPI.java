package com.aspire.ums.cmdb.v3.authorization;

import com.aspire.ums.cmdb.v3.authorization.payload.CmdbV3Authorization;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: ICmdbV3AuthorizationAPI
 * Author:   zhu.juwang
 * Date:     2020/1/10 9:38
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("/v3/cmdb/authorization")
public interface ICmdbV3AuthorizationAPI {
    /**
     * 获取权限
     * @return 返回实例数据
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiOperation(value = "获取权限", notes = "获取所有权限", tags = {"CMDB V3 Authorization API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<CmdbV3Authorization> list(@RequestParam(value = "authOwner", required = false) String authOwner);

}
