package com.aspire.mirror.alert.api.service.isolate;

import com.aspire.mirror.alert.api.dto.AlertsClearRequest;
import com.aspire.mirror.alert.api.dto.bpm.AlertsOrderRequest;
import com.aspire.mirror.alert.api.dto.alert.QueryParams;
import com.aspire.mirror.common.entity.PageResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author baiwp
 * @title: AlertIsolateAlertsService
 * @projectName mirror-alert
 * @description: TODO
 * @date 2019/8/1510:56
 */
@RequestMapping("/v2/alerts/isolateAlert")
@Api(value = "告警屏蔽记录")
public interface AlertIsolateAlertsV2Service {
    /**
     * 查询屏蔽记录列表
     *
     * @param queryParams
     * @return
     */
    @PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询屏蔽记录列表", notes = "查询屏蔽记录列表", 
    				response = PageResponse.class, tags = {"AlertIsolateAlerts API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = PageResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = PageResponse.class)})
    PageResponse<Map<String, Object>> list(@RequestBody QueryParams queryParams);

    /**
     * 导出屏蔽记录列表
     *
     * @param queryParams
     * @return
     */
    @PostMapping(value = "/export", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "导出屏蔽记录列表", notes = "导出屏蔽记录列表", response = Map.class, tags = {"AlertIsolateAlerts API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = Map.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = Map.class)})
    Map<String, Object> export(@RequestBody QueryParams queryParams);

    /**
     * 告警工单
     *
     * @param alertsOperationRequest 告警ID集合
     * @return 处理结果
     */
    @PostMapping(value = "/order")
    @ApiOperation(value = "手动派单", notes = "手动派单", tags = {"AlertIsolateAlerts API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    Map<String, Object> alertOrder(@RequestBody AlertsOrderRequest alertsOperationRequest);

    /**
     * 恢复告警
     *
     * @param params 告警ID集合
     * @return 处理结果
     */
    @PostMapping(value = "/recovery")
    @ApiOperation(value = "恢复告警", notes = "恢复告警", tags = {"AlertIsolateAlerts API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    Map<String, Object> alertRecovery(@RequestBody Map<String, Object> params);

    /**
     * 清除告警屏蔽日志
     *
     * @param alertsOperationRequest 告警ID集合
     * @return 处理结果
     */
    @DeleteMapping(value = "/remove")
    @ApiOperation(value = "清除告警屏蔽日志", notes = "清除告警屏蔽日志", tags = {"AlertIsolateAlerts API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "OK"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    Map<String, Object> remove(@RequestBody AlertsClearRequest alertsOperationRequest);
}
