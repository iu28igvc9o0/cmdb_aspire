package com.aspire.mirror.alert.server.biz.alert;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.aspire.mirror.alert.server.dao.alert.po.Alerts;
import com.aspire.mirror.alert.server.vo.alert.AlertsTop10Vo;
import com.aspire.mirror.alert.server.vo.alert.AlertStatisticSummaryVo;
import com.aspire.mirror.alert.server.vo.alert.AlertsStatisticClassifyVo;
import com.aspire.mirror.alert.server.vo.alert.AlertsVo;
import com.aspire.mirror.common.entity.PageResponse;

public interface AlertsIndexPageBiz {

    AlertStatisticSummaryVo getSummaryByOperateType(Integer code, String idcType, String startTime, String endTime);
	AlertStatisticSummaryVo getHisSummaryByOperateType(String idcType,String startTime,String endTime);
	AlertStatisticSummaryVo getSummaryByDateRange(Date startDate, Date endDate, String idcType);
    Map<String, Object> getTrendSeries(String interval, String idcType, String deviceType, String alertLevel, String source);
    List<AlertsStatisticClassifyVo> getClassifySeries(Date startDate, Date endDate, String idcType, Map<String, List<String>> resFilterMap);
	List<AlertsTop10Vo> getAlertTop10(String idcType, String deviceType,
									  String alertLevel, String colName);
	PageResponse<AlertsTop10Vo> getAlertMoniIndexTop10(String startDate, String endDate, String idcType,
													   String deviceType, String alertLevel);
	List<AlertsVo> getLatest(Integer limit, String idcType, Integer operateStatus,
							 String startDate, String endDate);
	List<Alerts> getLatestOrder(Integer limit, String idcType, Integer operateStatus,
								String startDate, String endDate);
	List<Map<String, Object>> selectIdcTypeAlertsByOperateStatus(Integer operateStatus,
			String startDate, String endDate);
	List<Map<String, Object>> selectDeviceTypeAlertsByOperateStatus(Integer operateStatus, String startDate,
			String endDate);
	List<Map<String, Object>> selectIdcTypeAlertsByOperateStatusOrder(Integer operateStatus,
																 String startDate, String endDate);
	List<Map<String, Object>> selectDeviceTypeAlertsByOperateStatusOrder(Integer operateStatus, String startDate,
																	String endDate);
	List<Map<String,Object>> alertIdcDoHours (String alertLevel);
}
