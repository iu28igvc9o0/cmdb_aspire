package com.aspire.mirror.alert.server.controller.monthReport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.aspire.mirror.alert.api.dto.monthReport.AlertMonthReportAllDTO;
import com.aspire.mirror.alert.api.dto.monthReport.AlertMonthReportIdcTypeDTO;
import com.aspire.mirror.alert.api.dto.monthReport.AlertsLevelCountsDTO;
import com.aspire.mirror.alert.api.service.monthReport.AlertExternalInterfaceService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import com.aspire.mirror.alert.server.dao.monthReport.AlertMonthReportSyncDao;
import com.aspire.mirror.alert.server.dao.alert.AlertsIndexPageDao;
import com.aspire.mirror.alert.server.dao.monthReport.po.AlertMonthReportIdcType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * @author baiwp
 * @title: AlertDeriveController
 * @projectName mirror-alert
 * @description: TODO
 * @date 2019/8/1614:14
 */
@RestController
@Slf4j
public class AlertExternalInterfaceController implements AlertExternalInterfaceService {
	@Autowired
	private AlertMonthReportSyncDao alertMonthReportSyncDao;
	@Autowired
	private AlertsIndexPageDao alertsIndexPageDao;
	

	@Override
	public List<AlertMonthReportIdcTypeDTO> getDepartmentRatio(String deviceType) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		Date startTime = calendar.getTime();
		
