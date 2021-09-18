package com.aspire.ums.cmdb.v3.condication;

import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3AccessUser;
import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3CondicationSettingQuery;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: ICmdbV3CondicationSettingAPI
 * Author:   zhu.juwang
 * Date:     2020/1/10 9:39
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("/v3/cmdb/access/user")
public interface ICmdbV3AccessUserAPI {
    /**
     * 获取组织列表
     * @param
     * @return
     */
    @PostMapping("/list")
    @ApiOperation(value = "查询接入用户列表", notes = "查询接入用户列表", tags = {"Cmdb Access User API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<CmdbV3AccessUser> list();
}
