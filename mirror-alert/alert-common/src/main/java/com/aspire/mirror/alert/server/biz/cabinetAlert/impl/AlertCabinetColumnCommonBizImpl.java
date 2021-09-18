package com.aspire.mirror.alert.server.biz.cabinetAlert.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.mirror.alert.server.biz.cabinetAlert.AlertCabinetColumnCommonBiz;
import com.aspire.mirror.alert.server.dao.cabinetAlert.AlertCabinetColumnDao;
import com.aspire.mirror.alert.server.dao.cabinetAlert.po.AlertCabinetColumnConfig;
import com.aspire.mirror.alert.server.dao.cabinetAlert.po.AlertCabinetColumnConfigData;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class AlertCabinetColumnCommonBizImpl implements AlertCabinetColumnCommonBiz{
	Logger LOGGER = LoggerFactory.getLogger(AlertCabinetColumnCommonBizImpl.class);
	@Autowired
    private AlertCabinetColumnDao alertCabinetColumnDao;
	
	

	
	public List<AlertCabinetColumnConfigData> getConfigData(AlertCabinetColumnConfig config){
		int configType = config.getConfigType();
		Integer timeRange = config.getTimeRange();
		Integer alertPercentage = config.getAlertPercentage();
		Map<String,Object> params = Maps.newHashMap();
		if(configType==2) {//资源池
			params.put("idcType", config.getIdcType());
		}else if(configType==3){//机房
			params.put("idcType", config.getIdcType());
			params.put("roomId", config.getRoomId());
		}
		
		List<AlertCabinetColumnConfigData> data = alertCabinetColumnDao.selectNotInitialCabinetList(params);
		Date date = new Date();
		for(AlertCabinetColumnConfigData d:data) {
			d.setTimeRange(timeRange);
			d.setAlertPercentage(alertPercentage);
			d.setUpdateTime(date);
			d.setCreateTime(date);
			d.setStatus(1);
			d.setAlertStatus(1);
			//d.setConfigId(config.getId());
		}
		return data;
	}
	
	public void delConfigData(AlertCabinetColumnConfig config){
		int configType = config.getConfigType();
		Map<String,Object> params = Maps.newHashMap();
		if(configType==2) {//资源池
			params.put("idcType", config.getIdcType());
		}else if(configType==3){//机房
			params.put("idcType", config.getIdcType());
			params.put("roomId", config.getRoomId());
		}
		
		alertCabinetColumnDao.delConfigData(params);
		
	}
	

  
   
}
