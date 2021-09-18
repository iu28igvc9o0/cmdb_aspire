package com.aspire.mirror.alert.api.service.scanComparision;

import com.aspire.mirror.alert.api.dto.scanComparision.AlertScanComparisionDetail;
import com.aspire.mirror.alert.api.dto.scanComparision.AlertScanComparisionReq;
import com.aspire.mirror.common.entity.PageResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Api(value = "告警扫描对账")
@RequestMapping(value = "/${version}/alerts/scanComparision")
public interface AlertScanComparisionService {

    /**
     * 告警扫描对账列表
     */
    @PostMapping(value = "/getScanComparisionList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "告警扫描对账列表", notes = "告警扫描对账列表",
            response = AlertScanComparisionDetail.class, tags = {"Alert Scan Comparision API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = AlertScanComparisionDetail.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertScanComparisionDetail.class)})
    PageResponse<AlertScanComparisionDetail> getScanComparisionList(@RequestBody AlertScanComparisionReq request);
    
  

    /**
     * 删除告警扫描对账
     */
    @DeleteMapping(value = "/deleteScanComparisionById", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "删除告警扫描对账", notes = "删除告警扫描对账", tags = {"Alert Scan Comparision API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    void deleteScanComparisionById(@RequestBody List<String> request);

    /**
     * 同步告警扫描对账
     */
    @DeleteMapping(value = "/synScanComparision", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "删除告警扫描对账", notes = "删除告警扫描对账", tags = {"Alert Scan Comparision API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    void synScanComparision(@RequestBody List<Map<String, String>> request);

    /**
     * 导出告警扫描对账
     */
    @DeleteMapping(value = "/exportScanComparision", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "导出告警扫描对账", notes = "导出告警扫描对账", tags = {"Alert Scan Comparision API"})
    List<Map<String, Object>> exportScanComparision(@RequestBody AlertScanComparisionReq request) throws Exception;
}
