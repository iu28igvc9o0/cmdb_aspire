/**
 * 项目名： ops-service
 * <p/>
 * <p>
 * 文件名:  OpsReportBiz.java
 * <p/>
 * <p>
 * 功能描述: TODO
 * <p/>
 *
 * @author pengguihua
 * @date 2019年11月28日
 * @version V1.0 <p/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 */
package com.aspire.mirror.ops.biz;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.aspire.mirror.common.util.DateUtil;
import com.aspire.mirror.ops.api.domain.AgentStatusEnum;
import com.aspire.mirror.ops.api.domain.IndexPageDayRunTrendItem;
import com.aspire.mirror.ops.api.domain.IndexPageDayRunTrendItem.DayStausCountItem;
import com.aspire.mirror.ops.api.domain.IndexPageHostStatistic;
import com.aspire.mirror.ops.api.domain.IndexPagePipelineJobStatistic;
import com.aspire.mirror.ops.api.domain.IndexPagePipelineStatistic;
import com.aspire.mirror.ops.api.domain.IndexPageRecent30DaysRunStatistic;
import com.aspire.mirror.ops.api.domain.IndexPageRunTimeSpanStatistic;
import com.aspire.mirror.ops.api.domain.IndexReportExceptionPipeInstQueryModel;
import com.aspire.mirror.ops.api.domain.OpsAutoRepairStatusEnum;
import com.aspire.mirror.ops.api.domain.OpsPipelineInstanceDTO;
import com.aspire.mirror.ops.api.domain.OpsPipelineScenes;
import com.aspire.mirror.ops.api.domain.OpsStatusEnum;
import com.aspire.mirror.ops.api.domain.OpsSuccessRateStatistic;
import com.aspire.mirror.ops.api.domain.OpsToBeProcessedTaskQueryModel;
import com.aspire.mirror.ops.api.domain.OpsToBeProcessedTaskStatistic;
import com.aspire.mirror.ops.api.domain.OpsTriggerWayEnum;
import com.aspire.mirror.ops.api.domain.PageListQueryResult;
import com.aspire.mirror.ops.api.domain.StatisticRespModel;
import com.aspire.mirror.ops.api.domain.StatusGroupCountItem;
import com.aspire.mirror.ops.api.domain.TodayOpsTaskStatistic;
import com.aspire.mirror.ops.api.domain.TodayOpsTaskStatusStatistic;
import com.aspire.mirror.ops.clientservice.InspectionServiceClient;
import com.aspire.mirror.ops.clientservice.model.InspectionCountQueryModel;
import com.aspire.mirror.ops.clientservice.model.InspectionCountResp;
import com.aspire.mirror.ops.dao.OpsReportDao;
import com.aspire.mirror.ops.domain.OpsReportGroupData;
import com.aspire.mirror.ops.domain.OpsStatisticNumBase;
import com.aspire.mirror.ops.domain.OpsTimeConsumeStatisticBase;
import com.aspire.mirror.ops.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 项目名称: ops-service
 * <p/>
 * <p>
 * 类名: OpsReportBiz
 * <p/>
 * <p>
 * 类功能描述: TODO
 * <p/>
 *
 * @author pengguihua
 * @version V1.0
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有
 * @date 2019年11月28日
 */
@Service
@Transactional
public class OpsReportBiz {
    @Autowired
    private OpsReportDao reportDao;
    @Autowired
    private InspectionServiceClient inspectionClient;

    public IndexPageHostStatistic indexHostStatistic() {
        IndexPageHostStatistic statistic = new IndexPageHostStatistic();
        statistic.setAgentCount(reportDao.queryAgentHostCount());
        return statistic;
    }

    public IndexPagePipelineStatistic indexPipelineStatistic() {
        IndexPagePipelineStatistic result = new IndexPagePipelineStatistic();
        result.setPipelineCount(reportDao.queryPipelineCount());
        return result;
    }

