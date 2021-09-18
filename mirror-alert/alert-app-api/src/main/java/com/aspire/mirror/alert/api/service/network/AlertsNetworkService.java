package com.aspire.mirror.alert.api.service.network;


import com.aspire.mirror.alert.api.dto.network.AlertNormResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author hewang
 * @version 1.0
 * @date 2021/3/11 20:42
 */
@RequestMapping(value = "/v1/alerts/netWork")
public interface AlertsNetworkService {
    /**
     * 端口指标查询
     *
     * @param indicatorName
     * @param pageNum
     * @param pageSize
     * @return AlertNormResponse
     */
    @GetMapping(value = "/networkPortIndicators")
    @ApiOperation(value = "查询端口指标", notes = "查询端口指标", response = ResponseEntity.class, tags = {"Alert_Network_Port_API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class)})
    List<AlertNormResponse> queryNetworkPortIndicators(@RequestParam(value = "indicatorName", required = false) String indicatorName,
                                                       @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                       @RequestParam(value = "pageSize", defaultValue = "50") int pageSize
    );

    /**
     * 端口指标新增
     *
     * @param responses
     * @return String
     */
    @PostMapping(value = "/addNetworkPortIndicators")
    @ApiOperation(value = "端口指标新增", notes = "端口指标新增", response = ResponseEntity.class, tags = {"Alert_Network_Port_API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class)})
    String addNetworkPortIndicators(@RequestBody List<AlertNormResponse> responses);


    /**
     * Top报表类型配置查询
     *
     * @param namespace
     * @return Map<String, Object>
     */
    @GetMapping(value = "/topReportTypeConfiguration")
    @ApiOperation(value = "Top报表类型配置查询", notes = "Top报表类型配置查询", response = ResponseEntity.class, tags = {"Alert_Network_Port_API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class)})
    List<AlertNormResponse> queryTopReportTypeConfiguration(@RequestParam(value = "userName",required = false) String userName);

    /**
     * Top报表类型配置更新
     * @param responses
     * @return
     */
    @PutMapping(value = "/updateTopReportTypeConfiguration")
    @ApiOperation(value = "Top报表类型配置查询", notes = "Top报表类型配置查询", response = ResponseEntity.class, tags = {"Alert_Network_Port_API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class)})
    String updateTopReportTypeConfiguration(@RequestBody List<AlertNormResponse> responses);

    /**
     * Top报表类型配置删除
     * @param  id
     * @return
     */
    @DeleteMapping(value = "/deleteTopReportTypeConfiguration")
    @ApiOperation(value = "Top报表类型配置删除", notes = "Top报表类型配置删除", response = ResponseEntity.class, tags = { "Alert Network Port API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
    String deleteTopReportTypeConfiguration(@RequestParam(value = "id" ,required = false) Integer id);

}
