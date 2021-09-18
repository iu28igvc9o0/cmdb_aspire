package com.aspire.mirror.alert.api.service.monthReport;

import com.aspire.mirror.alert.api.dto.monthReport.AlertEsDataRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Api(value = "资源池运营月报")
@RequestMapping(value = "${version}/alerts/monReport")
public interface AlertsMonReportService {

    @PostMapping(value = "/viewByIdcType")
    @ApiOperation(value = "资源池运营月报-资源池总览", notes = "资源池运营月报-资源池总览", tags = {"Alerts Mon Report API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功"), @ApiResponse(code = 500, message = "内部错误")})
    Map<String,Object> viewByIdcType(@RequestBody Map<String, String> map);

    @PostMapping(value = "/viewByIp")
    @ApiOperation(value = "资源池运营月报-告警设备TOP10", notes = "资源池运营月报-告警设备TOP10", tags = {"Alerts Mon Report API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功"), @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String,Object>> viewByIp(@RequestBody Map<String,String> map);

    @PostMapping(value = "/viewByKeyComment")
    @ApiOperation(value = "资源池运营月报-告警指标TOP10", notes = "资源池运营月报-告警指标TOP10", tags = {"Alerts Mon Report API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功"), @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String,Object>> viewByKeyComment(@RequestBody Map<String,String> map);

    @PostMapping(value = "/getUserRateForZH", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "资源池，设备类型告警", notes = "资源池，设备类型告警",
            response = Map.class, tags = {"AlertIndexPage API"})

    Map<String, Object> getUserRateForZH(@RequestBody AlertEsDataRequest request) throws ParseException;


    @PostMapping(value = "/getIdcTypeUserRate", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "运营月报-资源池均峰值利用率", notes = "运营月报-资源池均峰值利用率", response = Map.class, tags = {"restful API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class)})
    Map<String, Object> getIdcTypeUserRate(@RequestBody AlertEsDataRequest request) throws ParseException;

    @PostMapping(value = "/getIdcTypeTrends", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "运营月报-资源池利用率趋势图", notes = "运营月报-资源池利用率趋势图", response = List.class, tags = {"restful API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class)})
    List<Map<String, Object>> getIdcTypeTrends(@RequestBody AlertEsDataRequest request);

}
