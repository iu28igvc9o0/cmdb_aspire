package com.aspire.mirror.ops.api.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aspire.mirror.ops.api.domain.IndexPageDayRunTrendItem;
import com.aspire.mirror.ops.api.domain.IndexPageHostStatistic;
import com.aspire.mirror.ops.api.domain.IndexPagePipelineJobStatistic;
import com.aspire.mirror.ops.api.domain.IndexPagePipelineStatistic;
import com.aspire.mirror.ops.api.domain.IndexPageRecent30DaysRunStatistic;
import com.aspire.mirror.ops.api.domain.IndexPageRunTimeSpanStatistic;
import com.aspire.mirror.ops.api.domain.IndexReportExceptionPipeInstQueryModel;
import com.aspire.mirror.ops.api.domain.OpsPipelineInstanceDTO;
import com.aspire.mirror.ops.api.domain.OpsPipelineScenes;
import com.aspire.mirror.ops.api.domain.OpsSuccessRateStatistic;
import com.aspire.mirror.ops.api.domain.OpsToBeProcessedTaskQueryModel;
import com.aspire.mirror.ops.api.domain.OpsToBeProcessedTaskStatistic;
import com.aspire.mirror.ops.api.domain.PageListQueryResult;
import com.aspire.mirror.ops.api.domain.TodayOpsTaskStatistic;
import com.aspire.mirror.ops.api.domain.TodayOpsTaskStatusStatistic;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/** 
 *
 * 项目名称: ops-api 
 * <p/>
 * 
 * 类名: IOpsManageService
 * <p/>
 *
 * 类功能描述: Ops报表接口
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月30日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Api(value = "ops报表服务")
@RequestMapping(value = "/v1/ops-service/opsReport/")
public interface IOpsReportService {
	
	@GetMapping(value = "/indexHostStatistic", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "首页主机统计", notes = "首页主机统计", tags = {"Ops Report service API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "返回", response=IndexPageHostStatistic.class), 
    		              @ApiResponse(code = 500, message = "Unexpected error")})
	IndexPageHostStatistic indexHostStatistic();
	
	@GetMapping(value = "/indexPipelineStatistic", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "首页常用作业统计", notes = "首页常用作业统计", tags = {"Ops Report service API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "返回", response=IndexPagePipelineStatistic.class), 
    		              @ApiResponse(code = 500, message = "Unexpected error")})
	IndexPagePipelineStatistic indexPipelineStatistic();
	
	@GetMapping(value = "/indexRecent30DaysRunStatistic", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "近30天任务数统计", notes = "近30天任务数统计", tags = {"Ops Report service API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "返回", response=IndexPageRecent30DaysRunStatistic.class), 
    		              @ApiResponse(code = 500, message = "Unexpected error")})
	IndexPageRecent30DaysRunStatistic indexRecent30DaysRunStatistic();
	
	@GetMapping(value = "/indexPipelineJobStatistic", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "定时任务数统计", notes = "定时任务数统计", tags = {"Ops Report service API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "返回", response=IndexPagePipelineJobStatistic.class), 
    		              @ApiResponse(code = 500, message = "Unexpected error")})
	IndexPagePipelineJobStatistic indexPipelineJobStatistic();
	
	@GetMapping(value = "/indexRecent30DaysRunTrend", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "近30天任务执行趋势统计", notes = "近30天任务执行趋势统计", tags = {"Ops Report service API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "返回", response=IndexPageDayRunTrendItem.class), 
    		              @ApiResponse(code = 500, message = "Unexpected error")})
	List<IndexPageDayRunTrendItem> indexRecent30DaysRunTrend();
	
	@GetMapping(value = "/indexRunTimeSpanStatistic", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "任务耗时区间统计", notes = "任务耗时区间统计", tags = {"Ops Report service API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "返回", response=IndexPageRunTimeSpanStatistic.class),
    		              @ApiResponse(code = 500, message = "Unexpected error")})
	IndexPageRunTimeSpanStatistic indexRunTimeSpanStatistic();

	@GetMapping(value = "/normalScenesStatistic", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "常用场景列表", notes = "常用场景列表", tags = {"Ops Report service API"})
	@ApiResponses(value = {@ApiResponse(code = 204, message = "返回", response=OpsPipelineScenes.class),
			@ApiResponse(code = 500, message = "Unexpected error")})
	List<OpsPipelineScenes> normalScenesStatistic();

	@GetMapping(value = "/todayOpsTaskStatistic", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "今日任务统计", notes = "今日任务统计", tags = {"Ops Report service API"})
	@ApiResponses(value = {@ApiResponse(code = 204, message = "返回", response=IndexPageRunTimeSpanStatistic.class),
			@ApiResponse(code = 500, message = "Unexpected error")})
	TodayOpsTaskStatistic todayOpsTaskStatistic();

	@GetMapping(value = "/todayOpsTaskStatusStatistic", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "当日任务执行状态", notes = "当日任务执行状态", tags = {"Ops Report service API"})
	@ApiResponses(value = {@ApiResponse(code = 204, message = "返回", response=IndexPageRunTimeSpanStatistic.class),
			@ApiResponse(code = 500, message = "Unexpected error")})
	TodayOpsTaskStatusStatistic todayOpsTaskStatusStatistic();

	@GetMapping(value = "/taskExec7DaySuccessedRate", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "七日任务成功率", notes = "七日任务成功率", tags = {"Ops Report service API"})
	@ApiResponses(value = {@ApiResponse(code = 204, message = "返回", response=OpsSuccessRateStatistic.class),
			@ApiResponse(code = 500, message = "Unexpected error")})
	OpsSuccessRateStatistic taskExec7DaySuccessedRate();

	@PostMapping(value = "/toBeProcessedTaskList", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "待处理任务", notes = "待处理任务", tags = {"Ops Report service API"})
	@ApiResponses(value = {@ApiResponse(code = 204, message = "返回", response=OpsToBeProcessedTaskStatistic.class),
			@ApiResponse(code = 500, message = "Unexpected error")})
	PageListQueryResult<OpsToBeProcessedTaskStatistic> toBeProcessedTaskList(@RequestBody OpsToBeProcessedTaskQueryModel queryModel);

	///// for new index page
	@GetMapping(value = "/newIndexAgentStatistic", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "自动化对象数量", notes = "自动化对象数量", tags = {"Ops Report service API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "返回", response=Map.class), 
    		              @ApiResponse(code = 500, message = "Unexpected error")})
	public Map<String, Integer> newIndexAgentStatistic();
	
	@GetMapping(value = "/newIndexPipeInstStatistic7Days", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "7日自动化任务", notes = "7日自动化任务", tags = {"Ops Report service API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "返回", response=Map.class), 
    		              @ApiResponse(code = 500, message = "Unexpected error")})
	public Map<String, Integer> newIndexPipeInstStatistic7Days();
	
	@GetMapping(value = "/newIndexInspectionTaskStatistic7Days", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "7日自动巡检任务", notes = "7日自动巡检任务", tags = {"Ops Report service API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "返回", response=Map.class), 
    		              @ApiResponse(code = 500, message = "Unexpected error")})
	public Map<String, Integer> newIndexInspectionTaskStatistic7Days();
	
	@GetMapping(value = "/newIndexAutoRepairInstanceStatistic7Days", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "7日故障自愈任务", notes = "7日故障自愈任务", tags = {"Ops Report service API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "返回", response=Map.class), 
    		              @ApiResponse(code = 500, message = "Unexpected error")})
	public Map<String, Integer> newIndexAutoRepairInstanceStatistic7Days();
	
	
	@GetMapping(value = "/newIndexAllTasksStatistic30Days", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "30日内任务总数", notes = "30日内任务总数", tags = {"Ops Report service API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "返回", response=Map.class), 
    		              @ApiResponse(code = 500, message = "Unexpected error")})
	public Map<String, Integer> newIndexAllTypeTasksInAllStatistic30Days();
	
	
	@GetMapping(value = "/newIndexPipeInstStatisticByLine7Days", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "7天任务执行情况", notes = "7天任务执行情况", tags = {"Ops Report service API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "返回", response=Map.class), 
    		              @ApiResponse(code = 500, message = "Unexpected error")})
	public List<IndexPageDayRunTrendItem> newIndexPipeInstStatisticByLine7Days();
	
	
	@GetMapping(value = "/newIndexAllTaskTypeStatisticByPie7Days", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "7天各类型任务分布", notes = "7天各类型任务分布", tags = {"Ops Report service API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "返回", response=Map.class), 
    		              @ApiResponse(code = 500, message = "Unexpected error")})
	public Map<String, Integer> newIndexAllTaskTypeStatisticByPie7Days();
	
	
	@GetMapping(value = "/newIndexPipeInstCostTimeStatistic7Days", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "7天任务执行时长分布", notes = "7天任务执行时长分布", tags = {"Ops Report service API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "返回", response=Map.class), 
    		              @ApiResponse(code = 500, message = "Unexpected error")})
	public Map<String, Integer> newIndexPipeInstCostTimeStatistic7Days();
	
	
	@PostMapping(value = "/newIndexExceptionPipeInstPageList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "任务执行异常列表", notes = "任务执行异常列表", tags = {"Ops Report service API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "返回", response=OpsPipelineInstanceDTO.class), 
    		              @ApiResponse(code = 500, message = "Unexpected error")})
	public PageListQueryResult<OpsPipelineInstanceDTO> newIndexExceptionPipeInstPageList(
												@RequestBody IndexReportExceptionPipeInstQueryModel queryParam);
}
