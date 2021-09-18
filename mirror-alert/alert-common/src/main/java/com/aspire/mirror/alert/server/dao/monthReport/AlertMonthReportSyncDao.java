package com.aspire.mirror.alert.server.dao.monthReport;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.mirror.alert.server.dao.monthReport.po.AlertMonthReportIdcType;
import com.aspire.mirror.alert.server.dao.monthReport.po.alertNetMonitorConfig;


/**
 * 告警DAO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.server.dao
 * 类名称:    AlertsHisDao.java
 * 类描述:    告警DAO
 * 创建人:    longfeng
 * 创建时间:  2018/9/18 16:16
 * 版本:      v1.0
 */
@Mapper
public interface AlertMonthReportSyncDao {

	void insertIdcType(List<AlertMonthReportIdcType> map);

	
    
    
	void insertDepartment(List<Map<String,Object>> map);
    
    List<AlertMonthReportIdcType> queryDepartmentBymonth(@Param(value = "month") String month,
    		@Param(value = "deviceType")String deviceType);
    
    
    
    
    List<alertNetMonitorConfig> queryNetMoniter();
    
    
    void insertNetMonitorData(List<Map<String, Object>> map);
    
    
    
    void insertIdcTypeBizSystemDay(List<Map<String,Object>> map);
    
    void insertIdcTypeDay(List<Map<String,Object>> map);
    
    
    List<Map<String,Object>> queryDayBizSystem(@Param(value = "month") String month
    		,@Param(value = "idcType") String idcType
    		,@Param(value = "hisFlag")Integer hisFlag);
    
    List<Map<String,Object>> queryDayBizSystemMonth(@Param(value = "month") String month
    		,@Param(value = "idcType") String idcType
    		,@Param(value = "hisFlag")Integer hisFlag);
    
    List<String> queryDays(@Param(value = "month") String month,@Param(value = "idcType") String idcType
    		,@Param(value = "hisFlag")Integer hisFlag);
    
    List<Map<String,Object>> queryDaysConfig(@Param(value = "type") String type);
    
    void updateDaysConfig(Map<String,String> map);
    
    void insertDaysConfig(Map<String,String> map);
    
    List<String> querySuyanConfigLogsByTime(@Param(value = "startTime") String startTime
    		,@Param(value = "endTime") String endTime,@Param(value = "device_type") String device_type
    		,@Param(value = "status") String status);
    
    Map<String,Object> querySuyanConfigLogsByToTime(@Param(value = "to_time") String to_time
    		,@Param(value = "device_type") String device_type ,@Param(value = "status") String status);
    
    void insertSuyanConfigLogs(Map<String,Object> map);
    
    void insertIdcTypeIpDay(List<Map<String,Object>> map);
    
    List<AlertMonthReportIdcType> queryDepartmentBymonthDay(@Param(value = "month") String month,
    		@Param(value = "deviceType")String deviceType,@Param(value = "idcType") String idcType);
    
    
    List<AlertMonthReportIdcType> queryIdcTypeBymonthDay(@Param(value = "month") String month
    		,@Param(value = "deviceType")String deviceType,@Param(value = "idcType") String idcType);
    
    
    //暂时没用--开始
    List<Map<String,Object>> queryIdcTypeIpAllCount(@Param(value = "month") String month);
    
    
    List<Map<String,Object>> queryBizSystemIpAllCount(@Param(value = "month") String month);
    
    
    List<Map<String,Object>> queryIdcTypeIpCpuCount(@Param(value = "month") String month,
    		@Param(value = "startVal") Integer startVal,@Param(value = "endVal") Integer endVal);
    
    List<Map<String,Object>> queryBizSystemIpCpuCount(@Param(value = "month") String month,
    		@Param(value = "startVal") Integer startVal,@Param(value = "endVal") Integer endVal);
    
    List<Map<String,Object>> queryIdcTypeIpMemoryCount(@Param(value = "month") String month,
    		@Param(value = "startVal") Integer startVal,@Param(value = "endVal") Integer endVal);
    
    List<Map<String,Object>> queryBizSystemIpMemoryCount(@Param(value = "month") String month,
    		@Param(value = "startVal") Integer startVal,@Param(value = "endVal") Integer endVal);
    //暂时没用--结束
    
    //运营月报大屏接口
    
    Map<String,Object> queryMonthIdcTypeUseRate(@Param(value = "month") String month,
    		@Param(value = "idcType") String idcType,@Param(value = "deviceType")String deviceType);
    
    
    List<Map<String,Object>> queryMonthIdcTypeTrends(@Param(value = "month") String month,
    		@Param(value = "idcType") String idcType,@Param(value = "deviceType")String deviceType);
    
    //运营月报二级部门租户资源利用率
    void insertDepartment2(List<Map<String,Object>> map);
    
    List<Map<String,Object>> queryMonthUseRateForZH(@Param(value = "month") String month,
    		@Param(value = "authFilter") Map<String,String> authFilter,@Param(value = "deviceType")String deviceType);
    
    
    List<AlertMonthReportIdcType>   queryIdctypeDayNewByMonth(@Param(value = "day") String day,
    		@Param(value = "type") int type);
    
    
    List<AlertMonthReportIdcType> queryIdcTypeByDayMonthDay(@Param(value = "startDay") String startDay
    		,@Param(value = "endDay") String endDay,@Param(value = "deviceType")String deviceType
    		,@Param(value = "idcType") String idcType);
    
    List<AlertMonthReportIdcType> queryIdcTypeByDayMonthDayTopN(@Param(value = "startDay") String startDay
    		,@Param(value = "endDay") String endDay,@Param(value = "department1")String department1
    		,@Param(value = "sort") String sort);
    
    List<AlertMonthReportIdcType> queryIdcTypeByDayMonthDayAll(@Param(value = "startDay") String startDay
    		,@Param(value = "endDay") String endDay,@Param(value = "department1")String department1
    		,@Param(value = "idcType") String idcType);
    
    int querybizSysDayType2Count(Map<String, Object> request);
    
    List<Map<String, Object>> querybizSysDayType2CountTopN(Map<String, Object> request);
    
    List<Map<String, Object>> querybizSysDayType2CountAll(Map<String, Object> request);
}
