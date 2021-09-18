package com.aspire.mirror.composite.service.cmdb.instance;

import com.aspire.mirror.composite.service.cmdb.instance.payload.CmdbBpmProcQuery;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IInstanceBpmAPI
 * Author:   hangfang
 * Date:     2019/9/6
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("${version}/cmdb/instanceBpm")
public interface IInstanceBpmAPI {

    /**
     * 根据instId获取bpm工单列表
     */
    @RequestMapping(value = "/findInstListByDeviceId", method = RequestMethod.POST)
    @ApiOperation(value = "根据instId获取bpm工单列表", notes = "根据instId获取bpm工单列表", tags = {"CMDB instanceBpm API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Object findInstListByDeviceId(@RequestBody CmdbBpmProcQuery bpmProcQuery);

    /**
     * 获取bpm所有工单类型
     */
    @RequestMapping(value = "/getAllFlowDefList", method = RequestMethod.GET)
    @ApiOperation(value = "获取bpm所有工单类型", notes = "获取bpm所有工单类型", tags = {"CMDB instanceBpm API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Object getAllFlowDefList();

}
