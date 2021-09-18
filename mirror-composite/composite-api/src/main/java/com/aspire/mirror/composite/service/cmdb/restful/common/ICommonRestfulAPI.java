package com.aspire.mirror.composite.service.cmdb.restful.common;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.restful.payload.StatisticRequestEntity;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IAutomateRestfulAPI
 * Author:   zhu.juwang
 * Date:     2019/9/11 10:55
 * Description: 该接口类用来专门提供接口给自动化运维工具使用
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("${version}/cmdb/restful/common")
public interface ICommonRestfulAPI {
    /**
     * 根据查询条件,获取主机列表
     * @param params 查询参数
     *   {"device_type": "设备类型", "idcType": "资源池名称"}
     */
    @RequestMapping(value = "/instance/list", method = RequestMethod.POST)
    @ApiOperation(value = "获取设备列表信息", notes = "获取设备列表信息", tags = {"Cmdb Common Restful API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<Map<String, Object>> getInstanceList(@RequestBody Map<String, Object> params, HttpServletResponse response) throws Exception;

    /**
     * 根据查询条件,获取主机列表
     * @param params 查询参数
     *   {"device_type": "设备类型", "idcType": "资源池名称"}
     */
    @RequestMapping(value = "/instance/detail", method = RequestMethod.POST)
    @ApiOperation(value = "获取设备列表信息", notes = "获取设备列表信息", tags = {"Cmdb Common Restful API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> getInstanceDetail(@RequestBody Map<String, Object> params);

    /**
     * 统计资源设备
     * @param statisticRequestEntity 查询参数
     */
    @RequestMapping(value = "/instance/statistics", method = RequestMethod.POST)
    @ApiOperation(value = "统计设备信息", notes = "统计设备信息", tags = {"Cmdb Common Restful API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Object getInstanceStatistics(@RequestBody StatisticRequestEntity statisticRequestEntity);
}
