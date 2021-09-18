package com.aspire.mirror.composite.service.alert;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.alert.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Api(value = "告警数据统计")
@RequestMapping("/${version}/alerts/statistic/")
public interface ICompAlertsStatisticService {

    /**
     * 告警总览
     *
     * @return AlertStatisticSummaryResponse 告警总览
     */
    @GetMapping(value = "/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建告警", notes = "告警总览", response = CompAlertStatisticSummaryResp.class, tags = {"AlertStatistic API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = CompAlertStatisticSummaryResp.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = CompAlertStatisticSummaryResp.class)})
    CompAlertStatisticSummaryResp summary(@RequestParam(name = "idcType", required = false) String idcType);

    /**
     *  综合首页待确认/待解决告警总量
     */
    @RequestMapping(value = "/getAlertSummary", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ApiOperation(value = "综合首页待确认/待解决告警总量", notes = "综合首页待确认/待解决告警总量",
            response = CompAlertSummaryResponse.class, tags = {"AlertStatistic API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = CompAlertSummaryResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = CompAlertSummaryResponse.class)})
    CompAlertSummaryResponse getAlertSummary();

    /**
     * 告警总览
     *
     * @return AlertStatisticSummaryResponse 告警总览
     */
    @GetMapping(value = "/overview", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "告警数量总览", notes = "告警数量总览", response = CompAlertsOverViewResponse.class, tags = {"AlertStatistic API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = CompAlertsOverViewResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = CompAlertsOverViewResponse.class)})
    CompAlertsOverViewResponse overview(@RequestParam(value = "codes") String code) throws ParseException;

    /**
     * 告警-监控对象列表
     *
     * @return AlertStatisticSummaryResponse 告警总览
     */
    @GetMapping(value = "/monit-obj-list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "监控对象列表", notes = "监控对象列表", response = List.class, tags = {"AlertStatistic API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = List.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = List.class)})
    List<String> monitObjectList();

    /**
     * 历史告警-监控对象列表
     *
     * @return AlertsSummaryResponse 监控对象列表
     */
    @GetMapping(value = "/monit-obj-list-his", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "历史监控对象列表", notes = "历史监控对象列表", response = List.class, tags = {"AlertStatistic API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = List.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = List.class)})
    List<String> monitObjectHisList();

    /**
     * 告警级别分布查询
     *
     * @return AlertStatisticSummaryDTO 告警级别总览
     */
    @GetMapping(value = "/level-span", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "告警级别分布查询", notes = "告警级别分布查询", response = CompAlertStatisticLevelResp.class, tags = {"AlertStatistic API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = CompAlertStatisticLevelResp.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = CompAlertStatisticLevelResp.class)})
    CompAlertStatisticLevelResp alertLevelSummay(@RequestParam(value = "span") String span);

    /**
     * 告警级别趋势查询
     * @param inteval
     * @return
     */
    @GetMapping(value = "/trend", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "告警级别趋势查询", notes = "告警级别趋势查询", response = Map.class, tags = {"AlertStatistic API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = Map.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = Map.class)})
    Map trend(@RequestParam(value = "span") String inteval);

    /**
     * 最新警报列表
     * @param limit
     * @return
     */
    @GetMapping(value = "/latest", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "最新警报列表", notes = "最新警报列表", response = CompAlertsDetailResp.class, tags = {"AlertStatistic API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = CompAlertsDetailResp.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = CompAlertsDetailResp.class)})
    List<CompAlertsDetailResp> latest(@RequestParam(value = "limit") Integer limit);

    /**
     * 告警类型分布查询
     * @return
     * @throws ParseException
     */
    @GetMapping(value = "/classify-span", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "告警类型分布查询", notes = "告警类型分布查询", response = CompAlertsStatisticClassifyResp.class, tags = {"AlertStatistic API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = CompAlertsStatisticClassifyResp.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = CompAlertsStatisticClassifyResp.class)})
    List<CompAlertsStatisticClassifyResp> classifyByTimeSpan(@RequestParam(value = "span") String span);

    /**
     * 告警资源池分布
     * @param span
     * @return
     */
    @GetMapping(value = "/source-classify-span", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "资源池归属", notes = "资源池归属", response = Map.class, tags = {"AlertStatistic API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = Map.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = Map.class)})
    List<Map<String, Object>> sourceClassifyByTimeSpan(@RequestParam(value = "span") String span);

    /**
     *  活动/确认 告警 查询列表
     * @param queryRequest
     * @return
     */
    @PostMapping(value = "/query", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "告警查询", notes = "告警查询", response = PageResponse.class, tags = {"AlertStatistic API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = PageResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = PageResponse.class)})
    PageResponse<CompAlertsDetailResp> query(@RequestBody CompAlertsDataRequest queryRequest) throws ParseException;

    /**
     * 导出警报列表数据
     * @param response
     */
    @ApiOperation(value = "导出警报列表数据", notes = "导出警报列表数据", tags = {"AlertStatistic API"})
    @RequestMapping(value = "/export", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    void export(@RequestBody CompAlertsDataRequest queryRequest, HttpServletResponse response) throws Exception;

    /**
     *  活动/确认 告警历史 查询列表
     * @param queryRequest
     * @return
     */
    @PostMapping(value = "/query-his", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "资源池归属", notes = "资源池归属", response = PageResponse.class, tags = {"AlertStatistic API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = PageResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = PageResponse.class)})
    PageResponse<CompAlertsHisDetailResp> queryHis(@RequestBody CompAlertsHisDataRequest queryRequest) throws ParseException;

    /**
     * 导出历史警报列表数据
     * @param response
     */
    @ApiOperation(value = "导出历史警报列表数据", notes = "导出历史警报列表数据", tags = {"AlertStatistic API"})
    @RequestMapping(value = "/export-his", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Object> exportHis(@RequestBody CompAlertsHisDataRequest queryRequest, HttpServletResponse response) throws Exception;

    /**
     *  综合首页设备告警数量概览
     */
    @GetMapping(value = "/getAlertSummaryByDeviceClass", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "综合首页设备告警数量概览", notes = "综合首页设备告警数量概览",
            response = Map.class, tags = {"AlertStatistic API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = Map.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = Map.class)})
    Map<String, Object> getAlertSummaryByDeviceClass(@RequestParam("code") String code);

    /**
     *  根据告警字段获取告警数量TOP10
     */
    @GetMapping(value = "/getAlertTargetCount", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "根据告警字段获取告警数量TOP10", notes = "根据告警字段获取告警数量TOP10",
            response = CompAlertTargetCountResponse.class, tags = {"AlertStatistic API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = Map.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = Map.class)})
    List<CompAlertTargetCountResponse> getAlertTargetCount(@RequestParam(value = "alertLevel", required = false) String alertLevel,
                                                           @RequestParam("type") String type);

    /**
     *  根据设备ip和资源池获取告警数量
     */
    @PostMapping(value = "/getAlertCountByIpAndIdc", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "根据设备ip和资源池获取告警数量", notes = "根据设备ip和资源池获取告警数量",
            response = Map.class, tags = {"AlertStatistic API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = Map.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = Map.class)})
    List<Map<String,Object>> getAlertCountByIpAndIdc(@RequestBody List<Map<String,Object>> request);

}