    public IndexPageRecent30DaysRunStatistic indexRecent30DaysRunStatistic() {
        Map<String, Object> params = new HashMap<>();
        Date thirtyDaysAgo = DateUtils.truncate(DateUtils.addDays(new Date(), -30), Calendar.DAY_OF_MONTH);
        params.put("bizClassify", OpsPipelineInstanceDTO.BIZ_CLASSIFY_OPS);
        params.put("fromDay", thirtyDaysAgo);
        List<StatusGroupCountItem> statusCountList = reportDao.queryPipelineInstanceStatusCount(params);

        final MutableInt totalCount = new MutableInt();
        Map<Integer, Integer> statusMapCount = new HashMap<>();
        statusCountList.parallelStream().forEach(item -> {
            statusMapCount.put(item.getStatus(), item.getStatusCount());
            totalCount.add(item.getStatusCount());
        });

        IndexPageRecent30DaysRunStatistic statistic = new IndexPageRecent30DaysRunStatistic();
        statistic.setRunCount(statusMapCount.get(OpsStatusEnum.STATUS_100.getStatusCode()));
        statistic.setSuccessCount(statusMapCount.get(OpsStatusEnum.STATUS_9.getStatusCode()));
        statistic.setFailCount(statusMapCount.get(OpsStatusEnum.STATUS_101.getStatusCode()));
        statistic.setTimeoutCount(statusMapCount.get(OpsStatusEnum.STATUS_102.getStatusCode()));
        statistic.setTotalCount(totalCount.getValue());
        return statistic;
    }
    
    public static void main(String[] args) {
    	Date calcDay = DateUtils.truncate(DateUtils.addDays(new Date(), -7), Calendar.DAY_OF_MONTH);
    	System.out.println(calcDay);
    	calcDay = DateUtils.truncate(DateUtils.addDays(new Date(), -30), Calendar.DAY_OF_MONTH);
    	System.out.println(calcDay);
	}

    public IndexPagePipelineJobStatistic indexPipelineJobStatistic() {
        List<StatusGroupCountItem> statusCountList = reportDao.queryPipelineJobStatusCount();
        Map<Integer, Integer> statusMapCount = new HashMap<>();
        statusCountList.parallelStream().forEach(item -> {
            statusMapCount.put(item.getStatus(), item.getStatusCount());
        });
        IndexPagePipelineJobStatistic result = new IndexPagePipelineJobStatistic();
        result.setPauseJobCount(statusMapCount.get(OpsStatusEnum.STATUS_5.getStatusCode()));
        result.setRunJobCount(statusMapCount.get(OpsStatusEnum.STATUS_100.getStatusCode()));
        return result;
    }

    public List<IndexPageDayRunTrendItem> indexRecent30DaysRunTrend() {
        final int days = -30;
        Date thirtyDaysAgo = DateUtils.truncate(DateUtils.addDays(new Date(), days), Calendar.DAY_OF_MONTH);
        Map<String, Object> params = new HashMap<>();
        params.put("bizClassify", OpsPipelineInstanceDTO.BIZ_CLASSIFY_OPS);
        params.put("fromDay", thirtyDaysAgo);
        List<DayStausCountItem> dayStatusList = reportDao.queryStatusCountByDay(params);

        List<IndexPageDayRunTrendItem> result = constructEmptyTrendDayItemList(thirtyDaysAgo, 30);
        for (DayStausCountItem dayItem : dayStatusList) {
            IndexPageDayRunTrendItem trendItem = new IndexPageDayRunTrendItem(dayItem.getTheDay());
            int idx = result.indexOf(trendItem);
            if (idx < 0) {
                result.add(trendItem);
            } else {
                trendItem = result.get(idx);
            }
            if (dayItem.getStatus().equals(OpsStatusEnum.STATUS_9.getStatusCode())) {
                trendItem.getSuccessCount().add(dayItem.getStatusCount());
            }
            trendItem.getTotalCount().add(dayItem.getStatusCount());
        }
        return result;
    }

    private List<IndexPageDayRunTrendItem> constructEmptyTrendDayItemList(Date thirtyDaysAgo, int spanDays) {
        DateFormat dateFt = new SimpleDateFormat("yyyy-MM-dd");
        List<IndexPageDayRunTrendItem> resultList = new ArrayList<IndexPageDayRunTrendItem>();
        for (int i = 0; i < spanDays; i++) {
            Date stepDay = DateUtils.addDays(thirtyDaysAgo, i);
            resultList.add(new IndexPageDayRunTrendItem(dateFt.format(stepDay)));
        }
        return resultList;
    }

