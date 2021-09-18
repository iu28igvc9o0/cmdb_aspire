package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.cmic;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.cmdb.cmic.payload.CmdbModuleEventHandlerClass;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: ICmdbModuleEventAPI
 * Author:   zhu.juwang
 * Date:     2020/5/19 10:23
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "CMDB")
public interface CmdbModuleEventHandlerClassClient {

    @RequestMapping(value = "/cmdb/module/handleClass/getModuleHandlerClassList", method = RequestMethod.GET)
    @ApiOperation(value = "获取模型后置事件列表", notes = "获取模型后置事件列表", tags = {"CMDB Module Event API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<CmdbModuleEventHandlerClass> getModuleHandlerClassList(@RequestParam(value = "eventClass",required = false) String eventClass);

}
