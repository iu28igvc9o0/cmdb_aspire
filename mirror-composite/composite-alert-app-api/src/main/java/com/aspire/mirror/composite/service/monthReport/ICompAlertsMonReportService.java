package com.aspire.mirror.composite.service.monthReport;

import com.aspire.mirror.composite.payload.alert.ComAlertEsDataRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Api(value = "资源池运营月报")
@RequestMapping(value = "${version}/alerts/monReport")
public interface ICompAlertsMonReportService {

    @PostMapping(value = "/viewByIdcType")
    @ApiOperation(value = "资源池运营月报-资源池总览", notes = "资源池运营月报-资源池总览", tags = {"Alerts Mon Report API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功"), @ApiResponse(code = 500, message = "内部错误")})
    Map<String,Object> viewByIdcType(@RequestBody Map<String,String> map);

    @PostMapping(value = "/viewByIp")
    @ApiOperation(value = "资源池运营月报-告警设备TOP10", notes = "资源池运营月报-告警设备TOP10", tags = {"Alerts Mon Report API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功"), @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String,Object>> viewByIp(@RequestBody Map<String,String> map);

    @PostMapping(value = "/viewByKeyComment")
    @ApiOperation(value = "资源池运营月报-告警指标TOP10", notes = "资源池运营月报-告警指标TOP10", tags = {"Alerts Mon Report API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功"), @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String,Object>> viewByKeyComment(@RequestBody Map<String,String> map);

    @PostMapping(value = "/getIdcTypeUserRate")
    @ApiOperation(value = "运营月报-资源池均峰值利用率", notes = "运营月报-资源池均峰值利用率", response = ResponseEntity.class, tags = {"restful API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class)})
    List<Map<String, Object>> getIdcTypeUserRate(@RequestBody ComAlertEsDataRequest request) throws ParseException;

    @PostMapping(value = "/getIdcTypeTrends")
    @ApiOperation(value = "运营月报-资源池利用率趋势图", notes = "运营月报-资源池利用率趋势图", response = ResponseEntity.class, tags = {"restful API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class)})
    List<Map<String, Object>> getIdcTypeTrends(@RequestBody ComAlertEsDataRequest request);

    @GetMapping(value = "/getUserRateForZH", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "租户首页：资源利用率", notes = "租户首页：资源利用率", response = Map.class, tags = {"AlertIndexPage API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = Map.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = Map.class)})
    Map<String, Object> getUserRateForZH(@RequestParam(value = "deviceType",required=false) String deviceType) throws ParseException;

}
