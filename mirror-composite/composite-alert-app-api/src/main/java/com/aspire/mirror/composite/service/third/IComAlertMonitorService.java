package com.aspire.mirror.composite.service.third;

import java.util.List;
import java.util.Map;

import com.aspire.mirror.composite.payload.third.ComAlertsCountDTO;
import com.aspire.mirror.composite.payload.third.ComAlertsDeviceCountsDTO;
import com.aspire.mirror.composite.payload.third.ComAlertsLevelCountsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 历史告警服务
 * <p>
 * 项目名称: mirror平台 包: com.aspire.mirror.alert.api.metrics 类名称:
 * AlertsHisService.java 类描述: 历史告警服务 创建人: JinSu 创建时间: 2018/9/19 11:19 版本: v1.0
 */
@RequestMapping("/${version}/alerts/visualization")
public interface IComAlertMonitorService {

	/**
	 * 查询告警过滤详情
	 *
	 */
	@GetMapping(value = "/info")
	@ApiOperation(value = "展示信息港资源池待解决和已解决告警总数", notes = "展示信息港资源池待解决和已解决告警总数", tags = { "visualization" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = ComAlertsCountDTO.class),
			@ApiResponse(code = 500, message = "内部错误") })
	ComAlertsCountDTO idcTypeAlertCount(@RequestParam("idc_type")String idcType);
	
	@GetMapping(value = "/toBeResolved")
	@ApiOperation(value = "待解决告警分类统计", notes = "待解决告警分类统计", tags = { "visualization" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = ComAlertsLevelCountsResponse.class),
			@ApiResponse(code = 500, message = "内部错误") })
	ComAlertsLevelCountsResponse toBeResolvedLevalCounts(@RequestParam("idc_type")String idcType);

	/**
	 * 新增告警过滤
	 */
	@GetMapping(value = "/type")
	@ApiOperation(value = "设备告警分类统计", notes = "设备告警分类统计", response = ResponseEntity.class, tags = { "visualization" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	ComAlertsDeviceCountsDTO deviceLevelCount() throws Exception;

	/**
	 * 修改告警过滤
	 * 
	 */

	@GetMapping(value = "/ratio")
	@ApiOperation(value = "TOP10租户资源利用率", notes = "TOP10租户资源利用率", response = ResponseEntity.class, tags = { "visualization" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	 List<Map<String, String>>  department1Rate() throws Exception;

}