    public IndexPageRunTimeSpanStatistic indexRunTimeSpanStatistic(int spanDays) {
        Date thirtyDaysAgo = DateUtils.truncate(DateUtils.addDays(new Date(), -1 * spanDays), Calendar.DAY_OF_MONTH);
        Map<String, Object> params = new HashMap<>();
//		params.put("bizClassify", OpsPipelineInstanceDTO.BIZ_CLASSIFY_OPS);
        params.put("fromDay", thirtyDaysAgo);
        List<Float> spanList = reportDao.queryRunTimeSpanList(params);
        IndexPageRunTimeSpanStatistic result = new IndexPageRunTimeSpanStatistic();
        for (Float span : spanList) {
            if (span < TimeUnit.MINUTES.toSeconds(1)) {
                result.stepInOneMinuteCount();
            } else if (span >= TimeUnit.MINUTES.toSeconds(1) && span < TimeUnit.MINUTES.toSeconds(3)) {
                result.stepAmongOne2ThreeMinutesCount();
            } else if (span >= TimeUnit.MINUTES.toSeconds(3) && span < TimeUnit.MINUTES.toSeconds(5)) {
                result.stepAmongThree2FiveMinutesCount();
            } else if (span >= TimeUnit.MINUTES.toSeconds(5) && span < TimeUnit.MINUTES.toSeconds(10)) {
                result.stepAmongFive2TenMinutesCount();
            } else if (span >= TimeUnit.MINUTES.toSeconds(10) && span < TimeUnit.MINUTES.toSeconds(30)) {
                result.stepAmongTen2ThirtyMinutesCount();
            } else if (span >= TimeUnit.MINUTES.toSeconds(30)) {
                result.stepMoreThanThirtyMinutesCount();
            }
        }
        return result;
    }

    //  for new index page

    /**
     * 功能描述: agent主机统计
     * <p>
     *
     * @return
     */
    public Map<String, Integer> newIndexAgentStatistic() {
        final MutableInt unLinkCount = new MutableInt(0);
        final MutableInt totalCount = new MutableInt(0);
        List<OpsReportGroupData> groupList = reportDao.getAgentHostGroupCountByStatus();
        for (OpsReportGroupData statusGroup : groupList) {
            if (AgentStatusEnum.UNLINK.name().equals(statusGroup.getGroupCode())) {
                unLinkCount.add(statusGroup.getGroupCount());
            }
            totalCount.add(statusGroup.getGroupCount());
        }

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("un_link", unLinkCount.getValue());        // 未连接数量
        resultMap.put("total", totalCount.getValue());            // 所有数量
        return resultMap;
    }

    /**
     * 功能描述: 作业统计
     * <p>
     *
     * @param spanDays
     * @return
     */
    public Map<String, Integer> newIndexPipeInstStatistic(int spanDays) {
        Date fromDay = DateUtils.truncate(DateUtils.addDays(new Date(), -1 * spanDays), Calendar.DAY_OF_MONTH);
        List<OpsReportGroupData> groupList = reportDao.getPipeInstGroupCountByStatus(fromDay);

        final MutableInt failCount = new MutableInt(0);
        final MutableInt succCount = new MutableInt(0);
        final MutableInt totalCount = new MutableInt(0);
        for (OpsReportGroupData statusGroup : groupList) {
            OpsStatusEnum statusEnum = OpsStatusEnum.of(Integer.valueOf(statusGroup.getGroupCode()));
            if (OpsStatusEnum.STATUS_101 == statusEnum || OpsStatusEnum.STATUS_102 == statusEnum
                    || OpsStatusEnum.STATUS_7 == statusEnum) {
                failCount.add(statusGroup.getGroupCount());
            } 
            else if (OpsStatusEnum.STATUS_9 == statusEnum) {
                succCount.add(statusGroup.getGroupCount());
            }
            totalCount.add(statusGroup.getGroupCount());
        }

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("fail", failCount.getValue());           // 失败数量
        resultMap.put("success", succCount.getValue());        // 成功数量
        resultMap.put("total", totalCount.getValue());         // 所有数量
        return resultMap;
    }

