package com.aspire.mirror.alert.server.biz.alert;

import com.aspire.mirror.alert.server.dao.alert.po.AlertsExport;
import com.aspire.mirror.alert.server.vo.alert.*;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AlertsStatisticBiz {

    AlertStatisticSummaryVo getSummaryByOperateType(Map<String, Object> param, Map<String, List<String>> resFilterMap);
    AlertStatisticSummaryVo getSummaryByOperateTypeOrder(Map<String, Object> param, Map<String, List<String>> resFilterMap);
    AlertStatisticSummaryVo getHisSummaryByOperateType(Map<String, Object> param, Map<String, List<String>> resFilterMap);
    AlertStatisticSummaryVo getHisSummaryByOperateTypeOrder(Map<String, Object> param, Map<String, List<String>> resFilterMap);
    AlertStatisticSummaryVo getSummaryByDateRange(Date startDate, Date endDate);
    AlertStatisticSummaryVo getOverview(String codes);
    List<String> getAlertsMonitObjectList();
    List<String> getHisAlertsMonitObjectList();
    Map<String, Object> getTrendSeries(String interval);
    List<AlertsStatisticClassifyVo> getClassifySeries(Date startDate, Date endDate);
    Map<String, List<AlertsStatisticSourceClassifyVo>> getSourceClassifySeries(Date startDate, Date endDate);
    List<AlertsVo> getLatest(Integer limit, Map<String, List<String>> param);
    PageResponse<AlertsVo> select(PageRequest pageRequest);
    PageResponse<AlertsHisVo> selectHis(PageRequest pageRequest);
    List<AlertsExport> export(PageRequest pageRequest);
    List<Map<String, Object>> exportHis(PageRequest pageRequest);
    List<AlertMainSummaryVo> getAlertSummaryByDeviceClass(String code);
    List<AlertTargetCountVo> getAlertTargetCount(String alertLevel, String type);
    AlertStatisticSummaryVo getSummaryByAuthContent(Map<String, Object> authContent);
    List<String> getSummaryByAuthContentTest(Map<String, Object> authContent, Map<String, List<String>> resFilterMap);
    int selectAlert(PageRequest pageRequest);
    List<Map<String, Object>> getAlertCountByIpAndIdc(List<Map<String,Object>> request);

}
