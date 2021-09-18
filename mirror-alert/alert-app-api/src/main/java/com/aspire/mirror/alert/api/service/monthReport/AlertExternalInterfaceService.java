package com.aspire.mirror.alert.api.service.monthReport;

import java.util.List;

import com.aspire.mirror.alert.api.dto.monthReport.AlertMonthReportAllDTO;
import com.aspire.mirror.alert.api.dto.monthReport.AlertMonthReportIdcTypeDTO;
import com.aspire.mirror.alert.api.dto.monthReport.AlertsLevelCountsDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.mirror.common.entity.PageResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author baiwp
 * @title: AlertDeriveAlertsService
 * @projectName mirror-alert
 * @description: TODO
 * @date 2019/8/1510:56
 */
@RequestMapping("/v1/alerts/ExternalInterfaceService")
@Api(value = "告警衍生记录")
public interface AlertExternalInterfaceService {

    /**
     *
     * @param deviceType
     * @return
     */
    @GetMapping(value = "/getDepartmentRatio", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询衍生记录列表", notes = "查询衍生记录列表", response = PageResponse.class, tags = {"ExternalInterfaceService API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = PageResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = PageResponse.class)})
    List<AlertMonthReportIdcTypeDTO> getDepartmentRatio(@RequestParam(value = "deviceType", required = false) String deviceType);
    
    @GetMapping(value = "/getAlertCount", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询衍生记录列表", notes = "查询衍生记录列表", response = PageResponse.class, tags = {"ExternalInterfaceService API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = PageResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = PageResponse.class)})
    List<AlertsLevelCountsDTO> getAlertCount();

    
    @GetMapping(value = "/getReportMonthAllDdata", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询衍生记录列表", notes = "查询衍生记录列表", response = PageResponse.class, tags = {"ExternalInterfaceService API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = PageResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = PageResponse.class)})
	List<AlertMonthReportAllDTO> getReportMonthAllDdata();

  
}