    /**
     * 功能描述: 巡检任务统计
     * <p>
     *
     * @param spanDays
     * @return
     */
    public Map<String, Integer> newIndexInspectionTaskStatistic(int spanDays) {
        Date fromDay = DateUtils.truncate(DateUtils.addDays(new Date(), -1 * spanDays), Calendar.DAY_OF_MONTH);
        InspectionCountQueryModel queryParam = new InspectionCountQueryModel();
        queryParam.setCreateTimeStart(fromDay);
        InspectionCountResp resp = inspectionClient.getInspectionTaskStatistics(queryParam);
        TypeReference<Map<String, Integer>> typeRef = new TypeReference<Map<String, Integer>>() {
        };
        return JsonUtil.jacksonConvert(resp, typeRef);
    }

    /**
     * 功能描述: 故障自愈统计
     * <p>
     *
     * @param spanDays
     * @return
     */
    public Map<String, Integer> newIndexAutoRepairInstanceStatistic(int spanDays) {
        Date fromDay = DateUtils.truncate(DateUtils.addDays(new Date(), -1 * spanDays), Calendar.DAY_OF_MONTH);
        List<OpsReportGroupData> groupList = reportDao.getApInstanceGroupCountByStatus(fromDay);

        final MutableInt failCount = new MutableInt(0);
        final MutableInt totalCount = new MutableInt(0);
        for (OpsReportGroupData statusGroup : groupList) {
            OpsAutoRepairStatusEnum statusEnum = OpsAutoRepairStatusEnum.of(Integer.valueOf(statusGroup.getGroupCode
                    ()));
            if (statusEnum == OpsAutoRepairStatusEnum.STATUS_101
                    || statusEnum == OpsAutoRepairStatusEnum.STATUS_102
                    || statusEnum == OpsAutoRepairStatusEnum.STATUS_7) {
                failCount.add(statusGroup.getGroupCount());
            }
            totalCount.add(statusGroup.getGroupCount());
        }

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("fail", failCount.getValue());            // 失败数量
        resultMap.put("total", totalCount.getValue());            // 失败数量
        return resultMap;
    }


    /**
     * 功能描述: 所有类型任务按状态统计  (自动化作业、巡检、故障自愈)
     * 需展示状态： 失败，成功，告警，执行中 
	 *         失败:  包含执行失败、执行阻断、执行超时、人工终止
	 *         告警：     包含任务执行成功但执行状态异常、审核拒绝;
	 *         成功：     执行成功且不存在返回信息异常的
	 *         执行中： 包含 执行中、等待执行、等待用户、执行待审核、人工干预
     * <p>
     *
     * @return
     */
    @Transactional(isolation=Isolation.REPEATABLE_READ, readOnly=true)
    public Map<String, Integer> newIndexAllTypeTasksInAllStatistic(int spanDays) {
    	final MutableInt succCount = new MutableInt(0);
        final MutableInt failCount = new MutableInt(0);
        final MutableInt exceptionCount = new MutableInt(0);
        final MutableInt runningCount = new MutableInt(0);
        final MutableInt totalCount = new MutableInt(0);
        Date fromDay = DateUtils.truncate(DateUtils.addDays(new Date(), -1 * spanDays), Calendar.DAY_OF_MONTH);

        // 作业
        List<OpsReportGroupData> groupList = reportDao.getPipeInstGroupCountByStatus(fromDay);
        Integer exceptionPipeCount = reportDao.getPipeInstExceptionResultCount(fromDay);
        exceptionCount.add(exceptionPipeCount);
        
        for (OpsReportGroupData statusGroup : groupList) {
            OpsStatusEnum statusEnum = OpsStatusEnum.of(Integer.valueOf(statusGroup.getGroupCode()));
            if (OpsStatusEnum.STATUS_101 == statusEnum || OpsStatusEnum.STATUS_102 == statusEnum
                    || OpsStatusEnum.STATUS_7 == statusEnum) {
                failCount.add(statusGroup.getGroupCount());
            } 
            else if (OpsStatusEnum.STATUS_100 == statusEnum || OpsStatusEnum.STATUS_5 == statusEnum
                    	|| OpsStatusEnum.STATUS_6 == statusEnum || OpsStatusEnum.STATUS_8 == statusEnum) {
            	runningCount.add(statusGroup.getGroupCount());  	
            }
            else if (OpsStatusEnum.STATUS_10 == statusEnum) {
            	exceptionCount.add(statusGroup.getGroupCount());
            }
            else if (OpsStatusEnum.STATUS_9 == statusEnum) {
            	succCount.add(statusGroup.getGroupCount().intValue() - exceptionPipeCount.intValue());
            }
            totalCount.add(statusGroup.getGroupCount());
        }

        // 故障自愈
        groupList = reportDao.getApInstanceGroupCountByStatus(fromDay);
        Integer exceptionApCount = reportDao.getApInstanceExceptionResultCount(fromDay);
        exceptionCount.add(exceptionApCount);
        
        for (OpsReportGroupData statusGroup : groupList) {
            OpsAutoRepairStatusEnum statusEnum = OpsAutoRepairStatusEnum.of(Integer.valueOf(statusGroup.getGroupCode()));
            if (statusEnum == OpsAutoRepairStatusEnum.STATUS_101
                    || statusEnum == OpsAutoRepairStatusEnum.STATUS_102
                    || statusEnum == OpsAutoRepairStatusEnum.STATUS_7) {
                failCount.add(statusGroup.getGroupCount());
            } 
            else if (statusEnum == OpsAutoRepairStatusEnum.STATUS_100
                    || statusEnum == OpsAutoRepairStatusEnum.STATUS_6) {
            	runningCount.add(statusGroup.getGroupCount());
            } 
            else if (OpsAutoRepairStatusEnum.STATUS_9 == statusEnum) {
        		succCount.add(statusGroup.getGroupCount().intValue() - exceptionApCount.intValue());
            }
            totalCount.add(statusGroup.getGroupCount());
        }

        // 巡检
        InspectionCountQueryModel queryParam = new InspectionCountQueryModel();
        queryParam.setCreateTimeStart(fromDay);
        InspectionCountResp resp = inspectionClient.getInspectionTaskStatistics(queryParam);
        failCount.add(resp.getFailedNum());
        succCount.add(resp.getSuccessNum());
        runningCount.add(resp.getRunningNum());
        totalCount.add(resp.getTotalNum());

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("success", succCount.getValue());     	// 成功数量
        resultMap.put("fail", failCount.getValue());        	// 失败数量
        resultMap.put("running", runningCount.getValue());     	// 运行中数量
        resultMap.put("exception", exceptionCount.getValue());  // 异常数量
        resultMap.put("total", totalCount.getValue());      	// 所有数量
        return resultMap;
    }