		String month = sdf.format(startTime);
		List<AlertMonthReportIdcType> list = alertMonthReportSyncDao.queryDepartmentBymonth(month, deviceType);
		List<AlertMonthReportIdcTypeDTO> valList = Lists.newArrayList();
		for(AlertMonthReportIdcType val:list) {
			AlertMonthReportIdcTypeDTO temp = new AlertMonthReportIdcTypeDTO();
			BeanUtils.copyProperties(val, temp);
			valList.add(temp);
		}
		return valList;
	}
	
	
	

	@Override
	public List<AlertsLevelCountsDTO> getAlertCount() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		Date startTime = calendar.getTime();

		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DATE, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		Date endTime = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		List<Map<String,Object>> list = alertsIndexPageDao.getLevelCountsBySystem(sdf.format(startTime), sdf.format(endTime));
		Map<String,AlertsLevelCountsDTO> mapRs = Maps.newHashMap();
		for(Map<String,Object> map:list) {
			if(null!= map.get("idc_type") && null!= map.get("biz_sys")) {
				AlertsLevelCountsDTO val = new AlertsLevelCountsDTO();
				
				String idcType = map.get("idc_type").toString();
				String biz = map.get("biz_sys").toString();
				String key = idcType+"_"+biz;
				if(mapRs.containsKey(key)) {
					val = mapRs.get(key);
				}else {
					val.setBizSystem(biz);
					val.setIdcType(idcType);
				}
				mapRs.put(key, val);
				int count = Integer.parseInt(map.get("count").toString());
				int level = Integer.parseInt(map.get("alert_level").toString());
				if(level ==5) {
					val.setSerious(count);
				}
				if(level ==4) {
					val.setHigh(count);
				}
				if(level ==3) {
					val.setMedium(count);
				}
				if(level ==2) {
					val.setLow(count);
				}
				val.setSum();
			}
			
		}
		return new ArrayList(mapRs.values());
	}
	
	public List<AlertMonthReportIdcType> getDepartmentRatioNew(String deviceType) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date startTime = calendar.getTime();
		
		String month = sdf.format(startTime);
		List<AlertMonthReportIdcType> list = alertMonthReportSyncDao.queryDepartmentBymonth(month, deviceType);
		/*
		 * for(AlertMonthReportIdcType val:list) { AlertMonthReportIdcTypeDTO temp = new
		 * AlertMonthReportIdcTypeDTO(); BeanUtils.copyProperties(val, temp);
		 * valList.add(temp); }
		 */
		return list;
	}
	
	@Override
	public List<AlertMonthReportAllDTO> getReportMonthAllDdata() {
		List<AlertMonthReportIdcType> vmList = getDepartmentRatioNew("云主机");
		List<AlertMonthReportIdcType> phyList = getDepartmentRatioNew("X86服务器");
		Map<String,AlertMonthReportAllDTO> map = Maps.newHashMap();
		for(AlertMonthReportIdcType vm:vmList) {
			
			String idcType = vm.getIdcType();
			String biz = vm.getBizSystem();
			if(StringUtils.isNotBlank(idcType) && StringUtils.isNotBlank(biz)) {
				AlertMonthReportAllDTO val = new AlertMonthReportAllDTO();
				String key = idcType+"_"+biz;
				if(map.containsKey(key)) {
					val = map.get(key);
				}else {
					val.setIdcType(idcType);
					val.setBizSystem(biz);
					val.setMonth(vm.getMonth());
				}
				map.put(key, val);
				 val.setVm_cpu_max(vm.getCpu_max());
				 val.setVm_cpu_avg(vm.getCpu_avg());
				 val.setVm_memory_avg(vm.getMemory_avg());
				 val.setVm_memory_max(vm.getMemory_max());
				 val.setVm_cpu_eighty_ratio(vm.getCpu_eighty_ratio());
				 val.setVm_cpu_fourty_ratio(vm.getCpu_fourty_ratio());
				 val.setVm_cpu_fifteen_ratio(vm.getCpu_fifteen_ratio());
				 val.setVm_cpu_eighty_more_ratio(vm.getCpu_eighty_more_ratio());
				 val.setVm_memory_eighty_ratio(vm.getMemory_eighty_ratio());
				 val.setVm_memory_fourty_ratio(vm.getMemory_fourty_ratio());
				 val.setVm_memory_fifteen_ratio(vm.getMemory_fifteen_ratio());
				 val.setVm_memory_eighty_more_ratio(vm.getMemory_eighty_more_ratio());
			}
		}
		
		
		for(AlertMonthReportIdcType vm:phyList) {
			
			String idcType = vm.getIdcType();
			String biz = vm.getBizSystem();
			if(StringUtils.isNotBlank(idcType) && StringUtils.isNotBlank(biz)) {
				AlertMonthReportAllDTO val = new AlertMonthReportAllDTO();
				String key = idcType+"_"+biz;
				if(map.containsKey(key)) {
					val = map.get(key);
				}else {
					val.setIdcType(idcType);
					val.setBizSystem(biz);
					val.setMonth(vm.getMonth());
				}
				map.put(key, val);
				 val.setPhy_cpu_max(vm.getCpu_max());
				 val.setPhy_cpu_avg(vm.getCpu_avg());
				 val.setPhy_memory_avg(vm.getMemory_avg());
				 val.setPhy_memory_max(vm.getMemory_max());
				 val.setPhy_cpu_eighty_ratio(vm.getCpu_eighty_ratio());
				 val.setPhy_cpu_fourty_ratio(vm.getCpu_fourty_ratio());
				 val.setPhy_cpu_fifteen_ratio(vm.getCpu_fifteen_ratio());
				 val.setPhy_cpu_eighty_more_ratio(vm.getCpu_eighty_more_ratio());
				 val.setPhy_memory_eighty_ratio(vm.getMemory_eighty_ratio());
				 val.setPhy_memory_fourty_ratio(vm.getMemory_fourty_ratio());
				 val.setPhy_memory_fifteen_ratio(vm.getMemory_fifteen_ratio());
				 val.setPhy_memory_eighty_more_ratio(vm.getMemory_eighty_more_ratio());
			}
		}
		List<AlertsLevelCountsDTO> alertList = getAlertCount();
		for(AlertsLevelCountsDTO alert:alertList) {
			
			String idcType = alert.getIdcType();
			String biz = alert.getBizSystem();
				AlertMonthReportAllDTO val = new AlertMonthReportAllDTO();
				String key = idcType+"_"+biz;
				if(map.containsKey(key)) {
					val = map.get(key);
				}else {
					val.setIdcType(idcType);
					val.setBizSystem(biz);
					//val.setMonth(vm.getMonth());
				}
				map.put(key, val);
				val.setHigh(alert.getHigh());
				val.setLow(alert.getLow());
				val.setMedium(alert.getMedium());
				val.setSerious(val.getSerious());
				val.setSum(alert.getSum());
		}
		
		return  new ArrayList(map.values());
	}

    
}
