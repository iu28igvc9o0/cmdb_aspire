package com.aspire.mirror.alert.api.service.alert;

import com.aspire.mirror.alert.api.dto.*;
import com.aspire.mirror.alert.api.dto.alert.AlertsConfirmRequest;
import com.aspire.mirror.alert.api.dto.bpm.AlertsOrderRequest;
import com.aspire.mirror.alert.api.dto.notify.AlertsNotifyRequest;
import com.aspire.mirror.alert.api.dto.alert.QueryParams;
import com.aspire.mirror.common.entity.PageResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.api.v2.service
 * @Author: baiwenping
 * @CreateTime: 2020-03-06 16:05
 * @Description: ${Description}
 */
@RequestMapping("/v2/alerts/alert")
@Api(value = "告警管理v2")
public interface AlertsV2Service {


    /**
     * 查询列表
     * @param queryParams
     * @return
     */
    @PostMapping(value = "/query", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "告警查询", notes = "告警查询", response = PageResponse.class, tags = {"Alert v2 API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = PageResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = PageResponse.class)})
    PageResponse<Map<String, Object>> query(@RequestBody QueryParams queryParams);

    /**
     * 导出警报列表数据
     */
    @ApiOperation(value = "导出警报列表数据", notes = "导出警报列表数据", tags = {"Alert v2 API"})
    @RequestMapping(value = "/export", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Object> export(@RequestBody QueryParams queryParams);

    /**
     * 查询详情
     * @param alertId
     * @return
     */
    @GetMapping(value = "/detail/{alert_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询详情", notes = "查询详情", response = Map.class, tags = {"Alert v2 API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = Map.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = Map.class)})
    Map<String, Object> detail(@PathVariable(name = "alert_id") String alertId);

    /**
     * 根据ids获取处理后的告警列表
     *
     * @param alertIds 告警Id
     * @return
     */
    @GetMapping(value = "/queryHandleList")
    @ApiOperation(value = "根据ids获取处理后的告警列表", notes = "根据ids获取处理后的告警列表", tags = {"Alert v2 API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> queryHandleList(@RequestParam("alert_ids") List<String> alertIds);

    /**
     * 告警-监控对象列表
     */
    @GetMapping(value = "/moniObject", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "监控对象列表", notes = "监控对象列表", response = List.class, tags = {"Alert v2 API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = List.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = List.class)})
    List<AlertConfigDict> getMonitObjectList();

    /**
     * 告警转派
     * @param alertsOperationRequest 告警id集合，逗号分隔
     * @return ResponseEntity 返回
     */

    @PostMapping(value = "/transfer")
    @ApiOperation(value = "告警转派", notes = "告警转派", tags = {"Alert v2 API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> alertTransfer(@RequestBody AlertsTransferRequest alertsOperationRequest);

    /**
     * 告警确认
     * @param alertsOperationRequest 告警id集合，逗号分隔
     * @return ResponseEntity 返回
     */
    @PostMapping(value = "/confirm")
    @ApiOperation(value = "告警确认", notes = "告警确认",
            tags = {"Alert v2 API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String>  alertConfirm(@RequestBody AlertsConfirmRequest alertsOperationRequest);

    /**
     * 告警待观察
     */
    @PostMapping(value = "/alertObserve")
    @ApiOperation(value = "告警待观察", notes = "告警待观察",tags = {"Alert v2 API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String>  alertObserve(@RequestBody Map<String,Object> request);

    /**
     * 告警工单
     *
     * @param alertsOperationRequest 告警ID集合
     * @return 处理结果
     */
    @PostMapping(value = "/order")
    @ApiOperation(value = "手动派单", notes = "手动派单", tags = {"Alert v2 API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> alertOrder(@RequestBody AlertsOrderRequest alertsOperationRequest);

    /**
     * 告警已通知功能
     *
     * @param request 告警ID集合
     * @return 处理结果
     */
    @PutMapping(value = "/notify/status/{status}")
    @ApiOperation(value = "批量更改通知状态", notes = "批量更改通知状态", tags = {"Alert v2 API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> notifyStatus(@PathVariable(name = "status") String status, @RequestBody Map<String,String> request);

    /**
     * 清除告警
     *
     * @param alertsOperationRequest 告警ID集合
     * @return 处理结果
     */
    @DeleteMapping(value = "/remove")
    @ApiOperation(value = "清除告警", notes = "清除告警", tags = {"Alert v2 API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "OK"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    ResponseEntity<String> alertRemove(@RequestBody AlertsClearRequest alertsOperationRequest);

    /**
     * 告警邮件通知
     *
     * @param alertsNotifyRequest 告警ID集合
     * @return 处理结果
     */
    @PostMapping(value = "/emailNotify")
    @ApiOperation(value = "告警邮件通知", notes = "告警邮件通知", tags = {"Alert v2 API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    ResponseEntity<String> alertemailNotify(@RequestBody AlertsNotifyRequest  alertsNotifyRequest);



    /**
     * 告警短信通知
     *
     * @param alertsNotifyRequest 告警ID集合
     * @return 处理结果
     */
    @PostMapping(value = "/smsNotify")
    @ApiOperation(value = "告警短信通知", notes = "告警短信通知", tags = {"Alert v2 API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    ResponseEntity<String> recordSMSNotify(@RequestBody AlertsNotifyRequest alertsNotifyRequest);

    /**
     * 查询单设备告警列表
     * @param queryParams
     * @return
     */
    @PostMapping(value = "/queryDeviceAlertList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询单设备告警列表", notes = "查询单设备告警列表", response = PageResponse.class, tags = {"Alert v2 API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = PageResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = PageResponse.class)})
    PageResponse<Map<String, Object>> queryDeviceAlertList(@RequestBody QueryParams queryParams);

    /**
     * 根据级别统计告警所属设备数
     * @param queryParams
     * @return
     */
    @PostMapping(value = "/summaryDeviceAlertsByLevel", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "根据级别统计告警所属设备数", notes = "根据级别统计告警所属设备数", response = PageResponse.class, tags = {"Alert v2 API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = PageResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = PageResponse.class)})
    List<Map<String, Object>> summaryDeviceAlertsByLevel(@RequestBody QueryParams queryParams);

    /**
     * 校验工单状态，返回告警未关闭的工单号
     * @param params
     * @return
     */
    @PostMapping(value = "/checkOrderStatus", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "校验工单状态，返回告警未关闭的工单号", notes = "校验工单状态，返回告警未关闭的工单号", response = PageResponse.class, tags = {"Alert v2 API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = List.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = List.class)})
    Map<String, Object> checkOrderStatus(@RequestBody Map<String, List<String>> params);
}