    public List<IndexPageDayRunTrendItem> newIndexPipeInstStatisticByLine(int spanDays) {
        Date fromDay = DateUtils.truncate(DateUtils.addDays(new Date(), -1 * spanDays), Calendar.DAY_OF_MONTH);
        Map<String, Object> params = new HashMap<>();
        params.put("fromDay", fromDay);
        List<DayStausCountItem> dayStatusList = reportDao.queryStatusCountByDay(params);
        List<IndexPageDayRunTrendItem> result = constructEmptyTrendDayItemList(fromDay, spanDays);

        for (DayStausCountItem dayItem : dayStatusList) {
            IndexPageDayRunTrendItem trendItem = new IndexPageDayRunTrendItem(dayItem.getTheDay());
            int idx = result.indexOf(trendItem);
            if (idx < 0) {
                result.add(trendItem);
            } else {
                trendItem = result.get(idx);
            }

            OpsStatusEnum statusEnum = OpsStatusEnum.of(dayItem.getStatus());
            if (OpsStatusEnum.STATUS_101 == statusEnum || OpsStatusEnum.STATUS_102 == statusEnum
                    || OpsStatusEnum.STATUS_7 == statusEnum) {
                trendItem.getFailCount().add(dayItem.getStatusCount());
            } 
            else if (OpsStatusEnum.STATUS_9 == statusEnum) {
                trendItem.getSuccessCount().add(dayItem.getStatusCount());
            }
            trendItem.getTotalCount().add(dayItem.getStatusCount());
        }
        return result;
    }

