package com.aspire.ums.cmdb.cmic;

import com.alibaba.fastjson.JSONArray;
import com.aspire.ums.cmdb.cmic.payload.TestEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: ITestAPI
 * Author:   zhu.juwang
 * Date:     2020/7/6 11:24
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Api(value = " IP地址接口类")
@RequestMapping("/cmdb/cmic/test")
public interface ITestAPI {

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    @ApiOperation(value = "", notes = "",
            tags = {"CMDB CMIC IP API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    void test(@RequestBody TestEntity entity);

}
