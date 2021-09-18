package com.aspire.mirror.composite.service.inspectionDaily;

import com.aspire.mirror.composite.payload.inspectionDaily.ComAlertInspectionDailyReq;
import com.aspire.mirror.composite.payload.inspectionDaily.ComAlertInspectionDailyResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aspire.mirror.common.entity.PageResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 历史告警暴露服务
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.alert
 * 类名称:    ICompAlertsHisService.java
 * 类描述:    历史告警暴露服务
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 20:03
 * 版本:      v1.0
 */
@Api(value = "历史告警信息管理")
@RequestMapping("/${version}/alerts/report/")
public interface ICompAlertsResourcePoolService {
	/**
	 * 查询资源池告警列表
	 */
	@PostMapping(value = "/inspection_daily", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "列表", notes = "获取历史告警列表", tags = { "ResourcePool" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = PageResponse.class),
			@ApiResponse(code = 500, message = "内部错误") })
	PageResponse<ComAlertInspectionDailyResponse> inspectionDaily(@RequestBody ComAlertInspectionDailyReq pageRequset);

	/**
	 * 查询资源池告警列表
	 */
	@PostMapping(value = "/exportDaily", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "列表", notes = "获取历史告警列表", tags = { "ResourcePool" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = PageResponse.class),
			@ApiResponse(code = 500, message = "内部错误") })
	void exportDaily(@RequestBody ComAlertInspectionDailyReq pageRequest);

}
