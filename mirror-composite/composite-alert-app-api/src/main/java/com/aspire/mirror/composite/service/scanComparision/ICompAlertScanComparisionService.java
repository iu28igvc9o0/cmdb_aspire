package com.aspire.mirror.composite.service.scanComparision;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.scanComparision.CompAlertScanComparisionDetail;
import com.aspire.mirror.composite.payload.scanComparision.CompAlertScanComparisionReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Api(value = "告警扫描对账")
@RequestMapping(value = "/${version}/alerts/scanComparision")
public interface ICompAlertScanComparisionService {

    /**
     * 告警扫描对账列表
     */
    @PostMapping(value = "/getScanComparisionList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "告警扫描对账列表", notes = "告警扫描对账列表",
            response = CompAlertScanComparisionDetail.class, tags = {"Alert Scan Comparision API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = CompAlertScanComparisionDetail.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = CompAlertScanComparisionDetail.class)})
    PageResponse<CompAlertScanComparisionDetail> getScanComparisionList(@RequestBody CompAlertScanComparisionReq request);

    /**
     * 删除告警扫描对账
     */
    @DeleteMapping(value = "/deleteScanComparisionById", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "删除告警扫描对账", notes = "删除告警扫描对账",
            response = String.class, tags = {"Alert Scan Comparision API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = String.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = String.class)})
    String deleteScanComparisionById(@RequestBody List<String> request);

    /**
     * 同步告警扫描对账
     */
    @PostMapping(value = "/synScanComparision", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "同步告警扫描对账", notes = "同步告警扫描对账",
            response = String.class, tags = {"Alert Scan Comparision API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = String.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = String.class)})
    String synScanComparision(@RequestBody List<Map<String,String>> request);

    /**
     * 导出告警扫描对账
     */
    @RequestMapping(value = "/exportScanComparision", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "导出告警扫描对账", notes = "导出告警扫描对账", tags = {"Alert Scan Comparision API"})
    void exportScanComparision(@RequestBody CompAlertScanComparisionReq request, HttpServletResponse response) throws Exception;
}
