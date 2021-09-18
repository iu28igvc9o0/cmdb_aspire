package com.aspire.mirror.ops.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.mirror.ops.api.domain.IndexPageDayRunTrendItem.DayStausCountItem;
import com.aspire.mirror.ops.api.domain.IndexReportExceptionPipeInstQueryModel;
import com.aspire.mirror.ops.api.domain.OpsPipelineInstanceDTO;
import com.aspire.mirror.ops.api.domain.OpsPipelineScenes;
import com.aspire.mirror.ops.api.domain.OpsToBeProcessedTaskQueryModel;
import com.aspire.mirror.ops.api.domain.OpsToBeProcessedTaskStatistic;
import com.aspire.mirror.ops.api.domain.StatisticRespModel;
import com.aspire.mirror.ops.api.domain.StatusGroupCountItem;
import com.aspire.mirror.ops.domain.OpsReportGroupData;
import com.aspire.mirror.ops.domain.OpsStatisticNumBase;
import com.aspire.mirror.ops.domain.OpsTimeConsumeStatisticBase;

@Mapper
public interface OpsReportDao {
	
	Integer queryAgentHostCount();
	
	Integer queryPipelineCount();
	
	List<StatusGroupCountItem> queryPipelineInstanceStatusCount(Map<String, Object> params);
	
	List<StatusGroupCountItem> queryPipelineJobStatusCount();
	
	List<DayStausCountItem> queryStatusCountByDay(Map<String, Object> params);
	
	List<Float> queryRunTimeSpanList(Map<String, Object> params);

    List<OpsPipelineScenes> queryNormalScenes(int index);

	List<StatisticRespModel> queryPipelineInstanceTriggerWayCount(Map<String,Object> param);

	//  for new index page
	List<OpsReportGroupData> getAgentHostGroupCountByStatus();
	
	List<OpsReportGroupData> getPipeInstGroupCountByStatus(@Param("fromDay") Date fromDay);
	
	Integer getPipeInstExceptionResultCount(@Param("fromDay") Date fromDay);
	
	List<OpsReportGroupData> getPipeInstGroupCountByTriggerWay(@Param("fromDay") Date fromDay);
	
	List<OpsReportGroupData> getApInstanceGroupCountByStatus(@Param("fromDay") Date fromDay);
	
	Integer getApInstanceExceptionResultCount(@Param("fromDay") Date fromDay);
	
	Integer getApInstanceTotalCount(@Param("fromDay") Date fromDay);
	
	List<OpsPipelineInstanceDTO> queryExceptionPipeInstList(IndexReportExceptionPipeInstQueryModel queryParam);
	
	Integer queryExceptionPipeInstListTotalSize(IndexReportExceptionPipeInstQueryModel queryParam);

	OpsStatisticNumBase getOpsHistoryNumStatistic(Map<String,Object> param);

	OpsStatisticNumBase getApInstanceGroupCountStatistic(Map<String,Object> param);

	Integer getOpsHistoryDeviceNum(Map<String,Object> param);

	OpsTimeConsumeStatisticBase getOpsHistoryTimeStatistic(Map<String,Object> param);

	OpsTimeConsumeStatisticBase getApInstanceTimeStatistic(Map<String,Object> param);

	Integer queryToBeProcessedTaskListCount(OpsToBeProcessedTaskQueryModel queryModel);

	List<OpsToBeProcessedTaskStatistic> queryToBeProcessedTaskList(OpsToBeProcessedTaskQueryModel queryModel);
}
