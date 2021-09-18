package com.aspire.mirror.alert.api.service.alert;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.aspire.mirror.alert.api.dto.alert.AlertStatisticSummaryResponse;
import com.aspire.mirror.alert.api.dto.alert.AlertsDetailResponse;
import com.aspire.mirror.alert.api.dto.alert.AlertStatisticSummaryDTO;
import com.aspire.mirror.alert.api.dto.alert.AlertsStatisticClassifyDTO;
import com.aspire.mirror.alert.api.dto.alert.AlertsTop10DTOResponse;
import com.aspire.mirror.common.entity.PageResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "告警统计")
@RequestMapping(value = "/v1/alerts/indexPage/")
public interface AlertIndexPageService {
    /**
     * 待解决告警、已解决告警
     *
     * @return AlertStatisticSummaryResponse 告警总览
     */
    @GetMapping(value = "/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "待解决和已解决告警", notes = "告警总览", 
    				response = AlertStatisticSummaryResponse.class, tags = {"AlertIndexPage API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = AlertStatisticSummaryResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertStatisticSummaryResponse.class)})
    AlertStatisticSummaryResponse summary(@RequestParam(value="idcType",required = false) String idcType);




    /**
     * 告警级别分布查询
     *
     * @return AlertStatisticSummaryDTO 告警级别总览
     */
    @GetMapping(value = "/level-span", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "告警级别分布查询", notes = "告警级别分布查询", 
    				response = AlertStatisticSummaryDTO.class, tags = {"AlertIndexPage API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = AlertStatisticSummaryDTO.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertStatisticSummaryDTO.class)})
    AlertStatisticSummaryDTO alertLevelSummayByTimeSpan(@RequestParam(value = "span") String span
    		,@RequestParam(value="idcType",required = false) String idcType);
    /**
     * 告警级别趋势查询
     * @param inteval
     * @return
     */
    @GetMapping(value = "/trend", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "告警级别趋势查询", notes = "告警级别趋势查询", response = Map.class, tags = {"AlertIndexPage API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = Map.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertStatisticSummaryDTO.class)})
    Map trend(@RequestParam(value = "span") String inteval,
    		@RequestParam(value = "idcType", required = false) String idcType,
    		@RequestParam(value = "deviceType", required = false) String deviceType,
    		@RequestParam(value = "alertLevel", required = false) String alertLevel,
    		@RequestParam(value = "source", required = false) String source);

   


    /**
     * 告警类型分布查询
     * @return
     * @throws ParseException
     */
    @GetMapping(value = "/classify-span", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "告警类型分布查询", notes = "告警类型分布查询", 
    				response = AlertsStatisticClassifyDTO.class, tags = {"AlertIndexPage API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = AlertsStatisticClassifyDTO.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertsStatisticClassifyDTO.class)})
    List<AlertsStatisticClassifyDTO> classifyByTimeSpan(@RequestParam(value = "span") String span
    		,@RequestParam(value="idcType",required = false) String idcType);


    
    /**
     * 查top10数据
     * @param idcType
     * @param deviceType
     * @param alertLevel
     * @param colName
     * @return
     */
    @GetMapping(value = "/alertTop10", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "告警设容相关top10查询", notes = "告警类型分布查询", 
    				response = AlertsStatisticClassifyDTO.class, tags = {"AlertIndexPage API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = AlertsStatisticClassifyDTO.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertsStatisticClassifyDTO.class)})
	List<AlertsTop10DTOResponse> alertTop10( @RequestParam(value="idcType",required = false)String idcType
			, @RequestParam(value="deviceType",required = false)String deviceType,
			@RequestParam(value="alertLevel",required = false)String alertLevel,@RequestParam(value="colName") String colName);



    @GetMapping(value = "/alertMoniIndexTop10", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "告警内容相关top10查询", notes = "告警类型分布查询", 
    				response = AlertsStatisticClassifyDTO.class, tags = {"AlertIndexPage API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = AlertsStatisticClassifyDTO.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertsStatisticClassifyDTO.class)})
	PageResponse<AlertsTop10DTOResponse> alertMoniIndexTop10(
			@RequestParam(value = "startDate", required = false)String startDate, 
			@RequestParam(value = "endDate", required = false)String endDate, 
			@RequestParam(value = "idcType", required = false)String idcType, 
			@RequestParam(value = "deviceType", required = false)String deviceType,
			@RequestParam(value = "alertLevel", required = false)String alertLevel);



    @GetMapping(value = "/latest", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "最新热点告警", notes = "告警类型分布查询", 
    				response = AlertsStatisticClassifyDTO.class, tags = {"AlertIndexPage API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = AlertsDetailResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertsDetailResponse.class)})
	List<AlertsDetailResponse> latest(@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "idcType", required = false) String idcType
			,@RequestParam(value = "operateStatus", required = false) Integer operateStatus,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate);
    
    
    @GetMapping(value = "/exportLatest", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "最新热点告警导出", notes = "告警类型分布查询", 
    				response = AlertsStatisticClassifyDTO.class, tags = {"AlertIndexPage API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = AlertsDetailResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertsDetailResponse.class)})
	List<Map<String, Object>> exportLatest(@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "idcType", required = false) String idcType
			,@RequestParam(value = "operateStatus", required = false) Integer operateStatus,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate) throws Exception;




    @GetMapping(value = "/StorageUseRate", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询磁盘使用率", notes = "告警类型分布查询", 
    				response = AlertsStatisticClassifyDTO.class, tags = {"AlertIndexPage API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = AlertsDetailResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertsDetailResponse.class)})
	Map<String, Object> StorageUseRate();



    @GetMapping(value = "/selectAlertsByOperateStatus", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "资源池，设备类型告警", notes = "资源池，设备类型告警", 
    				response = AlertsStatisticClassifyDTO.class, tags = {"AlertIndexPage API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = AlertsDetailResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertsDetailResponse.class)})
	List<Map<String, Object>> selectAlertsByOperateStatus(@RequestParam(value = "operateStatus", required = false) Integer operateStatus,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate
			,@RequestParam(value = "colType", required = false) String colType);

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
