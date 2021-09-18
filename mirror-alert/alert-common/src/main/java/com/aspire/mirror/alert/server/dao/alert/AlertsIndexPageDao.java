package com.aspire.mirror.alert.server.dao.alert;

import com.aspire.mirror.alert.server.dao.alert.po.Alerts;
import com.aspire.mirror.alert.server.dao.alert.po.AlertsStatisticOverview;
import com.aspire.mirror.alert.server.dao.alert.po.AlertsStatisticClassify;
import com.aspire.mirror.alert.server.dao.alert.po.AlertsStatisticTrend;
import com.aspire.mirror.alert.server.vo.alert.AlertsTop10Vo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface AlertsIndexPageDao {
    Map<String,Integer> selectHisSummary(@Param(value = "idcType") String idcType,
										 @Param(value = "startTime") String startTime,
										 @Param(value = "endTime") String endTime);

	List<AlertsStatisticTrend> selectTrendBySection(Map<String, Object> params);

	List<AlertsStatisticClassify> selectAlertsClassify(@Param(value = "params") Map<String, Object> params,
													   @Param(value = "resFilterMap") Map<String, List<String>> resFilterMap);

	List<Map<String,Object>> selectSummeryByOperateCode(@Param(value = "code") Integer code,
														@Param(value = "idcType") String idcType,
														@Param(value = "startTime") String startTime,
														@Param(value = "endTime") String endTime);

	List<AlertsStatisticOverview> selectByDateRange(@Param(value = "beginDate") Date beginDate,
													@Param(value = "endDate") Date endDate, @Param(value = "idcType") String idcType);

	List<AlertsTop10Vo> selectAlertsTop10(Map<String, Object> params);
	
	
	int selectAlertsMoniIndexCount(Map<String, Object> params);

	List<Alerts> selectLatest(@Param(value = "limit")Integer limit, @Param(value = "idcType")String idcType);
	
	int getAlertsCount(@Param(value = "idcType") String idcType);
	
	int getAlertsHisCount(@Param(value = "idcType") String idcType);
	
	List<Map<String,Object>> getLevelCounts(@Param(value = "idcType") String idcType);
	
	List<Map<String,Object>> getDeviceLevelCounts(@Param(value = "deviceList") List<String> deviceList);
	
	List<Map<String,Object>> getLevelCountsBySystem(@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate);
	List<Map<String,Object>> getDeviceTypeLevelCounts(@Param(value = "deviceList") List<String> deviceList);
	
	
	List<Map<String,Object>> selectIdcTypeAlertsByOperateStatus(@Param(value = "operateStatus") Integer operateStatus,
			@Param(value = "startTime") String startTime,
			@Param(value = "endTime") String endTime);
	
	List<Map<String,Object>> selectDeviceTypeAlertsByOperateStatus(@Param(value = "operateStatus") Integer operateStatus,
			@Param(value = "startTime") String startTime,
			@Param(value = "endTime") String endTime);

	List<Map<String,Object>> selectIdcTypeAlertsByOperateStatusOrder(@Param(value = "operateStatus") Integer operateStatus,
																@Param(value = "startTime") String startTime,
																@Param(value = "endTime") String endTime);

	List<Map<String,Object>> selectDeviceTypeAlertsByOperateStatusOrder(@Param(value = "operateStatus") Integer operateStatus,
																   @Param(value = "startTime") String startTime,
																   @Param(value = "endTime") String endTime);
	
	List<Alerts> selectLatestAlertsByOperateStatus(@Param(value = "limit")Integer limit, @Param(value = "idcType")String idcType,
			@Param(value = "operateStatus") Integer operateStatus,@Param(value = "startTime") String startTime,
			@Param(value = "endTime") String endTime);

	List<Alerts> selectLatestOrderByOperateStatus(@Param(value = "limit")Integer limit, @Param(value = "idcType")String idcType,
												   @Param(value = "operateStatus") Integer operateStatus,@Param(value = "startTime") String startTime,
												   @Param(value = "endTime") String endTime);

	List<Map<String,Object>> alertIdcDoHours (@Param(value = "alertLevel") String alertLevel);
}
