package com.aspire.mirror.composite.service.alert;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.aspire.mirror.composite.payload.alert.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.aspire.mirror.common.entity.PageResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "告警统计")
@RequestMapping(value = "/${version}/alerts/indexPage")
public interface IComAlertIndexPageService {
    /**
     * 待解决告警、已解决告警
     *
     * @return AlertStatisticSummaryResponse 告警总览
     */
    @GetMapping(value = "/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "待解决和已解决告警", notes = "告警总览", response = CompAlertStatisticSummaryResp.class, tags = {"AlertIndexPage API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = CompAlertStatisticSummaryResp.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = CompAlertStatisticSummaryResp.class)})
    CompAlertStatisticSummaryResp summary(@RequestParam(value="idcType",required = false) String idcType);




    /**
     * 告警级别分布查询
     *
     * @return AlertStatisticSummaryDTO 告警级别总览
     */
    @GetMapping(value = "/level-span", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "告警级别分布查询", notes = "告警级别分布查询", response = CompAlertStatisticLevelResp.class, tags = {"AlertIndexPage API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = CompAlertStatisticLevelResp.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = CompAlertStatisticLevelResp.class)})
    CompAlertStatisticLevelResp alertLevelSummayByTimeSpan(@RequestParam(value = "span") String span
    		, @RequestParam(value="idcType",required = false) String idcType);
    /**
     * 告警级别趋势查询
     * @param inteval
     * @return
     */
    @GetMapping(value = "/trend", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "告警级别趋势查询", notes = "告警级别趋势查询", response = Map.class, tags = {"AlertIndexPage API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = Map.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = Map.class)})
    Map trend(@RequestParam(value = "span") String inteval,@RequestParam(value="idcType",required = false) String idcType,
    		@RequestParam(value="deviceType",required = false) String deviceType,@RequestParam(value = "alertLevel" ,required = false) String alertLevel
    		,@RequestParam(value="source",required = false) String source);

   


    /**
     * 告警类型分布查询
     * @return
     * @throws ParseException
     */
    @GetMapping(value = "/classify-span", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "告警类型分布查询", notes = "告警类型分布查询", response = CompAlertsStatisticClassifyResp.class, tags = {"AlertIndexPage API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = CompAlertsStatisticClassifyResp.class),
            @ApiResponse(code = 500, message = "Unexpected error", response =CompAlertsStatisticClassifyResp.class)})
    List<CompAlertsStatisticClassifyResp> classifyByTimeSpan(@RequestParam(value = "span") String span
    		, @RequestParam(value="idcType",required = false) String idcType);
    
    /**
     * 查设备top10数据
     * @param idcType
     * @param deviceType
     * @param alertLevel
     * @param colName
     * @return
     */
    @GetMapping(value = "/alertDeviceTop10", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "告警设备相关top10", notes = "告警类型分布查询", response = ComAlertsTop10DTOResponse.class, tags = {"AlertIndexPage API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = ComAlertsTop10DTOResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ComAlertsTop10DTOResponse.class)})
	List<ComAlertsTop10DTOResponse> alertDeviceTop10(@RequestParam(value="idcType",required = false)String idcType
			, @RequestParam(value="deviceType",required = false)String deviceType,
                                                     @RequestParam(value="alertLevel",required = false)String alertLevel, @RequestParam(value="colName") String colName);
    
    
    /**
     * 查内容top10数据
     * @param startDate
     * @param endDate
     * @param idcType
     * @param deviceType
     * @param alertLevel
     * @return
     */
    @GetMapping(value = "/alertContentTop10", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "告警内容top10", notes = "告警类型分布查询", response = ComAlertsTop10DTOResponse.class, tags = {"AlertIndexPage API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = ComAlertsTop10DTOResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ComAlertsTop10DTOResponse.class)})
	PageResponse<ComAlertsTop10DTOResponse> alertContentTop10(@RequestParam(value="startDate" ,required = false)String startDate
			, @RequestParam(value="endDate",required = false)String endDate, @RequestParam(value="idcType",required = false)String idcType
			, @RequestParam(value="deviceType",required = false)String deviceType,
			@RequestParam(value="alertLevel" ,required = false)String alertLevel);

    @GetMapping(value = "/latest", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "最新热点告警", notes = "告警类型分布查询", response = CompAlertsDetailResp.class, tags = {"AlertIndexPage API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = CompAlertsDetailResp.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = CompAlertsDetailResp.class)})
    List<CompAlertsDetailResp> latest(@RequestParam(value = "operateStatus", required = false) Integer operateStatus,
                                      @RequestParam(value = "startDate", required = false) String startDate,
                                      @RequestParam(value = "endDate", required = false) String endDate);
    
    /**
     * 导出警报列表数据
     * @param response
     */
    @ApiOperation(value = "导出最新热点告警数据", notes = "导出警报列表数据", tags = {"AlertIndexPage API"})
    @RequestMapping(value = "/exportLatest", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    void exportLatest(@RequestParam(value = "operateStatus", required = false) Integer operateStatus,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate, HttpServletResponse response) throws Exception;

    @GetMapping(value = "/selectAlertsByOperateStatus", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "资源池，设备类型告警", notes = "资源池，设备类型告警", response = CompAlertsDetailResp.class, tags = {"AlertIndexPage API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = CompAlertsDetailResp.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = CompAlertsDetailResp.class)})
    List<Map<String,Object>> selectAlertsByOperateStatus(@RequestParam(value = "operateStatus", required = false) Integer operateStatus,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "colType", required = false) String colType);

    /**
     * 告警处理时长-资源池分布
     * @param alertLevel
     * @return
     * @throws ParseException
     */
    @GetMapping(value = "/alertIdcDoHours", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "告警处理时长-资源池分布", notes = "告警处理时长-资源池分布",
            response = Map.class, tags = {"AlertIndexPage API"})
    List<Map<String, Object>> alertIdcDoHours(@RequestParam(value = "alertLevel") String alertLevel);

}
