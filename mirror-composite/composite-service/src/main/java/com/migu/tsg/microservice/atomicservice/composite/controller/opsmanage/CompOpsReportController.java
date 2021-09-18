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
package com.migu.tsg.microservice.atomicservice.composite.controller.opsmanage;

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

import com.aspire.mirror.composite.service.opsmanage.ICompOpsReportService;
import com.aspire.mirror.ops.api.domain.IndexPageDayRunTrendItem;
import com.aspire.mirror.ops.api.domain.IndexPageHostStatistic;
import com.aspire.mirror.ops.api.domain.IndexPagePipelineJobStatistic;
import com.aspire.mirror.ops.api.domain.IndexPagePipelineStatistic;
import com.aspire.mirror.ops.api.domain.IndexPageRecent30DaysRunStatistic;
import com.aspire.mirror.ops.api.domain.IndexPageRunTimeSpanStatistic;
import com.aspire.mirror.ops.api.domain.IndexReportExceptionPipeInstQueryModel;
import com.aspire.mirror.ops.api.domain.OpsPipelineInstanceDTO;
import com.aspire.mirror.ops.api.domain.PageListQueryResult;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.opsmanage.OpsReportManageClient;

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
public class CompOpsReportController implements ICompOpsReportService {
	@Autowired
	private OpsReportManageClient reportClient;

	@Override
	@ResponseStatus(HttpStatus.OK)
	public IndexPageHostStatistic indexHostStatistic() {
		return reportClient.indexHostStatistic();
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public IndexPagePipelineStatistic indexPipelineStatistic() {
		return reportClient.indexPipelineStatistic();
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public IndexPageRecent30DaysRunStatistic indexRecent30DaysRunStatistic() {
		return reportClient.indexRecent30DaysRunStatistic();
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public IndexPagePipelineJobStatistic indexPipelineJobStatistic() {
		return reportClient.indexPipelineJobStatistic();
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public List<IndexPageDayRunTrendItem> indexRecent30DaysRunTrend() {
		return reportClient.indexRecent30DaysRunTrend();
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public IndexPageRunTimeSpanStatistic indexRunTimeSpanStatistic() {
		return reportClient.indexRunTimeSpanStatistic();
	}
	
	////for new index page 
	@Override
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Integer> newIndexAgentStatistic() {
		return reportClient.newIndexAgentStatistic();
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Integer> newIndexPipeInstStatistic7Days() {
		return reportClient.newIndexPipeInstStatistic7Days();
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Integer> newIndexInspectionTaskStatistic7Days() {
		return reportClient.newIndexInspectionTaskStatistic7Days();
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Integer> newIndexAutoRepairInstanceStatistic7Days() {
		return reportClient.newIndexAutoRepairInstanceStatistic7Days();
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Integer> newIndexAllTypeTasksInAllStatistic30Days() {
		return reportClient.newIndexAllTypeTasksInAllStatistic30Days();
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public List<IndexPageDayRunTrendItem> newIndexPipeInstStatisticByLine7Days() {
		return reportClient.newIndexPipeInstStatisticByLine7Days();
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Integer> newIndexAllTaskTypeStatisticByPie7Days() {
		return reportClient.newIndexAllTaskTypeStatisticByPie7Days();
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Integer> newIndexPipeInstCostTimeStatistic7Days() {
		return reportClient.newIndexPipeInstCostTimeStatistic7Days();
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public PageListQueryResult<OpsPipelineInstanceDTO> 
			newIndexExceptionPipeInstPageList(@RequestBody IndexReportExceptionPipeInstQueryModel queryParam) {
		return reportClient.newIndexExceptionPipeInstPageList(queryParam);
	}
	@Override
	public List<OpsPipelineScenes> normalScenesStatistic() {
		return reportClient.normalScenesStatistic();
	}

	@Override
	public TodayOpsTaskStatistic todayOpsTaskStatistic() {
		return reportClient.todayOpsTaskStatistic();
	}

	@Override
	public TodayOpsTaskStatusStatistic todayOpsTaskStatusStatistic() {
		return reportClient.todayOpsTaskStatusStatistic();
	}

	@Override
	public OpsSuccessRateStatistic taskExec7DaySuccessedRate() {
		return reportClient.taskExec7DaySuccessedRate();
	}

	@Override
	public PageListQueryResult<OpsToBeProcessedTaskStatistic> toBeProcessedTaskList(@RequestBody OpsToBeProcessedTaskQueryModel queryModel) {
		return reportClient.toBeProcessedTaskList(queryModel);
	}
}