    /**
     * 功能描述: 按任务类型统计数量分布
     * <p>
     *
     * @param spanDays
     * @return
     */
    public Map<String, Integer> newIndexAllTaskTypeStatisticByPie(int spanDays) {
        final MutableInt manualCount = new MutableInt(0);
        final MutableInt autoCount = new MutableInt(0);
        final MutableInt autoRepairCount = new MutableInt(0);
        final MutableInt inspectionCount = new MutableInt(0);
        Date fromDay = DateUtils.truncate(DateUtils.addDays(new Date(), -1 * spanDays), Calendar.DAY_OF_MONTH);

        // 手工作业、自动作业
        List<OpsReportGroupData> groupList = reportDao.getPipeInstGroupCountByTriggerWay(fromDay);
        for (OpsReportGroupData statusGroup : groupList) {
            OpsTriggerWayEnum triggerWay = OpsTriggerWayEnum.fromStatusCode(Integer.valueOf(statusGroup.getGroupCode
                    ()));
            if (OpsTriggerWayEnum.TRIGGER_BY_MANUAL == triggerWay) {
                manualCount.add(statusGroup.getGroupCount());
            } else {
                autoCount.add(statusGroup.getGroupCount());
            }
        }

        // 自愈
        Integer apInstCount = reportDao.getApInstanceTotalCount(fromDay);
        autoRepairCount.setValue(apInstCount);

        // 巡检
        InspectionCountQueryModel queryParam = new InspectionCountQueryModel();
        queryParam.setCreateTimeStart(fromDay);
        InspectionCountResp resp = inspectionClient.getInspectionTaskStatistics(queryParam);
        inspectionCount.setValue(resp.getTotalNum());

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("manual_pipe", manualCount.getValue());            // 手动作业
        resultMap.put("auto_pipe", autoCount.getValue());                // 自动作业
        resultMap.put("auto_repair", autoRepairCount.getValue());        // 自愈
        resultMap.put("inspection", inspectionCount.getValue());        // 巡检
        return resultMap;
    }

    /**
     * 功能描述: 作业耗时时长分布统计
     * <p>
     *
     * @param spanDays
     * @return
     */
    public Map<String, Integer> newIndexPipeInstCostTimeStatistic(int spanDays) {
        IndexPageRunTimeSpanStatistic result = indexRunTimeSpanStatistic(spanDays);
        TypeReference<Map<String, Integer>> typeRef = new TypeReference<Map<String, Integer>>() {};
        return JsonUtil.jacksonConvert(result, typeRef);
    }

    /**
     * 功能描述: 异常作业列表查询
     * <p>
     *
     * @param queryParam
     * @return
     */
    public PageListQueryResult<OpsPipelineInstanceDTO>
    		newIndexExceptionPipeInstPageList(@RequestBody IndexReportExceptionPipeInstQueryModel queryParam) {
        List<OpsPipelineInstanceDTO> resultList = reportDao.queryExceptionPipeInstList(queryParam);
        if (CollectionUtils.isEmpty(resultList)) {
            return new PageListQueryResult<OpsPipelineInstanceDTO>(0, resultList);
        }
        Integer totalSize = reportDao.queryExceptionPipeInstListTotalSize(queryParam);
        return new PageListQueryResult<OpsPipelineInstanceDTO>(totalSize, resultList);
    }


    public List<OpsPipelineScenes> normalScenesStatistic(Integer num) {
        // 取最常用的6个作业场景
        return reportDao.queryNormalScenes(num);
    }

