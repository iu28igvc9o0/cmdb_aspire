package com.aspire.mirror.alert.api.service.monthReport;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

@Api(value = "定时任务手动操作")
@RequestMapping(value = "/v1/alerts/schedule/")
public interface AlertsScheduleService {

    @GetMapping(value = "/v1/alerts/monReport/alert")
    @ApiOperation(value = "告警运营月报-告警分布", notes = "告警运营月报-告警分布", tags = {"Alerts Schedule API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功"), @ApiResponse(code = 500, message = "内部错误")})
    void alert(@RequestParam("startTime") String startTime,
                        @RequestParam("endTime") String endTime,
                        @RequestParam("month") String month);

    @GetMapping(value = "/v1/alerts/monReport/device")
    @ApiOperation(value = "告警运营月报-设备TOP10", notes = "告警运营月报-设备TOP10", tags = {"Alerts Schedule API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功"), @ApiResponse(code = 500, message = "内部错误")})
    void device(@RequestParam("startTime") String startTime,
               @RequestParam("endTime") String endTime,
               @RequestParam("month") String month);

    @GetMapping(value = "/v1/alerts/monReport/alertIndex")
    @ApiOperation(value = "告警运营月报-告警指标TOP10", notes = "告警运营月报-告警指标TOP10", tags = {"Alerts Schedule API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功"), @ApiResponse(code = 500, message = "内部错误")})
    void alertIndex(@RequestParam("startTime") String startTime,
               @RequestParam("endTime") String endTime,
               @RequestParam("month") String month);

    @GetMapping(value = "/v1/alerts/monReport/alertSum")
    @ApiOperation(value = "告警运营月报-告警总量", notes = "告警运营月报-告警总量", tags = {"Alerts Schedule API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功"), @ApiResponse(code = 500, message = "内部错误")})
    void alertSum(@RequestParam("startTime") String startTime,
               @RequestParam("endTime") String endTime,
               @RequestParam("month") String month);

    @GetMapping(value = "/v1/alerts/synchronize/instance")
    @ApiOperation(value = "同步cmdb主机实例", notes = "同步cmdb主机实例", tags = {"Alerts Schedule API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功"), @ApiResponse(code = 500, message = "内部错误")})
    void synchronizeInstance();

    @GetMapping(value = "/deviceInspectionByDay")
    @ApiOperation(value = "每日设备同步", notes = "每日设备同步", tags = {"Alerts Schedule API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功"), @ApiResponse(code = 500, message = "内部错误")})
    void deviceInspectionByDay();
}