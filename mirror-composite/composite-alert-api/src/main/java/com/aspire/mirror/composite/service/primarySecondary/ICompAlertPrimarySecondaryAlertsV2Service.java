package com.aspire.mirror.composite.service.primarySecondary;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.alert.CompQueryParams;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * @author baiwp
 * @title: AlertIsolateAlertsService
 * @projectName mirror-alert
 * @description: TODO
 * @date 2019/8/1510:56
 */
@RequestMapping("/v2/alerts/primary_secondary_alerts")
@Api(value = "告警次要记录")
public interface ICompAlertPrimarySecondaryAlertsV2Service {
    /**
     * 查询次要记录列表
     *
     * @param compQueryParams
     * @return
     */
    @PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询次要记录列表", notes = "查询次要记录列表", 
    				response = PageResponse.class, tags = {"AlertPrimarySecondaryAlerts API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = PageResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = PageResponse.class)})
    PageResponse<Map<String, Object>> list(@RequestBody CompQueryParams compQueryParams);

    /**
     * 导出次要记录列表
     *
     * @param compQueryParams
     * @return
     */
    @PostMapping(value = "/export", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "导出次要记录列表", notes = "导出次要记录列表", response = Map.class, tags = {"AlertPrimarySecondaryAlerts API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = Map.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = Map.class)})
    Map<String, Object> export(@RequestBody CompQueryParams compQueryParams);

}