    public TodayOpsTaskStatistic todayOpsTaskStatistic() {
        TodayOpsTaskStatistic todayOpsTaskStatistic = new TodayOpsTaskStatistic();
        Map<String, Object> param = Maps.newHashMap();
//		param.put("triggerWays", OpsTriggerWayEnum.TRIGGER_BY_MANUAL.getStatusCode());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        param.put("fromDay", calendar.getTime());
        List<StatisticRespModel> resp = reportDao.queryPipelineInstanceTriggerWayCount(param);
        todayOpsTaskStatistic.setAutoOpsTaskNum(0);
        todayOpsTaskStatistic.setHandleOpsTaskNum(0);
        for (StatisticRespModel statisticRespModel : resp) {
            if (statisticRespModel.getType().equals(OpsTriggerWayEnum.TRIGGER_BY_MANUAL.getStatusCode().toString())) {
                todayOpsTaskStatistic.setHandleOpsTaskNum(statisticRespModel.getCount());
            } else {
                todayOpsTaskStatistic.setAutoOpsTaskNum(todayOpsTaskStatistic.getAutoOpsTaskNum() +
                        statisticRespModel.getCount());
            }
        }
        // 自愈任务数统计
        OpsStatisticNumBase groupStatisitic = reportDao.getApInstanceGroupCountStatistic(param);
        todayOpsTaskStatistic.setAutoRepairTaskNum(groupStatisitic.getTotalNum());


        Integer opsTaskFailedNum = 0;
        Integer auditNum = 0;
        OpsStatisticNumBase historyStatisitic = reportDao.getOpsHistoryNumStatistic(param);
        // 审核数
        auditNum = historyStatisitic.getAuditNum();
        todayOpsTaskStatistic.setAuditTaskNum(auditNum);

        // 自动化任务失败数
        opsTaskFailedNum = historyStatisitic.getFailNum();

        // 自愈任务处理失败数
//        OpsStatisticNumBase groupStatisitic = reportDao.getApInstanceGroupCountStatistic(param);
        opsTaskFailedNum += groupStatisitic.getFailNum();

        // 巡检任务失败数
        InspectionCountQueryModel queryParam = new InspectionCountQueryModel();
        queryParam.setCreateTimeStart(calendar.getTime());
        InspectionCountResp respInspStatistic = inspectionClient.getInspectionTaskStatistics(queryParam);
        opsTaskFailedNum += respInspStatistic.getFailedNum();
        todayOpsTaskStatistic.setFailedTaskNum(opsTaskFailedNum);
        // 巡检任务数
        todayOpsTaskStatistic.setInspectionTaskNum(respInspStatistic.getTotalNum());


        return todayOpsTaskStatistic;
    }

    public TodayOpsTaskStatusStatistic todayOpsTaskStatusStatistic() {

        Map<String, Object> param = Maps.newHashMap();
//		param.put("triggerWays", OpsTriggerWayEnum.TRIGGER_BY_MANUAL.getStatusCode());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        param.put("fromDay", calendar.getTime());

        // 自动化任务数统计
        OpsStatisticNumBase historyNum = reportDao.getOpsHistoryNumStatistic(param);

        // 自愈任务数统计
        OpsStatisticNumBase groupList = reportDao.getApInstanceGroupCountStatistic(param);

        // 巡检任务
        InspectionCountQueryModel queryParam = new InspectionCountQueryModel();
        queryParam.setCreateTimeStart(calendar.getTime());
        InspectionCountResp respInspStatistic = inspectionClient.getInspectionTaskStatistics(queryParam);

        TodayOpsTaskStatusStatistic todayOpsTaskStatusStatistic = new TodayOpsTaskStatusStatistic();
        Integer totalSuccessSum = historyNum.getSuccessNum() + groupList.getSuccessNum() + respInspStatistic
                .getSuccessNum();
        Integer totalNum = historyNum.getTotalNum() + groupList.getTotalNum() +
                respInspStatistic.getTotalNum();

        // 设置当日总成功率
        if (totalNum == 0){
            todayOpsTaskStatusStatistic.setTaskSuccessRate(0.0);
        }else {
            double successRate =  ((double) totalSuccessSum / totalNum);
            todayOpsTaskStatusStatistic.setTaskSuccessRate(successRate);
        }


        // 设置设备总数  --只需要查询所有自动化执行设备
        todayOpsTaskStatusStatistic.setDeviceNum(reportDao.getOpsHistoryDeviceNum(param));

        //自动化耗时统计
        OpsTimeConsumeStatisticBase history = reportDao.getOpsHistoryTimeStatistic(param);

        //自愈耗时统计
        OpsTimeConsumeStatisticBase autoRepair = reportDao.getApInstanceTimeStatistic(param);

        //巡检耗时统计
        param.put("fromDay",  DateUtil.format(calendar.getTime(), DateUtil.DATE_TIME_CH_FORMAT));
        OpsTimeConsumeStatisticBase inspction = inspectionClient.getInspectionTimeStatistic(param);

        if (history == null)
            history = new OpsTimeConsumeStatisticBase();
        if(autoRepair == null) {
            autoRepair = new OpsTimeConsumeStatisticBase();
        }
        if (inspction == null) {
            inspction = new OpsTimeConsumeStatisticBase();
        }
        Integer averageCount = 0;
        averageCount += ((history.getAverageTime() == 0.0 )? 0 : 1);
        averageCount += ((autoRepair.getAverageTime() == 0.0)? 0 : 1);
        averageCount += ((inspction.getAverageTime() == 0.0 )? 0 : 1);
        // 设置总平均耗时
        if (averageCount != 0) {
            todayOpsTaskStatusStatistic.setTaskAverageTime((history.getAverageTime() + autoRepair.getAverageTime() +
                    inspction.getAverageTime()) / averageCount);
        } else {
            todayOpsTaskStatusStatistic.setTaskAverageTime(0.0);
        }

        // 设置总最大耗时
        todayOpsTaskStatusStatistic.setTaskMaxTime(Double.max(Double.max(history.getMaxTime(), autoRepair
                .getMaxTime()), inspction.getMaxTime()));

        return todayOpsTaskStatusStatistic;
    }

