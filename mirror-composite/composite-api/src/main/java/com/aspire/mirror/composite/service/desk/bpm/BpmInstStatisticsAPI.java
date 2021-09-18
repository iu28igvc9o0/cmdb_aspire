package com.aspire.mirror.composite.service.desk.bpm;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @projectName: BpmInstStatisticsAPI
 * @description: 接口
 * @author: tongzhihong
 * @create: 2020-09-14 10:37
 **/
@RequestMapping("${version}/bpm/runtime")
public interface BpmInstStatisticsAPI {

    @RequestMapping(value = "/instDistribution", method = RequestMethod.GET)
    @ApiOperation(value = "工单分布", notes = "工单分布", tags = {"desk API "})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public Object instDistributionInBpm(@RequestParam Integer type, @RequestParam Integer isWhole,
                                        @RequestParam String startDate, @RequestParam String endDate);

    @RequestMapping(value = "/resRate/orderTimelinessGroupByTenant", method = RequestMethod.POST)
    @ApiOperation(value = "分租户统计工单及时/超时数据（查租户维度数据）", notes = "分租户统计工单及时/超时数据（查租户维度数据）", tags = {"desk API "})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public Object orderTimelinessGroupByTenant(@RequestBody Map<String, Object> req);

    @RequestMapping(value = "/resRate/orderTimelinessStatistics", method = RequestMethod.POST)
    @ApiOperation(value = "资源类型统计工单及时率饼状图", notes = "资源类型统计工单及时率饼状图", tags = {"desk API "})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public Object orderTimelinessStatistics(@RequestBody Map<String, Object> req);

    @RequestMapping(value = "/resRate/monthInTimeRate", method = RequestMethod.POST)
    @ApiOperation(value = "获取某一年度各类型资源各月份开通及时率", notes = "获取某一年度各类型资源各月份开通及时率", tags = {"desk API "})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public Object monthInTimeRate(@RequestBody Map<String, Object> req);

    @RequestMapping(value = "/resRate/allTypeOrder", method = RequestMethod.POST)
    @ApiOperation(value = "根据筛选条件获取某各类型资源工单详细数据", notes = "根据筛选条件获取某各类型资源工单详细数据", tags = {"desk API "})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public Object allTypeOrder(@RequestBody Map<String, Object> req);
}
