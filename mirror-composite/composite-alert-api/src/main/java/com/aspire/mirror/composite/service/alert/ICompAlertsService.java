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

import java.util.Map;

/**
 * 告警暴露接口服务
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.alert
 * 类名称:    ICompAlertsService.java
 * 类描述:    告警暴露接口服务
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 14:40
 * 版本:      v1.0
 */
@Api(value = "告警信息管理")
@RequestMapping("/${version}/alerts")
public interface ICompAlertsService {
	
    /**
     * 告警列表
     * @param pageRequset 查询page对象
     * @return PageResponse 列表返回对象
     */
    @PostMapping(value = "/list")
    @ApiOperation(value = "列表", notes = "获取告警列表", tags = {"Alerts API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = PageResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    PageResponse<CompAlertsDetailResponse> pageList(@RequestBody CompAlertsPageRequset pageRequset);
    
    
   /**
     * 告警详情
     * @param alertId 告警Id
     * @return
     */
    @GetMapping(value = "/alertDetail/{alert_id}")
    @ApiOperation(value = "详情", notes = "根据alert_id获取告警详情", tags = {"Alerts API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = CompAlertsDetailResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CompAlertsSecondDetailResp findAlertByPrimaryKey(@PathVariable("alert_id") String alertId);

   
    /**
     * 告警上报记录分页
     *
     * @param alertId 告警Id
     * @return
     */
    @GetMapping(value = "/alertGenerateList")
    @ApiOperation(value = "详情", notes = "根据alert_id获取上报记录", tags = {"Alerts API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = CompAlertGenResp.class),
            @ApiResponse(code = 500, message = "内部错误")})
    PageResponse<CompAlertGenResp> alertGenerateList(@RequestParam("alert_id") String alertId,
    		                                         @RequestParam("page_no") String pageNo,
    		                                         @RequestParam("page_size") String pageSize);
     
    
    /**
     * 告警操作记录分页
     *
     * @param alertId 告警Id
     * @return
     */
    @GetMapping(value = "/alertRecordList")
    @ApiOperation(value = "详情", notes = "根据alert_id获取告警操作记录", tags = {"Alerts API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = CompAlertRecordResp.class),
            @ApiResponse(code = 500, message = "内部错误")})
    PageResponse<CompAlertRecordResp>  alertRecordList(@RequestParam("alert_id") String alertId,
                                                       @RequestParam("page_no") String pageNo,
                                                       @RequestParam("page_size") String pageSize);
    
    
    /**
     * 告警通知记录分页
     *
     * @param alertId 告警Id
     * @return
     */
    @GetMapping(value = "/alertNotifyList")
    @ApiOperation(value = "详情", notes = "根据alert_id获取告警通知记录", tags = {"Alerts API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = CompNotifyPageResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CompNotifyPageResponse<CompAlertNotifyResp>  alertNotifyList(@RequestParam("alert_id") String alertId,
													            @RequestParam("page_no") String pageNo,
													            @RequestParam("page_size") String pageSize,
													            @RequestParam("report_type") String reportType);
    
    
    /**
     * 告警上报excel 下载
     *
     * @param alertId 告警Id
     * @return
     */
    @GetMapping(value = "/alertGenerateDownload")
    @ApiOperation(value = "详情", notes = "根据alert_id获取告警上报", tags = {"Alerts API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功" ),
            @ApiResponse(code = 500, message = "内部错误")})
    void alertGenerateDownload(@RequestParam("alert_id") String alertId  );
    
    
    
    /**
     * 告警操作excel 下载
     *
     * @param alertId 告警Id
     * @return
     */
    @GetMapping(value = "/alertRecordDownload")
    @ApiOperation(value = "详情", notes = "根据alert_id获取告警操作", tags = {"Alerts API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"    ),
            @ApiResponse(code = 500, message = "内部错误")})
    void alertRecordDownload(@RequestParam("alert_id") String alertId  );
     
    
    /**
     * 告警通知excel 下载
     *
     * @param alertId 告警Id
     * @return
     */
    @GetMapping(value = "/alertNotifyDownload")
    @ApiOperation(value = "详情", notes = "根据alert_id获取告警通知", tags = {"Alerts API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"  ),
            @ApiResponse(code = 500, message = "内部错误")})
    void alertNotifyDownload(@RequestParam("alert_id") String alertId );
    
    
   
    
    /**
     * 修改告警备注
     */
    @GetMapping(value="/updateNote/{alert_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value="根据alert_id修改告警备注",notes="修改告警备注",tags={ "Alerts API" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error") })
    ResponseEntity<String> updateNote(@PathVariable("alert_id") String alertId, @RequestParam("note") String note  );
    
    /**
     * 修改告警状态和结束时间
     */
    @PutMapping(value="/bpmCallBack/{order_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value="BPM工单状态回调",notes="BPM工单状态回调",tags={ "Alerts API" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error") })
    ResponseEntity<String> bpmCallBack(@PathVariable("order_id") String orderId);

 /**
  * 当前告警-相关设备信息
  *
  * @param alertId 告警Id
  * @return
  */
 @GetMapping(value = "/alertDeviceInformation")
 @ApiOperation(value = "详情", notes = "根据alert_id获取告警相关设备信息", tags = {"Alerts API"})
 @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = CompAlertRecordResp.class),
         @ApiResponse(code = 500, message = "内部错误")})
 ResponseEntity<AlertDeviceInformationResponse> alertDeviceInformation(@RequestParam("alert_id") String alertId);

 /**
  * 租户告警短信通知
  *
  */
 @PostMapping(value = "/smsTenantNotify")
 @ApiOperation(value = "租户告警短信通知", notes = "告警短信通知",
         tags = {"Alerts API"})
 @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
 ResponseEntity<Map>  smsTenantNotify(@RequestBody Map<String,Object> request);

}