    public OpsSuccessRateStatistic taskExecSuccessedRate(Integer spanDays) {
        OpsSuccessRateStatistic opsSuccessRateStatistic = new OpsSuccessRateStatistic();
        Date thirtyDaysAgo = DateUtils.truncate(DateUtils.addDays(new Date(), -1 * spanDays), Calendar.DAY_OF_MONTH);

        Map<String, Object> param = Maps.newHashMap();
        param.put("fromDay", thirtyDaysAgo);

        //自愈数量统计
        OpsStatisticNumBase autoRepair = reportDao.getApInstanceGroupCountStatistic(param);
        if (autoRepair ==null || autoRepair.getTotalNum() == 0) {
            opsSuccessRateStatistic.setSelfRepairSuccessRate(0.0);
        } else {
            double successRate = (double) autoRepair.getSuccessNum() / autoRepair.getTotalNum();
            opsSuccessRateStatistic.setSelfRepairSuccessRate(successRate);

        }
        // 自动任务数量统计
        param.put("types", OpsTriggerWayEnum.TRIGGER_BY_JOB.getStatusCode() + "," + OpsTriggerWayEnum.TRIGGER_BY_API
                .getStatusCode());
        OpsStatisticNumBase autoHistory = reportDao.getOpsHistoryNumStatistic(param);
        if (autoHistory ==null || autoHistory.getTotalNum() == 0) {
            opsSuccessRateStatistic.setSelfRepairSuccessRate(0.0);
        } else {
            double autoRate = (double) autoHistory.getSuccessNum() / autoHistory.getTotalNum();
            opsSuccessRateStatistic.setAutoSuccessRate(autoRate);
        }
        // 手动任务数量统计
        param.put("types", OpsTriggerWayEnum.TRIGGER_BY_MANUAL.getStatusCode().toString());
        OpsStatisticNumBase handleHistory = reportDao.getOpsHistoryNumStatistic(param);
        if (handleHistory ==null || handleHistory.getTotalNum() == 0) {
            opsSuccessRateStatistic.setSelfRepairSuccessRate(0.0);
        } else {
            double handleRate = (double) handleHistory.getSuccessNum() / handleHistory.getTotalNum();
            opsSuccessRateStatistic.setHandleSuccessRate(handleRate);
        }
        //巡检任务数量统计
        InspectionCountQueryModel queryParam = new InspectionCountQueryModel();
        queryParam.setCreateTimeStart(thirtyDaysAgo);
        InspectionCountResp inspection = inspectionClient.getInspectionTaskStatistics(queryParam);
        if (inspection ==null || inspection.getTotalNum() == 0) {
            opsSuccessRateStatistic.setSelfRepairSuccessRate(0.0);
        } else {
            double inspectionRate = (double) inspection.getSuccessNum() / inspection.getTotalNum();
            opsSuccessRateStatistic.setInspectionSuccessRate(inspectionRate);
        }
        return opsSuccessRateStatistic;
    }

    public PageListQueryResult<OpsToBeProcessedTaskStatistic> queryToBeProcessedTaskList
            (OpsToBeProcessedTaskQueryModel queryModel) {
        PageListQueryResult<OpsToBeProcessedTaskStatistic> result = new PageListQueryResult<>();
        Integer count = reportDao.queryToBeProcessedTaskListCount(queryModel);
        List<OpsToBeProcessedTaskStatistic> listTask = Lists.newArrayList();
        if (count > 0) {
            listTask = reportDao.queryToBeProcessedTaskList(queryModel);
        }
        result.setTotalCount(count);
        result.setDataList(listTask);
        return result;
    }
}
