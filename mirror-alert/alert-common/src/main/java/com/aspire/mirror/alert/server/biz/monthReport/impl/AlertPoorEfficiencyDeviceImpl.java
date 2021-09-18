package com.aspire.mirror.alert.server.biz.monthReport.impl;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.mirror.alert.server.biz.monthReport.AlertPoorEfficiencyDeviceBiz;
import com.aspire.mirror.alert.server.clientservice.MonthDayReportNewServiceClient;
import com.aspire.mirror.alert.server.dao.monthReport.AlertsPoorEfficiencyDeviceDao;
import com.aspire.mirror.alert.server.util.DateUtils;
import com.aspire.mirror.elasticsearch.api.dto.MonthReportRequest;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AlertPoorEfficiencyDeviceImpl implements AlertPoorEfficiencyDeviceBiz {
	@Autowired
	private AlertsPoorEfficiencyDeviceDao alertsPoorEfficiencyDeviceDao;

	@Autowired
	private MonthDayReportNewServiceClient MonthDayReportNewServiceClient;
	
	private DecimalFormat   df   =new   java.text.DecimalFormat("#.00");  

	@Override
	public void getPoorEfficiencyDeviceMonthData(String item ,int count) throws Exception {
		try {
			Map<String,Object> param  = Maps.newHashMap();
			//param.put("date", value);
			param.put("item", item);
			param.put("type", 2);
			Map<String,Object> map = alertsPoorEfficiencyDeviceDao.getDeviceDataLatestDate(param);//数据库最近的时间
			Date lastMonth = DateUtils.getTimesLastMonthmorning(null);//上月日期
			if(null!=map) {
				Date hisDate = DateUtils.stringToDate(DateUtils.DEFAULT_DATETIME_FMT, map.get("date").toString());
				Date curDate = DateUtils.getTimesNextMonthmorning(hisDate);//数据库最近时间的 下月时间
				while(curDate.getTime()<=lastMonth.getTime()) {//循环查询数据
					queryMonthData(curDate,item);
					curDate = DateUtils.getTimesNextMonthmorning(curDate);
				}
				
			}else {
				queryMonthData(lastMonth,item);//第一次跑任务
			}
		}catch(Exception e) {
			if(count>0) {
				throw e;
			}else {
				count++;
			}
			log.error("低设备性能按月跑数据第一次报错",e);
			getPoorEfficiencyDeviceMonthData(item,0);
		}
		
	}
	//查询月数据
	@Override
	public void queryMonthData(Date curDate,String item) throws Exception {
		Map<String,Object> logInfo = getLogInfo(item,curDate,2);
		try {
			MonthReportRequest request = new MonthReportRequest();
			String startTime = DateUtils.datetimeToString(DateUtils.DEFAULT_DATETIME_FMT, curDate);
			request.setStartTime(startTime);
			logInfo.put("date", startTime);
			request.setItem(item);
			request.setType(2);
			List<Map<String, Object>> dataList = MonthDayReportNewServiceClient.getPoorEfficiencyDeviceMonthData(request);
			if(null!=dataList && dataList.size()>0) {
				alertsPoorEfficiencyDeviceDao.insertPoorEfficiencyDeviceData(dataList);
				dataList.clear();
				dataList = null;
			}
			
		}catch(Exception e) {
			logInfo.put("status", "error");
			StringBuffer sb = new StringBuffer();
			String msg = sb.append("月任务：").append(item).append(curDate).append(e.getClass().getName())
					.append(":").append(e.getMessage()).toString();
			logInfo.put("content",msg);
			throw e;
			
		}finally {
			logInfo.put("create_time", new Date());
			alertsPoorEfficiencyDeviceDao.insertDeviceLogData(logInfo);//记录日志
		}
		
	}
	
	

	private Map<String, Object> getLogInfo(String item,Date curDate,int type) {
		Map<String,Object> logInfo = Maps.newHashMap();
		logInfo.put("type", type);
		logInfo.put("item", item);
		logInfo.put("exec_time", new Date());
		logInfo.put("status", "success");
		return logInfo;
	}

	@Override
	public void getPoorEfficiencyDeviceWeekData(String item,int count) throws Exception {
		try {
		Map<String,Object> param  = Maps.newHashMap();
		//param.put("date", value);
		param.put("item", item);
		param.put("type", 2);
		
		//判断上个月的日志是否存在
		Date lastMonth = DateUtils.getTimesLastMonthmorning(null);//上月日期
		String lastMonthStr = DateUtils.datetimeToString(DateUtils.DEFAULT_DATETIME_FMT, lastMonth);
		param.put("date", lastMonthStr);
		//List<Map<String,Object>> monthList = alertsPoorEfficiencyDeviceDao.getDeviceDataByDate(param);
		Map<String,Object> lastLog = alertsPoorEfficiencyDeviceDao.getDeviceDataLatestDate(param);
		if(null==lastLog) {
			this.getPoorEfficiencyDeviceMonthData(item,0);
		
		}
		param.put("type", 1);
		param.remove("date");
		Map<String,Object> latestLog = alertsPoorEfficiencyDeviceDao.getDeviceDataLatestDate(param);//数据库最近的时间
		Date lastWeek = DateUtils.getLaskWeekMonday(null);//上周一
		if(null!=latestLog) {
			Date hisDate = DateUtils.stringToDate(DateUtils.DEFAULT_DATETIME_FMT, latestLog.get("date").toString());
			Date curDate = DateUtils.getNextWeekMonday(hisDate);//数据库最近时间的 下周时间
			while(curDate.getTime()<=lastWeek.getTime()) {//循环查询数据
				queryWeekData(curDate,item);
				curDate = DateUtils.getNextWeekMonday(curDate);
			}
			
		}else {
			queryWeekData(lastWeek,item);
		}
		}catch(Exception e) {
			log.error("低设备性能按周跑数据第一次报错",e);
			if(count>0) {
				throw e;
			}else {
				count++;
			}
			getPoorEfficiencyDeviceWeekData(item,count);
			
		}
		
	}
	//查询周数据
	@Override
	public void queryWeekData(Date curDate,String item) throws Exception {
		Map<String,Object> logInfo = getLogInfo(item,curDate,1);
		try {
			MonthReportRequest request = new MonthReportRequest();
			String startTime = DateUtils.datetimeToString(DateUtils.DEFAULT_DATETIME_FMT, curDate);
			request.setStartTime(startTime);
			logInfo.put("date", startTime);
			long dateLong = 6*24*60*60*1000l;//获取周日日期0点
			String endTime =  DateUtils.datetimeToString(DateUtils.DEFAULT_DATETIME_FMT, new Date(curDate.getTime()+dateLong));
			request.setEndTime(endTime);
			request.setItem(item);
			request.setType(2);
			List<Map<String, Object>> dataList = MonthDayReportNewServiceClient.getPoorEfficiencyDeviceWeekData(request);
			if(null!=dataList && dataList.size()>0) {
				getCompareData( curDate,item,dataList);//获取与上月差值
				alertsPoorEfficiencyDeviceDao.insertPoorEfficiencyDeviceData(dataList);
				dataList.clear();
				dataList = null;
			}
			
		}catch(Exception e) {
			logInfo.put("status", "error");
			StringBuffer sb = new StringBuffer();
			String msg = sb.append("周任务：").append(item).append(curDate).append(e.getClass().getName())
					.append(":").append(e.getMessage()).toString();
			logInfo.put("content",msg);
			throw e;
			
		}finally {
			logInfo.put("create_time", new Date());
			alertsPoorEfficiencyDeviceDao.insertDeviceLogData(logInfo);//记录日志
		}
		
	}
	private void getCompareData(Date curDate, String item, List<Map<String, Object>> dataList) {
		Date lastMonth = DateUtils.getTimesLastMonthmorning(curDate);//上月日期
		String lastMonthStr = DateUtils.datetimeToString(DateUtils.DEFAULT_DATETIME_FMT, lastMonth);
		Map<String,Object> param = Maps.newHashMap();
		param.put("item", item);
		param.put("type", 2);
		param.put("date", lastMonthStr);
		List<Map<String,Object>> monthList = alertsPoorEfficiencyDeviceDao.getDeviceDataByDate(param);
		Map<String,Map<String,Object>> monthMap = Maps.newHashMap();
		for(Map<String,Object> m:monthList) {
			monthMap.put(m.get("resourceId").toString(), m);
		}
		
		for(Map<String, Object> d:dataList) {
			if(null!=d.get("avg_value_week")) {
				String resourceId = d.get("resourceId").toString();
				float avgValue = Float.parseFloat(d.get("avg_value_week").toString());
				float avgMonthValue = 0;
				if(monthMap.containsKey(resourceId)) {
					Map<String,Object> m = monthMap.get(resourceId);
					avgMonthValue =  m.get("avg_value_month")==null?0:Float.parseFloat(m.get("avg_value_month").toString());
				}
				d.put("avg_value_month", avgMonthValue);
				d.put("diff_value", df.format(avgValue-avgMonthValue));
			}
		}
		//释放内存
		monthMap.clear();
		monthMap = null;
		monthList.clear();
		monthList = null;
		
	}
}
