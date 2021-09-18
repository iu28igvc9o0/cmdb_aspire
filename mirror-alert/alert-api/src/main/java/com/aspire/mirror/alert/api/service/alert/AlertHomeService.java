package com.aspire.mirror.alert.api.service.alert;

import com.aspire.mirror.alert.api.dto.alert.AlertsDetailResponse;
import com.aspire.mirror.alert.api.dto.alert.AlertsHisDetailResponse;
import com.aspire.mirror.alert.api.dto.alert.AlertsOverViewResponse;
import com.aspire.mirror.alert.api.dto.alert.AlertsQueryRequest;
import com.aspire.mirror.common.entity.PageResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RequestMapping(value = "/alerts/home")
public interface AlertHomeService {

    /**
     *  活动/确认/删除 告警 查询列表
     */
    @PostMapping(value = "/getAlertData", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "告警查询", notes = "告警查询", response = PageResponse.class, tags = {"Alert Home API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = PageResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = PageResponse.class)})
    PageResponse<AlertsDetailResponse> getAlertData(@RequestBody AlertsQueryRequest queryRequest,
                                                    @RequestParam("alertType") String alertType) throws ParseException;

    /**
     * (APP)活动告警source列表
     */
    @ApiOperation(value = "(APP)活动告警source列表", notes = "(APP)活动告警source列表", 
    		response = PageResponse.class, tags = {"Alert Home API"})
    @RequestMapping(value = "/activeAlertSourceList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    List<String> activeAlertSourceList();
    
    /**
     * (工作台)告警数量总览
     */
    @PostMapping(value = "/selectHomeAlertOverview", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "(工作台)告警数量总览", notes = "(工作台)告警数量总览", 
    response = AlertsOverViewResponse.class, tags = {"Alert Home API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = AlertsOverViewResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertsOverViewResponse.class)})
    AlertsOverViewResponse overview(@RequestBody AlertsQueryRequest queryRequest,
                                    @RequestParam("alertType") String alertType) throws ParseException;

    /**
     *  (工作台)解除告警查询列表
     */
    @PostMapping(value = "/query-his", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "(工作台)解除告警查询列表", notes = "(工作台)解除告警查询列表", 
    response = PageResponse.class, tags = {"Alert Home API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = PageResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = PageResponse.class)})
    PageResponse<AlertsHisDetailResponse> queryHis(@RequestBody AlertsQueryRequest queryRequest) throws ParseException;

    /**
     * 导出警报列表数据
     */
    @ApiOperation(value = "(工作台)活动/确认告警导出", notes = "(工作台)活动/确认告警导出", response = PageResponse.class, tags = {"Alert Home API"})
    @RequestMapping(value = "/export", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    List<Map<String, Object>> export(@RequestBody AlertsQueryRequest queryRequest,
                                     @RequestParam ("alertType") String alertType) throws Exception;

    /**
     * (工作台)解除告警导出
     */
    @ApiOperation(value = "(工作台)解除告警导出", notes = "(工作台)解除告警导出", 
    		response = PageResponse.class, tags = {"Alert Home API"})
    @RequestMapping(value = "/export-his", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    List<Map<String, Object>> exportHis(@RequestBody AlertsQueryRequest queryRequest) throws Exception;

    /**
     * 获取工作台语音播报内容
     */
    @PostMapping(value = "/getHomeAlertVoiceContent", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取工作台语音播报内容", notes = "获取工作台语音播报内容", 
    response = PageResponse.class, tags = {"Alert Home API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = PageResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = PageResponse.class)})
    ResponseEntity<String> getHomeAlertVoiceContent(@RequestParam ("isUandS") boolean isUandS,
                                                    @RequestBody AlertsQueryRequest queryRequest);

    /**
     * (工作台-解除告警)告警数量总览
     */
    @PostMapping(value = "/hisOverview", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "(工作台-解除告警)告警数量总览", notes = "(工作台-解除告警)告警数量总览",
            response = AlertsOverViewResponse.class, tags = {"Alert Home API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = AlertsOverViewResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertsOverViewResponse.class)})
    AlertsOverViewResponse hisOverview(@RequestBody AlertsQueryRequest queryRequest) throws ParseException;
}
