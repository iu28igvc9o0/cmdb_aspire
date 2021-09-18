package com.aspire.mirror.composite.service.alert;

import com.aspire.mirror.common.entity.PageResult;
import com.aspire.mirror.composite.service.alert.payload.CompNetworkLoadBalancerDto;
import com.aspire.mirror.composite.service.alert.payload.CompNetworkPublicNetDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author baiwp
 * @title: ICompNetworkStrategyService
 * @projectName msp-composite
 * @description: TODO
 * @date 2019/7/2910:09
 */
@RequestMapping("${version}/alerts/network")
public interface ICompNetworkStrategyService {

    /**
     * 查询防火墙网略策略列表
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("fillWall")
    @ApiOperation(value = "查询防火墙网略策略列表", notes = "查询防火墙网略策略列表", tags = {"network strategy api"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "查询成功", response = PageResult.class),
            @ApiResponse(code = 500, message = "内部错误")})
    PageResult<Map<String, Object>> queryFillWall(@RequestParam(value = "keyword", required = false) String keyword,
                                                  @RequestParam(value = "name", required = false) String name,
                                                  @RequestParam(value = "sourceZone", required = false) String sourceZone,
                                                  @RequestParam(value = "destinationZone", required = false) String destinationZone,
                                                  @RequestParam(value = "action", required = false) String action,
                                                  @RequestParam(value = "destinationAddress", required = false) String destinationAddress,
                                                  @RequestParam(value = "sourceAddress", required = false) String sourceAddress,
                                                  @RequestParam(value = "servicePort", required = false) String servicePort,
                                                  @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                  @RequestParam(value = "pageSize", defaultValue = "50") int pageSize);

    /**
     *
     * @param keyword
     * @param response
     * @throws Exception
     */
    @PostMapping(value = "/fillWall/export", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "导出防火墙网略策略列表", notes = "导出防火墙网略策略列表", tags = {"network strategy api"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "查询成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    void exportFillWall(@RequestParam(value = "keyword", required = false) String keyword, HttpServletResponse response) throws Exception;

    /**
     * 查询负载均衡网略策略列表
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("loadBalancer")
    @ApiOperation(value = "查询负载均衡网略策略列表", notes = "查询负载均衡网略策略列表", tags = {"network strategy api"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "查询成功", response = PageResult.class),
            @ApiResponse(code = 500, message = "内部错误")})
    PageResult<CompNetworkLoadBalancerDto> queryLoadBalancer(@RequestParam(value = "keyword", required = false) String keyword,
                                                             @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                             @RequestParam(value = "pageSize", defaultValue = "50") int pageSize);

    /**
     *
     * @param keyword
     * @param response
     * @throws Exception
     */
    @PostMapping(value = "/loadBalancer/export", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "导出负载均衡网略策略列表", notes = "导出负载均衡网略策略列表", tags = {"network strategy api"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "查询成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    void exportLoadBalancer(@RequestParam(value = "keyword", required = false) String keyword, HttpServletResponse response) throws Exception;
    /**
     * 查询公网网略策略列表
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("publicNet")
    @ApiOperation(value = "查询公网网略策略列表", notes = "查询公网网略策略列表", tags = {"network strategy api"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "查询成功", response = PageResult.class),
            @ApiResponse(code = 500, message = "内部错误")})
    PageResult<CompNetworkPublicNetDto> queryPublicNet(@RequestParam(value = "keyword", required = false) String keyword,
                                                       @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                       @RequestParam(value = "pageSize", defaultValue = "50") int pageSize);
    /**
     *
     * @param keyword
     * @param response
     * @throws Exception
     */
    @PostMapping(value = "/publicNet/export", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "导出公网网略策略列表", notes = "导出公网网略策略列表", tags = {"network strategy api"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "查询成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    void exportPublicNet(@RequestParam(value = "keyword", required = false) String keyword, HttpServletResponse response) throws Exception;
}
