package com.aspire.mirror.composite.service.alert;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.alert.*;
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
public interface ICompAlertsV2Service {


    /**
     * 查询列表
     * @param compQueryParams
     * @return
     */
    @PostMapping(value = "/query", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "告警查询", notes = "告警查询", response = PageResponse.class, tags = {"Alert v2 API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = PageResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = PageResponse.class)})
    PageResponse<Map<String, Object>> query(@RequestBody CompQueryParams compQueryParams);

    /**
     * 导出警报列表数据
     */
    @ApiOperation(value = "导出警报列表数据", notes = "导出警报列表数据", tags = {"Alert v2 API"})
    @RequestMapping(value = "/export", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Object> export(@RequestBody CompQueryParams compQueryParams);

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
     * 告警转派
     * @param compAlertsTransferRequest 告警id集合，逗号分隔
     * @return ResponseEntity 返回
     */

    /**
     * 告警-监控对象列表
     */
    @GetMapping(value = "/moniObject", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "监控对象列表", notes = "监控对象列表", response = List.class, tags = {"Alert v2 API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = List.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = List.class)})
    List<CompAlertConfigDict> getMonitObjectList();

    @PostMapping(value = "/transfer")
    @ApiOperation(value = "告警转派", notes = "告警转派", tags = {"Alert v2 API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> alertTransfer(@RequestBody CompAlertsTransferRequest compAlertsTransferRequest);

    /**
     * 告警确认
     * @param compAlertsConfirmRequest 告警id集合，逗号分隔
     * @return ResponseEntity 返回
     */
    @PostMapping(value = "/confirm")
    @ApiOperation(value = "告警确认", notes = "告警确认",
            tags = {"Alert v2 API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String>  alertConfirm(@RequestBody CompAlertsConfirmRequest compAlertsConfirmRequest);

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
     * @param compAlertsOrderRequest 告警ID集合
     * @return 处理结果
     */
    @PostMapping(value = "/order")
    @ApiOperation(value = "手动派单", notes = "手动派单", tags = {"Alert v2 API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> alertOrder(@RequestBody CompAlertsOrderRequest compAlertsOrderRequest);

    /**
     * 清除告警
     *
     * @param compAlertsClearRequest 告警ID集合
     * @return 处理结果
     */
    @DeleteMapping(value = "/remove")
    @ApiOperation(value = "清除告警", notes = "清除告警", tags = {"Alert v2 API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "OK"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    ResponseEntity<String> alertRemove(@RequestBody CompAlertsClearRequest compAlertsClearRequest);

    /**
     * 告警已通知功能
     *
     * @param request 告警ID集合
     * @return 处理结果
     */
    @PutMapping(value = "/notify/status")
    @ApiOperation(value = "批量更改通知状态", notes = "批量更改通知状态", tags = {"Alert v2 API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> notifyStatus(@RequestBody Map<String,String> request);

    /**
     * 告警邮件通知
     * @param compAlertsNotifyRequest
     * @return
     */
    @PostMapping(value = "/emailNotify")
    @ApiOperation(value = "告警邮件通知", notes = "告警邮件通知",
            tags = {"Alert v2 API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> emailNotify(@RequestBody CompAlertsNotifyRequest compAlertsNotifyRequest);

    /**
     * 告警短信通知
     * @param compAlertsNotifyRequest
     * @return
     */
    @PostMapping(value = "/smsNotify")
    @ApiOperation(value = "告警短信通知", notes = "告警短信通知",
            tags = {"Alert v2 API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String>  smsNotify( @RequestBody CompAlertsNotifyRequest compAlertsNotifyRequest);

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

    /**
     * 根据级别统计告警所属设备数,用于全设备展示页面告警统计功能
     * @param params
     * @return
     */
    @RequestMapping(value = "/summaryDeviceAlertsByLevel", method = RequestMethod.POST)
    @ApiOperation(value = "根据级别统计告警所属设备数", notes = "根据级别统计告警所属设备数", tags = {"restful API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> summaryDeviceAlertsByLevel(@RequestBody Map<String, Object> params);
}
