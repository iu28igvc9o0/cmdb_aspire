package com.aspire.mirror.composite.service.alert;

import java.text.ParseException;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import com.aspire.mirror.composite.payload.alert.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.aspire.mirror.common.entity.PageResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "工作台-我的告警")
@RequestMapping("/${version}/alerts/home")
public interface ICompAlertHomeService {

    /**
     *  (工作台)活动/确认告警查询列表
     */
    @PostMapping(value = "/query", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "(工作台)活动/确认告警查询列表", notes = "(工作台)活动/确认告警查询列表", response = PageResponse.class, tags = {"Alert Home API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = PageResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = PageResponse.class)})
    PageResponse<CompAlertsDetailResp> query(@RequestBody CompAlertsQueryRequest queryRequest,
                                             @RequestParam("alertType") String alertType) throws ParseException;

    /**
     * (工作台)告警数量总览
     */
    @PostMapping(value = "/overview", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "(工作台)告警数量总览", notes = "(工作台)告警数量总览", response = CompAlertsOverViewResponse.class, tags = {"Alert Home API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = CompAlertsOverViewResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = CompAlertsOverViewResponse.class)})
    CompAlertsOverViewResponse overview(@RequestBody CompAlertsQueryRequest queryRequest,
                                        @RequestParam("alertType") String alertType) throws ParseException;

    /**
     *  (工作台)解除告警查询列表
     */
    @PostMapping(value = "/query-his", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "(工作台)解除告警查询列表", notes = "(工作台)解除告警查询列表", response = PageResponse.class, tags = {"Alert Home API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = PageResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = PageResponse.class)})
    PageResponse<CompAlertsHisDetailResp> queryHis(@RequestBody CompAlertsQueryRequest queryRequest) throws ParseException;

    /**
     * (工作台)活动/确认告警导出
     */
    @ApiOperation(value = "(工作台)活动/确认告警导出", notes = "(工作台)活动/确认告警导出", response = PageResponse.class, tags = {"Alert Home API"})
    @RequestMapping(value = "/export", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    void export(@RequestBody CompAlertsQueryRequest queryRequest,
                @RequestParam ("alertType") String alertType,
                HttpServletResponse response) throws Exception;

    /**
     * (工作台)解除告警导出
     */
    @ApiOperation(value = "(工作台)解除告警导出", notes = "(工作台)解除告警导出", response = PageResponse.class, tags = {"Alert Home API"})
    @RequestMapping(value = "/export-his", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    void exportHis(@RequestBody CompAlertsQueryRequest queryRequest, HttpServletResponse response) throws Exception;


    /**
     * 获取工作台语音播报内容
     */
    @PostMapping(value = "/getHomeAlertVoiceContent", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取工作台语音播报内容", notes = "获取工作台语音播报内容", response = PageResponse.class, tags = {"Alert Home API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = PageResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = PageResponse.class)})
    ResponseEntity<String> getHomeAlertVoiceContent(@RequestParam ("alertType") String alertType);

    /**
     * (工作台-解除告警)告警数量总览
     */
    @PostMapping(value = "/hisOverview", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "(工作台-解除告警)告警数量总览", notes = "(工作台-解除告警)告警数量总览",
                  response = CompAlertsOverViewResponse.class, tags = {"Alert Home API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = CompAlertsOverViewResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = CompAlertsOverViewResponse.class)})
    CompAlertsOverViewResponse hisOverview(@RequestBody CompAlertsQueryRequest queryRequest) throws ParseException;

    /**
     *  (移动端工作台)活动/确认告警查询列表
     */
    @PostMapping(value = "/mobileQuery", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "(移动端工作台)活动/确认告警查询列表", notes = "(移动端工作台)活动/确认告警查询列表", response = PageResponse.class, tags = {"Alert Home API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = PageResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = PageResponse.class)})
    PageResponse<CompAlertsDetailResp> mobileQuery(@RequestBody CompAlertsQueryRequest queryRequest,
                                             @RequestParam("alertType") String alertType) throws ParseException;

    /**
     * (移动端工作台)告警数量总览
     */
    @GetMapping(value = "/mobileOverView", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "(移动端工作台)告警数量总览", notes = "(移动端工作台)告警数量总览", response = CompAlertStatisticSummaryResp.class, tags = {"Alert Home API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = CompAlertStatisticSummaryResp.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = CompAlertStatisticSummaryResp.class)})
    CompAlertStatisticSummaryResp mobileOverView(@RequestParam Map<String, String> params);
}
