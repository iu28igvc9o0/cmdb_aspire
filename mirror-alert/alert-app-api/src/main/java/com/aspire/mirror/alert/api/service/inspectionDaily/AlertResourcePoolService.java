package com.aspire.mirror.alert.api.service.inspectionDaily;

import java.util.List;
import java.util.Map;

import com.aspire.mirror.alert.api.dto.inspectionDaily.AlertInspectionDailyReq;
import com.aspire.mirror.alert.api.dto.inspectionDaily.AlertInspectionDailyResponse;
import com.aspire.mirror.alert.api.dto.inspectionDaily.AlertRourcePoolReq;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
public interface AlertResourcePoolService {

	/**
	 * 查询资源池告警列表
	 */
	@PostMapping(value = "/v1/alerts/report/inspection_daily", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "列表", notes = "获取历史告警列表", tags = { "ResourcePool" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = PageResponse.class),
			@ApiResponse(code = 500, message = "内部错误") })
	PageResponse<AlertInspectionDailyResponse> inspectionDaily(@RequestBody AlertInspectionDailyReq pageRequset);

	/**
	 * 查询资源池告警列表
	 */
	@PostMapping(value = "/v1/alerts/report/exportDaily", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "列表", notes = "获取历史告警列表", tags = { "ResourcePool" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = PageResponse.class),
			@ApiResponse(code = 500, message = "内部错误") })
	List<Map<String, Object>> exportDaily(@RequestBody AlertInspectionDailyReq pageRequest);

	/**
	 * 查询资源池告警列表
	 */
	@PostMapping(value = "/v1/alerts/report/syncDeviceTop10Alert")
	@ApiOperation(value = "列表", notes = "获取历史告警列表", tags = { "ResourcePool" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = PageResponse.class),
			@ApiResponse(code = 500, message = "内部错误") })
	ResponseEntity<String> syncDeviceTop10Alert(@RequestBody AlertRourcePoolReq pageRequset);

	@PostMapping(value = "/v1/alerts/report/syncMoniterTop10Alert")
	@ApiOperation(value = "列表", notes = "获取历史告警列表", tags = { "ResourcePool" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = PageResponse.class),
			@ApiResponse(code = 500, message = "内部错误") })
	ResponseEntity<String> syncMoniterTop10Alert(@RequestBody AlertRourcePoolReq pageRequset);

	@PostMapping(value = "/v1/alerts/report/syncDistributionAlert")
	@ApiOperation(value = "列表", notes = "获取历史告警列表", tags = { "ResourcePool" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = PageResponse.class),
			@ApiResponse(code = 500, message = "内部错误") })
	ResponseEntity<String> syncDistributionAlert(@RequestBody AlertRourcePoolReq pageRequset);

	@PostMapping(value = "/v1/alerts/report/synResourcePoolAlert")
	@ApiOperation(value = "列表", notes = "获取历史告警列表", tags = { "ResourcePool" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = PageResponse.class),
			@ApiResponse(code = 500, message = "内部错误") })
	ResponseEntity<String> synResourcePoolAlert(@RequestBody AlertRourcePoolReq pageRequset);

}
