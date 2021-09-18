package com.aspire.mirror.alert.api.service.third;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import com.aspire.mirror.alert.api.dto.monthReport.AlertsLevelCountsResponse;
import com.aspire.mirror.alert.api.dto.third.AlertThirdRequest;
import com.aspire.mirror.alert.api.dto.third.AlertsCountDTO;
import com.aspire.mirror.alert.api.dto.third.AlertsDeviceCountsDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.aspire.mirror.common.entity.PageResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 历史告警服务
 * <p>
 * 项目名称: mirror平台 包: com.aspire.mirror.alert.api.metrics 类名称:
 * AlertsHisService.java 类描述: 历史告警服务 创建人: JinSu 创建时间: 2018/9/19 11:19 版本: v1.0
 */
@RequestMapping("/${version}/alerts/")
public interface AlertMonitorService {

	/**
	 * 查询告警过滤详情
	 *
	 */
	@GetMapping(value = "visualization/info")
	@ApiOperation(value = "展示信息港资源池待解决和已解决告警总数", notes = "展示信息港资源池待解决和已解决告警总数", tags = { "ThirdAlertMonitorController" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "内部错误") })
	AlertsCountDTO idcTypeAlertCount(@RequestParam("idc_type")String idcType);
	
	@GetMapping(value = "visualization/toBeResolved")
	@ApiOperation(value = "待解决告警分类统计", notes = "待解决告警分类统计", tags = { "ThirdAlertMonitorController" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "内部错误") })
	AlertsLevelCountsResponse toBeResolvedLevalCounts(@RequestParam("idc_type")String idcType);

	/**
	 * 新增告警过滤
	 */
	@GetMapping(value = "visualization/type")
	@ApiOperation(value = "设备告警分类统计", notes = "设备告警分类统计", response = ResponseEntity.class, tags = { "ThirdAlertMonitorController" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	AlertsDeviceCountsDTO deviceLevelCount() throws Exception;

	/**
	 * 修改告警过滤
	 * 
	 */

	@GetMapping(value = "visualization/ratio")
	@ApiOperation(value = "TOP10租户资源利用率", notes = "TOP10租户资源利用率", response = ResponseEntity.class, tags = { "ThirdAlertMonitorController" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	List<Map<String,String>> department1Rate() throws Exception;

	//直真接口
	@GetMapping(value = "visualization/idcTypeUserRate")
	@ApiOperation(value = "p1.1:4资源池设备数量统计", notes = "p1.1:4资源池设备数量统计", response = ResponseEntity.class, tags = { "ThirdAlertMonitorController" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	Map<String, List<Map<String, Object>>> idcTypeUserRate() throws ParseException;

	@GetMapping(value = "visualization/bizSystemTopUseRate")
	@ApiOperation(value = "p2.6资源利用率", notes = "p2.6资源利用率", response = ResponseEntity.class, tags = { "ThirdAlertMonitorController" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	List<Map<String, Object>> bizSystemTopUseRate(@RequestParam("idcType")String idcType) throws ParseException;

	@PostMapping(value = "visualization/bizSystemLowUseRate")
	@ApiOperation(value = "p1.2/4低利用率应用系统", notes = "p1.2/4低利用率应用系统", response = ResponseEntity.class, tags = { "ThirdAlertMonitorController" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	List<Map<String, Object>> bizSystemLowUseRate(@RequestBody AlertThirdRequest params);

	@PostMapping(value = "visualization/bizSystemLowUseRateTopN")
	@ApiOperation(value = "p1.2/4/1低利用率应用系统", notes = "p1.2/4/1低利用率应用系统", response = ResponseEntity.class, tags = { "ThirdAlertMonitorController" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	List<Map<String, Object>> bizSystemLowUseRateTopN(@RequestBody AlertThirdRequest params);

	@PostMapping(value = "visualization/bizSysDayType2TopN")
	@ApiOperation(value = "p1.2/3/1设备资源利用率", notes = "p1.2/3/1设备资源利用率", response = ResponseEntity.class, tags = { "ThirdAlertMonitorController" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	List<Map<String, Object>> bizSysDayType2TopN(@RequestBody AlertThirdRequest params);

	@PostMapping(value = "visualization/bizSysDayType2UseRate")
	@ApiOperation(value = "p1.2/3设备资源利用率", notes = "p1.2/3设备资源利用率", response = ResponseEntity.class, tags = { "ThirdAlertMonitorController" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	List<Map<String, Object>> bizSysDayType2UseRate(@RequestBody AlertThirdRequest params) throws Exception;


	@PostMapping(value = "kpi_score/list")
	@ApiOperation(value = "查询性能打分数据", notes = "查询性能打分数据", response = ResponseEntity.class, tags = { "ThirdAlertMonitorController" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	PageResponse<Map<String, Object>> getPoorEfficiencyDevice(@RequestBody Map<String, Object> params);

}
