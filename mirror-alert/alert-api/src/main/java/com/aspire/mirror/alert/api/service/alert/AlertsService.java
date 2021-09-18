package com.aspire.mirror.alert.api.service.alert;

import java.util.List;
import java.util.Map;
import com.aspire.mirror.alert.api.dto.*;
import com.aspire.mirror.alert.api.dto.alert.AlertDeviceInformation;
import com.aspire.mirror.alert.api.dto.alert.AlertValueSearchRequest;
import com.aspire.mirror.alert.api.dto.alert.AlertsOperationRequest;
import com.aspire.mirror.alert.api.dto.alert.AlertsConfirmRequest;
import com.aspire.mirror.alert.api.dto.alert.AlertRecordResp;
import com.aspire.mirror.alert.api.dto.notify.NotifyPageResponse;
import com.aspire.mirror.alert.api.dto.alert.AlertGenResp;
import com.aspire.mirror.alert.api.dto.alert.AlertsDetailResponse;
import com.aspire.mirror.alert.api.dto.notify.AlertNotifyResp;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.mirror.common.entity.PageResponse;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 告警服务
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.api.metrics
 * 类名称:    AlertsService.java
 * 类描述:    告警服务
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 11:09
 * 版本:      v1.0
 */
public interface AlertsService {

    /**
     * 提供bpm侧接口，同步告警事件
     * @param oldOrderId
     * @param orderId
     * @param type
     * @param orderStatus
     * @param userName
     * @return
     */
    @PutMapping(value="/v1/alerts/upgrade", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value="提供bpm侧接口，同步告警事件",notes="提供bpm侧接口，同步告警事件",response=String.class,tags={ "Alerts API" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response =  String.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = String.class) })
    String upgrade(@RequestParam("old_order_id") String oldOrderId, @RequestParam("order_id") String orderId,
                   @RequestParam("type") String type,@RequestParam("order_status") String orderStatus,
                   @RequestParam("user_name") String userName);

    /**
     * 告警列表
     *
     * @param pageRequset 查询page对象
     * @return PageResponse 列表返回对象
     */
    @PostMapping(value = "/v1/alerts/pageList")
    @ApiOperation(value = "列表", notes = "获取告警列表", tags = {"Alerts API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = PageResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    PageResponse<AlertsDetailResponse> pageList(@RequestBody AlertsPageRequset pageRequset);

    /**
     * 告警详情
     *
     * @param alertId 告警Id
     * @return
     */
    @GetMapping(value = "/v1/alerts/alertDetail/{alert_id}")
    @ApiOperation(value = "详情", notes = "根据alert_id获取告警详情", tags = {"Alerts API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = AlertsDetailResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    AlertSecondDetailResp findAlertByPrimaryKey(@PathVariable("alert_id") String alertId);


    /**
     * 告警上报记录
     *
     * @param alertId 告警Id
     * @return
     */
    @GetMapping(value = "/v1/alerts/alertGenerateList")
    @ApiOperation(value = "详情", notes = "根据alert_id获取上报记录", tags = {"Alerts API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = AlertsDetailResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    PageResponse<AlertGenResp> alertGenerateList(@RequestParam("alert_id") String alertId,
                                                 @RequestParam("page_no") String pageNo,
                                                 @RequestParam("page_size") String pageSize);


    /**
     * 告警操作记录
     *
     * @param alertId 告警Id
     * @return
     */
    @GetMapping(value = "/v1/alerts/alertRecordList")
    @ApiOperation(value = "详情", notes = "根据alert_id获取告警操作记录", tags = {"Alerts API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = AlertsDetailResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    PageResponse<AlertRecordResp>  alertRecordList(@RequestParam("alert_id") String alertId,
                                                   @RequestParam("page_no") String pageNo,
                                                   @RequestParam("page_size") String pageSize);


    /**
     * 告警通知记录
     *
     * @param alertId 告警Id
     * @return
     */
    @GetMapping(value = "/v1/alerts/alertNotifyList")
    @ApiOperation(value = "详情", notes = "根据alert_id获取告警通知记录", tags = {"Alerts API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = AlertsDetailResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    NotifyPageResponse<AlertNotifyResp>  alertNotifyList(@RequestParam("alert_id") String alertId,
                                                         @RequestParam("page_no") String pageNo,
                                                         @RequestParam("page_size") String pageSize,
                                                         @RequestParam("report_type") String reportType);


    /**
     * 告警上报excel 下载
     *
     * @param alertId 告警Id
     * @return
     */
    @GetMapping(value = "/v1/alerts/alertGenerateDownload")
    @ApiOperation(value = "详情", notes = "根据alert_id获取告警上报", tags = {"Alerts API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = AlertsDetailResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<AlertGenResp> alertGenerateDownload(@RequestParam("alert_id") String alertId  );



    /**
     * 告警操作excel 下载
     *
     * @param alertId 告警Id
     * @return
     */
    @GetMapping(value = "/v1/alerts/alertRecordDownload")
    @ApiOperation(value = "详情", notes = "根据alert_id获取告警操作", tags = {"Alerts API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = AlertsDetailResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<AlertRecordResp> alertRecordDownload(@RequestParam("alert_id") String alertId  );


    /**
     * 告警通知excel 下载
     *
     * @param alertId 告警Id
     * @return
     */
    @GetMapping(value = "/v1/alerts/alertNotifyDownload")
    @ApiOperation(value = "详情", notes = "根据alert_id获取告警通知", tags = {"Alerts API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = AlertsDetailResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<AlertNotifyResp> alertNotifyDownload(@RequestParam("alert_id") String alertId );




    /**
     * 修改告警备注
     *
     */

    @GetMapping(value = "/v1/alerts/updateNote/{alert_id}")
    @ApiOperation(value = "修改告警备注", notes = "修改告警备注", tags = {"Alerts API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String>  updateNote( @PathVariable("alert_id") String alertId, @RequestParam("note") String note );


    /**
     * 根据主键查询告警集合信息
     *
     * @param alertIds 模板主键(多个以逗号分隔)
     * @return List<AlertsDetailResponse> 告警查询响应对象
     */
    @GetMapping(value = "/v1/alerts/list/{alert_ids}")
    @ApiOperation(value = "查询", notes = "查询", tags = {"Alerts API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    List<AlertsDetailResponse> listByPrimaryKeyArrays(@PathVariable("alert_ids") String alertIds);

    @PutMapping(value = "/v1/alerts/modOrderStatusByOrderId/{order_id}/{status}")
    @ApiOperation(value = "根据工单ID修改状态", notes = "根据工单ID修改状态", tags = {"Alerts API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "OK"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    void modOrderStatusByOrderId(@PathVariable("order_id") String orderId, @PathVariable("status") String status);


    @PostMapping(value = "/v1/alerts/getAlertValue")
    @ApiOperation(value = "获取告警数量", notes = "获取告警数量", tags = {"Alerts API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功"), @ApiResponse(code = 500, message = "内部错误")})
    int getAlertValue(@RequestBody AlertValueSearchRequest alertValueSearchRequest);

    @GetMapping(value = "/v1/alerts/alertDeviceInformation")
    @ApiOperation(value = "详情", notes = "根据alert_id获取告警相关设备信息", tags = {"Alerts API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"), @ApiResponse(code = 500, message = "内部错误")})
    AlertDeviceInformation alertDeviceInformation(@RequestParam("alert_id") String alertId);

    @PostMapping(value = "/v1/alerts/smsTenantNotify")
    @ApiOperation(value = "租户告警短信通知", notes = "租户告警短信通知", tags = {"Alerts API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"), @ApiResponse(code = 500, message = "内部错误")})
    Boolean smsTenantNotify(@RequestBody  Map<String, Object> request);
}