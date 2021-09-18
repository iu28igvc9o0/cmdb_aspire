/**
 *
 * 项目名： ops-service 
 * <p/> 
 *
 * 文件名:  OpsReportController.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月28日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.controller;

import java.util.List;
import java.util.Map;

import com.aspire.mirror.ops.api.domain.OpsPipelineScenes;
import com.aspire.mirror.ops.api.domain.OpsSuccessRateStatistic;
import com.aspire.mirror.ops.api.domain.OpsToBeProcessedTaskQueryModel;
import com.aspire.mirror.ops.api.domain.OpsToBeProcessedTaskStatistic;
import com.aspire.mirror.ops.api.domain.TodayOpsTaskStatistic;
import com.aspire.mirror.ops.api.domain.TodayOpsTaskStatusStatistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.ops.api.domain.IndexPageDayRunTrendItem;
import com.aspire.mirror.ops.api.domain.IndexPageHostStatistic;
import com.aspire.mirror.ops.api.domain.IndexPagePipelineJobStatistic;
import com.aspire.mirror.ops.api.domain.IndexPagePipelineStatistic;
import com.aspire.mirror.ops.api.domain.IndexPageRecent30DaysRunStatistic;
import com.aspire.mirror.ops.api.domain.IndexPageRunTimeSpanStatistic;
import com.aspire.mirror.ops.api.domain.IndexReportExceptionPipeInstQueryModel;
import com.aspire.mirror.ops.api.domain.OpsPipelineInstanceDTO;
import com.aspire.mirror.ops.api.domain.PageListQueryResult;
import com.aspire.mirror.ops.api.service.IOpsReportService;
import com.aspire.mirror.ops.biz.OpsReportBiz;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: OpsReportController
 * <p/>
 *
 * 类功能描述: 报表服务controller
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月28日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@RestController
public class OpsReportController implements IOpsReportService {
	@Autowired
	private OpsReportBiz reportBiz;

	@Override
	@ResponseStatus(HttpStatus.OK)
	public IndexPageHostStatistic indexHostStatistic() {
		return reportBiz.indexHostStatistic();
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public IndexPagePipelineStatistic indexPipelineStatistic() {
		return reportBiz.indexPipelineStatistic();
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public IndexPageRecent30DaysRunStatistic indexRecent30DaysRunStatistic() {
		return reportBiz.indexRecent30DaysRunStatistic();
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public IndexPagePipelineJobStatistic indexPipelineJobStatistic() {
		return reportBiz.indexPipelineJobStatistic();
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public List<IndexPageDayRunTrendItem> indexRecent30DaysRunTrend() {
		return reportBiz.indexRecent30DaysRunTrend();
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public IndexPageRunTimeSpanStatistic indexRunTimeSpanStatistic() {
		return reportBiz.indexRunTimeSpanStatistic(30);
	}
	
	////  for new index page 
	@Override
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Integer> newIndexAgentStatistic() {
		return reportBiz.newIndexAgentStatistic();
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Integer> newIndexPipeInstStatistic7Days() {
		return reportBiz.newIndexPipeInstStatistic(7);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Integer> newIndexInspectionTaskStatistic7Days() {
		return reportBiz.newIndexInspectionTaskStatistic(7);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Integer> newIndexAutoRepairInstanceStatistic7Days() {
		return reportBiz.newIndexAutoRepairInstanceStatistic(7);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Integer> newIndexAllTypeTasksInAllStatistic30Days() {
		return reportBiz.newIndexAllTypeTasksInAllStatistic(30);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public List<IndexPageDayRunTrendItem> newIndexPipeInstStatisticByLine7Days() {
		return reportBiz.newIndexPipeInstStatisticByLine(7);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Integer> newIndexAllTaskTypeStatisticByPie7Days() {
		return reportBiz.newIndexAllTaskTypeStatisticByPie(7);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Integer> newIndexPipeInstCostTimeStatistic7Days() {
		return reportBiz.newIndexPipeInstCostTimeStatistic(7);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public PageListQueryResult<OpsPipelineInstanceDTO> 
			newIndexExceptionPipeInstPageList(@RequestBody IndexReportExceptionPipeInstQueryModel queryParam) {
		return reportBiz.newIndexExceptionPipeInstPageList(queryParam);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public List<OpsPipelineScenes> normalScenesStatistic() {
		return reportBiz.normalScenesStatistic(6);
	}

	@Override
	public TodayOpsTaskStatistic todayOpsTaskStatistic() {
		return reportBiz.todayOpsTaskStatistic();
	}

	@Override
	public TodayOpsTaskStatusStatistic todayOpsTaskStatusStatistic() {
		return reportBiz.todayOpsTaskStatusStatistic();
	}

	@Override
	public OpsSuccessRateStatistic taskExec7DaySuccessedRate() {
		return reportBiz.taskExecSuccessedRate(7);
	}

	@Override
	public PageListQueryResult<OpsToBeProcessedTaskStatistic> toBeProcessedTaskList(@RequestBody OpsToBeProcessedTaskQueryModel queryModel) {
		return reportBiz.queryToBeProcessedTaskList(queryModel);
	}

}
