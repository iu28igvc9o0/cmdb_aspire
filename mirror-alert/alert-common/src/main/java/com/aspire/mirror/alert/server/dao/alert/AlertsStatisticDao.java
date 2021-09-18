package com.aspire.mirror.alert.server.dao.alert;

import com.aspire.mirror.alert.server.dao.alert.po.*;
import com.aspire.mirror.alert.server.vo.alert.AlertTargetCountVo;
import com.aspire.mirror.common.entity.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AlertsStatisticDao {
    Map<String, Object> selectHisSummary(@Param(value = "param") Map<String, Object> param,
                                         @Param(value = "resFilterMap") Map<String, List<String>> resFilterMap);
    Map<String, Object> selectHisSummaryOrder(@Param(value = "param") Map<String, Object> param,
                                         @Param(value = "resFilterMap") Map<String, List<String>> resFilterMap);
    List<AlertsStatisticTrend> selectTrendBySection(Map<String, Object> params);
    List<AlertsStatisticClassify> selectAlertsClassify(Map<String, Object> params);
    List<AlertsStatisticSourceClassify> selectAlertsSourceClassify(Map<String, Object> params);
    int pageListCount(Page page);
    List<Alerts> pageList(Page page);
    Map<String, Object> selectOverview(Map<String, Object> params);
    int pageHisListCount(Page page);
    List<AlertsHis> pageHisList(Page page);
    List<AlertsExport> exportList(Page page);
    List<Map<String, Object>> exportHisList(Page page);
    int filterPageListCount(Page page);
    List<Alerts> filterPageList(Page page);
    List<Map<String, Object>> filterMapList(Page page);
    List<AlertsStatisticOverview> getAlertSummaryByDeviceClass(List<String> codeList);
    List<AlertsStatisticOverview> getAlertHisSummaryByDeviceClass();
    List<AlertTargetCountVo> getAlertCountByAlertTarget(@Param("alertLevel") String alertLevel);
    List<AlertTargetCountVo> getAlertCountByMoniTarget(@Param("alertLevel") String alertLevel);
    List<AlertsStatisticOverview> getSummaryByAuthContent(Map<String, Object> authContent);
    List<String> getSummaryActivityByAuthContent(@Param("authContent") Map<String, Object> authContent,
                                                 @Param("resFilterMap") Map<String, List<String>> resFilterMap);
    List<String> getSummaryConfirmByAuthContent(@Param("authContent") Map<String, Object> authContent,
                                                @Param("resFilterMap") Map<String, List<String>> resFilterMap);
    int selectAlert(Page page);
    Map<String, Object> getAlertCountByIpAndIdc(@Param("ip") String ip,
                                                      @Param("idcType") String idcType);
}
