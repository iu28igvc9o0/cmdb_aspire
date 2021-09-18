package com.aspire.mirror.alert.api.service.alert;

import com.aspire.mirror.alert.api.dto.alert.*;
import com.aspire.mirror.common.entity.PageResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Api(value = "告警统计")
@RequestMapping(value = "/v1/alerts/statistic/")
public interface AlertStatisticService {
    /**
     * 告警总览
     *
     * @return AlertStatisticSummaryResponse 告警总览
     */
    @GetMapping(value = "/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建告警", notes = "告警总览", response = AlertStatisticSummaryResponse.class, tags = {"AlertStatistic API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = AlertStatisticSummaryResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertStatisticSummaryResponse.class)})
    AlertStatisticSummaryResponse summary(@RequestParam(name = "idcType", required = false) String idcType);

    /**
     * 综合首页待确认/待解决告警总量
     */
    @PostMapping(value = "/getAlertSummary", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "综合首页待确认/待解决告警总量", notes = "综合首页待确认/待解决告警总量",
            response = AlertSummaryResponse.class, tags = {"AlertStatistic API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = AlertSummaryResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertSummaryResponse.class)})
    AlertSummaryResponse getAlertSummary(@RequestBody Map<String, Object> authContentMap);

    /**
     * 综合首页设备告警数量概览
     */
    @PostMapping(value = "/getAlertSummaryByDeviceClass", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "综合首页设备告警数量概览", notes = "综合首页设备告警数量概览",
            response = AlertStatisticSummaryResponse.class, tags = {"AlertStatistic API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = AlertStatisticSummaryResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertStatisticSummaryResponse.class)})
    List<AlertMainSummaryResponse> getAlertSummaryByDeviceClass(@RequestParam("code") String code);

    /**
     * 告警总览
     *
     * @return AlertsSummaryResponse 告警概览
     */
    @GetMapping(value = "/overview/{codes}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "告警数量总览", notes = "告警数量总览", response = AlertsOverViewResponse.class, tags = {"AlertStatistic API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = AlertsOverViewResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertsOverViewResponse.class)})
    AlertsOverViewResponse overview(@PathVariable("codes") String codes) throws ParseException;

    /**
     * 告警-监控对象列表
     *
     * @return AlertsSummaryResponse 监控对象列表
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
    @GetMapping(value = "/level", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "告警级别分布查询", notes = "告警级别分布查询", response = AlertStatisticSummaryDTO.class, tags = {"AlertStatistic API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = AlertStatisticSummaryDTO.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertStatisticSummaryDTO.class)})
    AlertStatisticSummaryDTO alertLevelSummay(@RequestParam(value = "startDate") String startDate,
                                              @RequestParam(value = "endDate") String endDate) throws ParseException;
    /**
     * 告警级别分布查询
     *
     * @return AlertStatisticSummaryDTO 告警级别总览
     */
    @GetMapping(value = "/level-span", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "告警级别分布查询", notes = "告警级别分布查询", response = AlertStatisticSummaryDTO.class, tags = {"AlertStatistic API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = AlertStatisticSummaryDTO.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertStatisticSummaryDTO.class)})
    AlertStatisticSummaryDTO alertLevelSummayByTimeSpan(@RequestParam(value = "span") String span);
    /**
     * 告警级别趋势查询
     * @param inteval
     * @return
     */
    @GetMapping(value = "/trend", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "告警级别趋势查询", notes = "告警级别趋势查询", response = Map.class, tags = {"AlertStatistic API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = Map.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertStatisticSummaryDTO.class)})
    Map trend(@RequestParam(value = "span") String inteval);

    /**
     * 最新警报列表
     * @param limit
     * @return
     */
    @GetMapping(value = "/latest", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "最新警报列表", notes = "最新警报列表", response = AlertsDetailResponse.class, tags = {"AlertStatistic API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = AlertsDetailResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertsDetailResponse.class)})
    List<AlertsDetailResponse> latest(@RequestParam(value = "limit") Integer limit);

    /**
     * 告警类型分布查询
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    @GetMapping(value = "/classify", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "告警类型分布查询", notes = "告警类型分布查询", response = AlertsStatisticClassifyDTO.class, tags = {"AlertStatistic API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = AlertsStatisticClassifyDTO.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertsStatisticClassifyDTO.class)})
    List<AlertsStatisticClassifyDTO> classify(@RequestParam(value = "startDate") String startDate,
                                              @RequestParam(value = "endDate") String endDate) throws ParseException;

    /**
     * 告警类型分布查询
     * @return
     * @throws ParseException
     */
    @GetMapping(value = "/classify-span", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "告警类型分布查询", notes = "告警类型分布查询", response = AlertsStatisticClassifyDTO.class, tags = {"AlertStatistic API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = AlertsStatisticClassifyDTO.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertsStatisticClassifyDTO.class)})
    List<AlertsStatisticClassifyDTO> classifyByTimeSpan(@RequestParam(value = "span") String span);
    /**
     * 资源池归属
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    @GetMapping(value = "/source-classify", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "资源池归属", notes = "资源池归属", response = Map.class, tags = {"AlertStatistic API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = Map.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = Map.class)})
    List<Map<String, Object>> sourceClassify(@RequestParam(value = "startDate") String startDate,
                       @RequestParam(value = "endDate") String endDate) throws ParseException;

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
    PageResponse<AlertsDetailResponse> query(@RequestBody AlertsDataRequest queryRequest) throws ParseException;

    /**
     * 导出警报列表数据
     */
    @ApiOperation(value = "导出警报列表数据", notes = "导出警报列表数据", tags = {"AlertStatistic API"})
    @RequestMapping(value = "/export", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    List<Map<String, Object>> export(@RequestBody AlertsDataRequest queryRequest) throws Exception;
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
    PageResponse<AlertsHisDetailResponse> queryHis(@RequestBody AlertsHisDataRequest queryRequest) throws ParseException;

    /**
     * 导出历史警报列表数据
     */
    @ApiOperation(value = "导出历史警报列表数据", notes = "导出历史警报列表数据", tags = {"AlertStatistic API"})
    @RequestMapping(value = "/export-his", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Object> exportHis(@RequestBody AlertsHisDataRequest queryRequest) throws Exception;

    /**
     *  根据告警字段获取告警数量TOP10
     */
    @GetMapping(value = "/getAlertTargetCount", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "根据告警字段获取告警数量TOP10", notes = "根据告警字段获取告警数量TOP10",
            response = AlertTargetCountResponse.class, tags = {"AlertStatistic API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = Map.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = Map.class)})
    List<AlertTargetCountResponse> getAlertTargetCount(@RequestParam(value = "alertLevel", required = false) String alertLevel,
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
