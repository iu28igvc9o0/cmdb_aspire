package com.aspire.mirror.composite.service.order;

import com.aspire.mirror.composite.service.order.payload.HomePageInstAnalysisReq;
import com.aspire.mirror.composite.service.order.payload.PageBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 工单服务
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.order
 * 类名称:    IOrderService.java
 * 类描述:    工单服务
 * 创建人:    JinSu
 * 创建时间:  2019/9/2 11:32
 * 版本:      v1.0
 */
@Api("工单信息查询")
@RequestMapping("${version}/order")
public interface IOrderService {

    @GetMapping(value = "/instTrend")
    @ApiOperation(value = "工单趋势", notes = "工单趋势", tags = {"Order API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = Object.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Object instTrend(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String
            endDate,@RequestParam(value = "isWhole") Integer isWhole);

    @PostMapping(value = "/listJson", produces= "application/json;charset=UTF-8")
    @ApiOperation(value = "查询所有工单类型", notes = "查询所有工单类型", tags = {"Order API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = Object.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Object listJson(@RequestBody PageBean page);

    @GetMapping(value = "/instDistribution")
    @ApiOperation(value = "工单分布", notes = "工单分布", tags = {"Order API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = Object.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Object instDistribution(@RequestParam(value = "type") Integer type,@RequestParam(value = "isWhole") Integer isWhole );

    @GetMapping(value = "/getAccountByParam")
    @ApiOperation(value = "获取日增、周关、待办工单数", notes = "获取日增、周关、待办工单数", tags = {"Order API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = Object.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Object getAccountByParam();

    @GetMapping(value = "/instStatistics")
    @ApiOperation(value = "工单统计", notes = "工单统计", tags = {"Order API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = Object.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Object instStatistics(@RequestParam(value = "defKey") String defKey, @RequestParam(value = "startDate") String
            startDate, @RequestParam(value = "endDate") String endDate,@RequestParam(value = "isWhole") Integer isWhole);

    //================@date 2019/10/24 新版综合首页
    /**
     * 工单总览
     * @param
     * @return
     */
    @GetMapping(value = "/instOverview")
    @ApiOperation(value = "工单总览", notes = "工单总览", tags = {"Order API"})
    Object instOverview();

    /**
     * 工单分析
     * @param req
     * @return
     */
    @PostMapping(value = "/homepageProInstAnalysis", produces= "application/json;charset=UTF-8")
    @ApiOperation(value = "工单分析", notes = "工单分析", tags = {"Order API"})
    Object homepageProInstAnalysis(@RequestBody HomePageInstAnalysisReq req);

    /**
     * 工单分布New
     * @return
     */
    @GetMapping(value = "/instDistributionNew")
    @ApiOperation(value = "工单分布New", notes = "工单分布", tags = {"Order API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = Object.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Object instDistributionNew();

    /**
     * 工单分类处理时长分析
     * @return
     */
    @GetMapping(value = "/instRuntimeAnalysis")
    @ApiOperation(value = "工单分类处理时长分析", notes = "工单分类处理时长分析", tags = {"Order API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = Object.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Object instRuntimeAnalysis();

    /**
     * 工单统计New
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping(value = "/instStatisticsNew")
    @ApiOperation(value = "工单统计New", notes = "工单统计New", tags = {"Order API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = Object.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Object instStatisticsNew(@RequestParam(value = "startDate") String
            startDate, @RequestParam(value = "endDate") String endDate);

    /**
     * 根据工单类型key获取工单实例列表
     * @param defKey
     * @return
     */
    @GetMapping(value = "/getInstListByDefKey")
    @ApiOperation(value = "根据工单类型key获取工单实例列表", notes = "根据工单类型key获取工单实例列表", tags = {"Order API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = Object.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Object getInstListByDefKey(@RequestParam(value = "defKey") String defKey);


    /**
     * 根据工单类型key和运行状态获取工单实例列表
     * @param defKey
     * @param status
     * @return
     */
    @GetMapping(value = "/getInstListByDefKeyAndStatus")
    @ApiOperation(value = "根据工单类型key和运行状态获取工单实例列表", notes = "根据工单类型key和运行状态获取工单实例列表", tags = {"Order API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = Object.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Object getInstListByDefKeyAndStatus(@RequestParam(value = "defKey") String defKey,@RequestParam(value = "status") String status);
}
