package com.aspire.mirror.alert.server.biz.inspectionDaily.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.aspire.mirror.alert.server.biz.inspectionDaily.AlertResourcePoolBiz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.aspire.mirror.alert.server.clientservice.CmdbClient;
import com.aspire.mirror.alert.server.clientservice.payload.ConfigDict;
import com.aspire.mirror.alert.server.constant.AlertCommonConstant;
import com.aspire.mirror.alert.server.dao.inspectionDaily.AlertResourcePoolDao;
import com.aspire.mirror.alert.server.dao.inspectionDaily.po.AlertDeviceTypeTop;
import com.aspire.mirror.alert.server.dao.inspectionDaily.po.AlertInspectionDaily;
import com.aspire.mirror.alert.server.dao.inspectionDaily.po.AlertMoniterObjectTop;
import com.aspire.mirror.alert.server.dao.inspectionDaily.po.AlertResourcePool;
import com.aspire.mirror.alert.server.dao.inspectionDaily.po.AlertResourcePoolNew;
import com.aspire.mirror.alert.server.vo.inspectionDaily.AlertRourcePoolVo;
import com.aspire.mirror.alert.server.vo.inspectionDaily.AlertTotalVo;
import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.PageUtil;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class AlertResourcePoolBizImpl implements AlertResourcePoolBiz {
	private static final Logger LOGGER = LoggerFactory.getLogger(AlertResourcePoolBizImpl.class);

    @Autowired
    private AlertResourcePoolDao alertResourcePoolDao;

    @Autowired
	private CmdbClient  cmdbClient;

    @Value("${systemType:simple}")
    private String systemType;

	/**
	 * 同步设备分类排名数据
	 */
	@Override
	public void syncDistributionAlert(AlertRourcePoolVo pageRequest) {
		initDistributionData(pageRequest.getMonth());
		AlertRourcePoolVo dto = new AlertRourcePoolVo();
		BeanUtils.copyProperties(pageRequest, dto);
		List<AlertResourcePoolNew> list = alertResourcePoolDao.getResourcePoolaRecordAlertNew(dto);
		  if (list.size() > 0) {
			  Map<String,AlertResourcePoolNew> map = new HashMap<>();
			  for(AlertResourcePoolNew ap:list) {
				  String idcType = ap.getIdcType();
					String alertLevel = ap.getAlertLevel();
					String key = idcType + alertLevel;
					Integer count = ap.getCount();
					String deviceType = ap.getDeviceType();
					//ap.setMonth(pageRequest.getMonth());
				  if(map.containsKey(key)) {
					  AlertResourcePoolNew r = map.get(key);
					  setDeviceTypeCount(r,deviceType,count);
				  }else {
					  AlertResourcePoolNew r = new AlertResourcePoolNew();
					  BeanUtils.copyProperties(ap, r);
					  r.setMonth(pageRequest.getMonth());
					  boolean flag = setDeviceTypeCount(r,deviceType,count);
					  if(flag) {
						  map.put(key, r);
					  }
				  }

			  }
			  List<AlertResourcePoolNew> valList = new ArrayList(map.values());

			  for(AlertResourcePoolNew a:valList) {
				  alertResourcePoolDao.updateAlertDistribution(a);
			  }
		  }

	}


	void initDistributionData(String month){
		List<AlertResourcePoolNew> valList = Lists.newArrayList();
		  List<ConfigDict> dicTypeStatistics = cmdbClient.getDictsByType("dicType_statistics", "", "", "");
		  for(ConfigDict c:dicTypeStatistics) {
			  for(int i=0;i<AlertCommonConstant.ALERT_LEVELS.length;i++) {
				  AlertResourcePoolNew device = new AlertResourcePoolNew();
				  device.setIdcType(c.getValue());
				  //device.setDeviceType(cc.getValue());
				  device.setAlertLevel(AlertCommonConstant.ALERT_LEVELS[i]);
				  device.setMonth(month);
				  valList.add(device);
			  }
		  }
		  alertResourcePoolDao.insertAlertDistribution(valList);
	}

	boolean setDeviceTypeCount(AlertResourcePoolNew r,String deviceType,Integer count) {
		if(AlertCommonConstant.DEVICE_TYPE_CONFIG.physicalMachine.equals(deviceType)) {
			r.setPhysicalMachineCount(count);
		}else if(AlertCommonConstant.DEVICE_TYPE_CONFIG.router.equals(deviceType)) {
			r.setRouterCount(count);
		}else if(AlertCommonConstant.DEVICE_TYPE_CONFIG.cloudStorage.equals(deviceType)) {
			r.setCloudStorageCount(count);
		}else if(AlertCommonConstant.DEVICE_TYPE_CONFIG.diskArray.equals(deviceType)) {
			r.setDiskArrayCount(count);
		}else if(AlertCommonConstant.DEVICE_TYPE_CONFIG.firewall.equals(deviceType)) {
			r.setFirewallCount(count);
		}else if(AlertCommonConstant.DEVICE_TYPE_CONFIG.SDNController.equals(deviceType)) {
			r.setSdnControllerCount(count);
		}else if(AlertCommonConstant.DEVICE_TYPE_CONFIG.switchNode.equals(deviceType)) {
			r.setSwitchDeviceCount(count);
		}else if(AlertCommonConstant.DEVICE_TYPE_CONFIG.tapeLibrary.equals(deviceType)) {
			r.setTapeLibraryCount(count);
		}else {
			return false;
		}
		return true;
	}


	//同步告警总数
	@Override
	public void syncAlertTotal(AlertRourcePoolVo pageRequest) {
		initAlertTotal(pageRequest.getMonth());
		AlertRourcePoolVo dto = new AlertRourcePoolVo();
		BeanUtils.copyProperties(pageRequest, dto);
		 List<AlertResourcePool> countList = alertResourcePoolDao.getResourcePoolCountAlert(dto);
	        Map<String,AlertTotalVo> levelMap = new HashMap<>();
	        for(AlertResourcePool p:countList) {
	        	String idcType = p.getIdcType();
	        	int level = p.getAlertLevel();
	        	int count = p.getCount();
				AlertTotalVo t;
	        	if(!levelMap.containsKey(idcType)) {
	        		t = new  AlertTotalVo();
	        		t.setIdcType(idcType);
	        		if(null != pageRequest.getMonth()) {
	        			t.setMonth(pageRequest.getMonth());
	        		}

	        		levelMap.put(idcType, t);
	        	}else {
	        		t = levelMap.get(idcType);
	        	}
	        	if(level == 5) {
	        		t.setSeriousCount(count);
	        	}else if(level ==4) {
	        		t.setImportantCount(count);
	        	}else if(level ==3 ) {
	        		t.setSecondaryCount(count);
	        	}else {
	        		t.setTipsCount(count);
	        	}
	        	t.setSum();
	        }
	        //组装空数据
	        if (levelMap.size() > 0) {
	        	for(AlertTotalVo a:levelMap.values()) {
	        		alertResourcePoolDao.updateAlertTotal(a);
	        	}
	    }
	}

	void initAlertTotal(String month){
		List<AlertTotalVo> valList = Lists.newArrayList();
		 List<ConfigDict> dicTypeStatistics = cmdbClient.getDictsByType("dicType_statistics", "", "", "");
     	for(ConfigDict c:dicTypeStatistics) {
     		AlertTotalVo total = new AlertTotalVo();
     		total.setIdcType(c.getValue());
     		total.setMonth(month);
     		valList.add(total);
     	}
     	alertResourcePoolDao.insertAlertTotal(valList);
	}

	@Override
	public PageResponse<AlertDeviceTypeTop> getDeviceTop10Alert(PageRequest pageRequest) {
		Page page = PageUtil.convert(pageRequest);
		int count = alertResourcePoolDao.getDeviceTypeListCount(page);
		PageResponse<AlertDeviceTypeTop> pageResponse = new PageResponse<AlertDeviceTypeTop>();
		List<AlertDeviceTypeTop> list = new ArrayList<>();
		if(count>0) {
			List<AlertResourcePool> resultList = null;//alertResourcePoolDao.getDeviceTypeList(page);
			for(AlertResourcePool p:resultList) {
				page.getParams().put("idc_type", p.getIdcType());
				page.getParams().put("alert_level", p.getAlertLevel());
				page.getParams().put("device_type", p.getDeviceType());
				 List<AlertDeviceTypeTop>  tops = null;//alertResourcePoolDao.getDeviceTop10Alert(page);
				 int i=1;
				 for(AlertDeviceTypeTop top:tops) {
					 top.setOrder(i);
					 i++;
				 }
				 list.addAll(tops);
			}
		}
		pageResponse.setResult(list);
		pageResponse.setCount(count);
		return pageResponse;
	}
	/**
	 * 同步设备分类排名数据
	 */
	@Override
	public void syncDeviceTop10Alert(AlertRourcePoolVo pageRequest) {
		initDeviceTop10Alert(pageRequest.getMonth());

		List<AlertDeviceTypeTop> list = new ArrayList<>();
		List<AlertResourcePool> resultList = alertResourcePoolDao.getDeviceTypeList(pageRequest);
		for(AlertResourcePool p:resultList) {
			String[] ids = {p.getIdcType()};
			pageRequest.setIdcType(ids);
			pageRequest.setAlertLevel(p.getAlertLevel());
			pageRequest.setDeviceType(p.getDeviceType());
			pageRequest.setPageSize(10);
			 List<AlertDeviceTypeTop>  tops = alertResourcePoolDao.getDeviceTop10Alert(pageRequest);
			 int i=1;
			 for(AlertDeviceTypeTop top:tops) {
				 top.setOrder(i);
				 top.setMonth(pageRequest.getMonth());
				 i++;
			 }
			 list.addAll(tops);
		}
		  if (list.size() > 0) {
			  for(AlertDeviceTypeTop a:list) {
				  alertResourcePoolDao.updateAlertDeviceTop(a);
			  }
		  }

	}

	void initDeviceTop10Alert(String month){
		List<AlertDeviceTypeTop> valList = Lists.newArrayList();
		 List<ConfigDict> dicTypeStatistics = cmdbClient.getDictsByType("dicType_statistics", "", "", "");
		  List<ConfigDict> moniterItems = cmdbClient.getDictsByType("device_type_view", "", "", "");
		  for(ConfigDict c:dicTypeStatistics) {
			  for(ConfigDict cc:moniterItems) {
				  for(int i=0;i<AlertCommonConstant.ALERT_LEVELS.length;i++) {
					  String level = AlertCommonConstant.ALERT_LEVELS[i];
					  for(int j=0;j<10;j++) {
						  AlertDeviceTypeTop device = new AlertDeviceTypeTop();
						  device.setIdcType(c.getValue());
						  device.setDeviceType(cc.getValue());
						  device.setAlertLevel(level);
						  device.setOrder(j+1);
						  device.setMonth(month);
						  valList.add(device);
					  }
				  }
			  }
		  }
		  alertResourcePoolDao.insertAlertDeviceTop(valList);
	}

	@Override
	public void syncMoniterTop10Alert(AlertRourcePoolVo pageRequest) {
		initMoniterTop10Alert(pageRequest.getMonth());

		List<AlertMoniterObjectTop> list = new ArrayList<>();
		List<AlertResourcePool> resultList = alertResourcePoolDao.getDeviceTypeList(pageRequest);
		for(AlertResourcePool p:resultList) {
			String[] ids = {p.getIdcType()};
			pageRequest.setIdcType(ids);
			pageRequest.setAlertLevel(p.getAlertLevel());
			pageRequest.setDeviceType(p.getDeviceType());
			pageRequest.setPageSize(10);
			 List<AlertMoniterObjectTop>  tops = alertResourcePoolDao.getMoniterTop10Alert(pageRequest);
			 int i=1;
			 for(AlertMoniterObjectTop top:tops) {
				 int count = top.getCount();
				 int sum = top.getSum();
				 BigDecimal bi1 = new BigDecimal(count);
				 BigDecimal bi2 = new BigDecimal(sum);
				 BigDecimal divide = bi1.divide(bi2, 4, RoundingMode.HALF_UP);
				 top.setRate(divide.toString());
				 top.setOrder(i);
				 top.setMonth(pageRequest.getMonth());
				 i++;
			 }
			 list.addAll(tops);
		}
		if (list.size() > 0) {
			for(AlertMoniterObjectTop a:list) {
				alertResourcePoolDao.updateAlertMonisterTop(a);
			}
		  }
	}

	void initMoniterTop10Alert(String month){
		List<AlertMoniterObjectTop> list = Lists.newArrayList();
		List<ConfigDict> dicTypeStatistics = cmdbClient.getDictsByType("dicType_statistics", "", "", "");
		  List<ConfigDict> moniterItems = cmdbClient.getDictsByType("device_type_view", "", "", "");
		  for(ConfigDict c:dicTypeStatistics) {
			  for(ConfigDict cc:moniterItems) {
				  for(int i=0;i<AlertCommonConstant.ALERT_LEVELS.length;i++) {
					  String level = AlertCommonConstant.ALERT_LEVELS[i];
					  for(int j=0;j<10;j++) {
						  AlertMoniterObjectTop device = new AlertMoniterObjectTop();
						  device.setIdcType(c.getValue());
						  device.setDeviceType(cc.getValue());
						  device.setAlertLevel(level);
						  device.setOrder(j+1);
						  device.setMonth(month);
						  list.add(device);
					  }
				  }
			  }
		  }
		  alertResourcePoolDao.insertAlertMonisterTop(list);
	}

	@Override
	public PageResponse<AlertInspectionDaily> getInspectionDaily(PageRequest pageRequest) {
		Page page = PageUtil.convert(pageRequest);
		//Map<String,String> dictMap = getDictMap();
		//pageRequest.addFields("cpuMoniter", dictMap.get(AlertCommonConstant.MONITER_OBJECT_TYPE_CPU));
		//pageRequest.addFields("memoryMoniter", dictMap.get(AlertCommonConstant.MONITER_OBJECT_TYPE_MEMORY));
		int count = alertResourcePoolDao.getInspectionDailyCount(page);
		List<AlertInspectionDaily> list = Lists.newArrayList();
		if(count>0) {
			
			list = alertResourcePoolDao.getInspectionDaily(page);
			
		}
		PageResponse<AlertInspectionDaily> pageResponse = new PageResponse<AlertInspectionDaily>();
		pageResponse.setCount(count);
		pageResponse.setResult(list);
		return pageResponse;
	}
	
	/**
     * 查询导出数据
     * @param pageRequest
     * @return
     */
    @Override
    public List<Map<String,Object>> queryExportData(PageRequest pageRequest) {
    	Page page = PageUtil.convert(pageRequest);
		Map<String,String> dictMap = getDictMap();
		pageRequest.addFields("cpuMoniter", dictMap.get(AlertCommonConstant.MONITER_OBJECT_TYPE_CPU));
		pageRequest.addFields("memoryMoniter", dictMap.get(AlertCommonConstant.MONITER_OBJECT_TYPE_MEMORY));
			
		List<Map<String,Object>> result = alertResourcePoolDao.exportInspectionDaily(page);
		
		return result;
    }
	
    @Override
    public   Map<String,String> getDictMap(){
		List<ConfigDict> moniterDicts = cmdbClient.getDictsByType(AlertCommonConstant.MONITER_OBJECT_TYPE, "", "", "");
		Map<String,String> dictMap = new HashMap<>();
		//dictMap.put(AlertCommonConstant.MONITER_OBJECT_TYPE_CPU,"General");
		//dictMap.put(AlertCommonConstant.MONITER_OBJECT_TYPE_MEMORY,"Zabbix agent");
		
		 for(ConfigDict dict:moniterDicts) {
			 dictMap.put(dict.getName(),dict.getValue()); 
		 }
		 
		return dictMap;
		
	}
	
    @Override
    public  String[] getDefaultIdcType(){
    	List<ConfigDict> dicTypeDefault = cmdbClient.getDictsByType(AlertCommonConstant.DICTYPE_DEFAULT, null, null, null);
    	String[] idcDefaults = {};
    	if(dicTypeDefault.size()>0) {
    		idcDefaults = new String[dicTypeDefault.size()];
	 		int i=0;
	 		for(ConfigDict cd:dicTypeDefault) {
	   	 		idcDefaults[i] = cd.getValue();
	   	 		i++;
	 		}
    	}
    	return idcDefaults;
    }

    @Override
	public void deleteCountByMonth(String month) {
    	alertResourcePoolDao.deleteCountByMonth(month);
	}

    @Override
	public void deleteRecordByMonth(String month) {
    	alertResourcePoolDao.deleteRecordByMonth(month);
	}

    @Override
	public void deleteDeviceByMonth(String month) {
    	alertResourcePoolDao.deleteDeviceByMonth(month);
	}

    @Override
	public void deleteMoniterByMonth(String month) {
    	alertResourcePoolDao.deleteMoniterByMonth(month);
	}

	/*
	 * @Override public List<AlertRecord> getRecordByNewTable(String month) {
	 * List<AlertResourcePoolNew> records =
	 * alertResourcePoolDao.getResourcePoolaRecordFromNew(month);
	 * Map<String,AlertRecord> map = new HashMap<>(); for(AlertResourcePoolNew
	 * a:records) { String idcType = a.getIdcType(); String alertLevel =
	 * a.getAlertLevel(); String key = idcType + alertLevel; String deviceType =
	 * a.getDeviceType(); Integer count = a.getCount(); if(map.containsKey(key)) {
	 * AlertRecord r = map.get(key); r.getDeviceTypeMap().put(deviceType, count);
	 * }else { AlertRecord r = new AlertRecord(); r.setAlertLevel(alertLevel);
	 * r.setIdcType(a.getIdcType()); r.getDeviceTypeMap().put(deviceType, count);
	 * map.put(key,r ); } } return new ArrayList(map.values()); }
	 */
    
    public static void main(String[] args) {
    	String sql = "select sum(count) count,idc_type,alert_level,device_type,ip,pod,device_mfrs,roomId from(" + 
    			"	 	select count(*) count,a.idc_type," + 
    			"	 	case " + 
    			"	    when a.alert_level = 5 then '重大告警'" + 
    			"	    when  a.alert_level = 4 then '高告警'" + 
    			"		when  a.alert_level = 3 then '中告警'" + 
    			"		when  a.alert_level = 2 then '低告警'" + 
    			"		else alert_level end" + 
    			"		 alert_level" + 
    			"		  ,i.device_type,i.ip,concat(a.idc_type,i.pod_name)pod " + 
    			"		,i.device_mfrs,i.roomId from alert_alerts a,cmdb.cmdb_instance i " + 
    			"		where  a.device_id = i.id " + 
    			"		and a.idc_type in ('信息港资源池')" + 
    			"" + 
    			"         " + 
    			"             and a.cur_moni_time like  CONCAT('2019-09','%')" + 
    			"		<include refid=\"querysql\"></include>" + 
    			"		group by a.idc_type,a.alert_level ,i.device_type,i.ip,i.pod_name,i.device_mfrs,i.roomId" + 
    			"		order  by count desc" + 
    			"		limit 10" + 
    			"		)mm" + 
    			"		group by idc_type,alert_level ,device_type,ip,pod,device_mfrs,roomId" + 
    			"		order by count desc";
    	
    }

}
