package com.aspire.mirror.composite.service.network;


import com.aspire.mirror.composite.payload.network.ComAlertNormResponse;
import com.aspire.mirror.composite.payload.network.ComNetworkIndicatorResponse;
import com.aspire.mirror.composite.payload.network.ComNetworkPageData;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author hewang
 * @date 2021-03-03
 * @version 1.0
 */
@RequestMapping(value="/v1/alerts/netWork")
public interface IComAlertNetworkService {

    /**
     * 端口指标查询
     * @param indicatorName
     * @param  pageNum
     * @param  pageSize
     * @return ComAlertNormResponse
     */
    @GetMapping(value = "/networkPortIndicators")
    @ApiOperation(value = "查询端口指标", notes = "查询端口指标", response = ResponseEntity.class, tags = {"Alert Network Port API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class)})
    Map<String, Object> queryNetworkPortIndicators(@RequestParam(value = "indicatorName", required = false) String indicatorName,
                                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                   @RequestParam(value = "pageSize", defaultValue = "50") Integer pageSize);

    /**
     * 端口指标新增
     * @param  responses
     * @return String
     */
    @PostMapping(value = "/addNetworkPortIndicators")
    @ApiOperation(value = "端口指标新增", notes = "端口指标新增", response = ResponseEntity.class, tags = { "Alert Network Port API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
    Map<String,String> addNetworkPortIndicators(@RequestBody List<ComAlertNormResponse> responses);


    /**
     * Top报表类型配置查询
     * @param  namespace
     * @return Map<String,Object>
     */
    @GetMapping (value = "/topReportTypeConfiguration")
    @ApiOperation(value = "Top报表类型配置查询", notes = "Top报表类型配置查询", response = ResponseEntity.class, tags = { "Alert Network Port API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
    Map<String, Object> queryTopReportTypeConfiguration(@RequestParam(value = "userName",required = false) String userName);

    /**
     * Top报表类型配置更新
     * @param  responses
     * @return Map<String,Object>
     */
    @PutMapping(value = "/updateTopReportTypeConfiguration")
    @ApiOperation(value = "Top报表类型配置更新", notes = "Top报表类型配置更新", response = ResponseEntity.class, tags = { "Alert Network Port API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
    Map<String, String> updateTopReportTypeConfiguration(@RequestBody List<ComAlertNormResponse> responses);

    /**
     * Top报表类型配置删除
     * @param  id
     * @return Map<String,Object>
     */
    @DeleteMapping(value = "/deleteTopReportTypeConfiguration")
    @ApiOperation(value = "Top报表类型配置删除", notes = "Top报表类型配置删除", response = ResponseEntity.class, tags = { "Alert Network Port API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
    Map<String, String> deleteTopReportTypeConfiguration(@RequestParam(value = "id",required = false) Integer id);

    /**
     * 网络端口设备列表导出
     *
     * @param request
     * @return Map<String, String>
     */
    @PostMapping(value = "/exportNetworkPortDeviceList")
    @ApiOperation(value = "网络端口设备列表导出", notes = "网络端口设备列表导出", response = ResponseEntity.class, tags = {"Alert_Network_Port_API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class)})
    Map<String, String> exportNetworkPortDeviceList(@RequestBody ComNetworkIndicatorResponse request);


    /**
     * 数据包详情导出
     *
     * @param data
     * @return Map<String, String>
     */
    @PostMapping(value = "/exportDataPageDetail")
    @ApiOperation(value = "数据包详情查询", notes = "数据包详情查询", response = ResponseEntity.class, tags = {"Alert_Network_Port_API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class)})
    Map<String, String> exportDataPageDetail(@RequestBody ComNetworkPageData data);


}
