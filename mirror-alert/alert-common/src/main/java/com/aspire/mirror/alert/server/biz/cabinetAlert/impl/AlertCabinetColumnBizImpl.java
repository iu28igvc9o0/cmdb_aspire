package com.aspire.mirror.alert.server.biz.cabinetAlert.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.aspire.mirror.alert.server.vo.cabinetAlert.AlertCabinetColumnVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.aspire.mirror.alert.server.biz.cabinetAlert.AlertCabinetColumnBiz;
import com.aspire.mirror.alert.server.biz.cabinetAlert.AlertCabinetColumnCommonBiz;
import com.aspire.mirror.alert.server.constant.AlertCommonConstant;
import com.aspire.mirror.alert.server.dao.cabinetAlert.AlertCabinetColumnDao;
import com.aspire.mirror.alert.server.dao.common.AlertScheduleIndexDao;
import com.aspire.mirror.alert.server.dao.cabinetAlert.po.AlertCabinetColumnConfig;
import com.aspire.mirror.alert.server.dao.cabinetAlert.po.AlertCabinetColumnConfigData;
import com.aspire.mirror.alert.server.vo.common.AlertScheduleIndexVo;
import com.aspire.mirror.alert.server.util.MapUtils;
import com.aspire.mirror.alert.server.dao.alert.AlertsHisV2Dao;
import com.aspire.mirror.alert.server.dao.alert.AlertsV2Dao;
import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.PageUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class AlertCabinetColumnBizImpl implements AlertCabinetColumnBiz{
	Logger LOGGER = LoggerFactory.getLogger(AlertCabinetColumnBizImpl.class);
	@Autowired
    private AlertCabinetColumnDao alertCabinetColumnDao;
	@Autowired
    private AlertScheduleIndexDao alertScheduleIndexDao;
	@Autowired
	private AlertCabinetColumnCommonBiz alertCabinetColumnCommonBiz;
	@Autowired
	private AlertsHisV2Dao alertsHisV2Dao;
	@Autowired
	private AlertsV2Dao alertsV2Dao;
	
	@Value("${AlertCabinetColumnTask.itemInfo:}")
	private String itemInfo;
	
	@Override
	public void updateConfig(List<AlertCabinetColumnConfig> configList) {
	
		List<AlertCabinetColumnConfigData> data = Lists.newArrayList();
		List<AlertCabinetColumnConfig> configInsertList = Lists.newArrayList();
		
		Date date = new Date();

		AlertCabinetColumnConfigData configData = new AlertCabinetColumnConfigData();
		for(AlertCabinetColumnConfig config: configList) {
			int configType = config.getConfigType();
			config.setUpdateTime(date);
			if(configType==1) {
				config.setConfigType(null);
			}else if(configType==2) {
				config.setConfigType(3);
			}
			int  num = alertCabinetColumnDao.updateConfig(config);//更新配置
			boolean initailFlag = false;//新增的配置
			if(num ==0) {
				configInsertList.add(config);
				initailFlag = getInitialConfigData(config);
			}
			if(!initailFlag) {
				configData.setTimeRange(config.getTimeRange());
				configData.setAlertPercentage(config.getAlertPercentage());
				configData.setUpdateTime(date);
				alertCabinetColumnDao.updateConfigDataByConfig(configData);//更新配置的列头柜数据
				List<AlertCabinetColumnConfigData> temp = alertCabinetColumnCommonBiz.getConfigData(config);//获取可能原来没有配置的列头柜,再配置保存
				if (temp.size() > 0) {
					data.addAll(temp);
				}
				//删除不存在的列头柜
				alertCabinetColumnCommonBiz.delConfigData(config);
			}
			
			
		}
		//新增的配置
		if (configInsertList.size() > 0) {
			alertCabinetColumnDao.batchInsertConfig(configInsertList);//新增
		}
		//新增的列头柜数据
		if (data.size() > 0) {
			alertCabinetColumnDao.batchInsertCabinetColumnData(data);
		}
		 
	}
	

	
	
	@Override
	public boolean getInitialConfigData(AlertCabinetColumnConfig config){
		int configType = config.getConfigType();
		int timeRange = config.getTimeRange();
		int alertPercentage = config.getAlertPercentage();
		Map<String,Object> params = Maps.newHashMap();
		if(configType==2) {//资源池
			params.put("idcType", config.getIdcType());
		}else if(configType==3){//机房
			params.put("idcType", config.getIdcType());
			params.put("roomId", config.getRoomId());
		}
		boolean flag = true;
		List<AlertCabinetColumnConfigData> dataList = alertCabinetColumnDao.getConfigDataList(params);
		if(null!=dataList && dataList.size()>0) {
			flag = false;
			return flag;
		}
		Date date = new Date();
		List<AlertCabinetColumnConfigData> data = alertCabinetColumnDao.selectCabinetList(params);
		for(AlertCabinetColumnConfigData d:data) {
			d.setTimeRange(timeRange);
			d.setAlertPercentage(alertPercentage);
			//d.setConfigId(config.getId());
			d.setStatus(1);
			d.setAlertStatus(1);
			d.setCreateTime(date);
			d.setUpdateTime(date);
		}
		if(data.size()>0) {
			alertCabinetColumnDao.batchInsertCabinetColumnData(data);
		}
		return flag;
	}
	
	@Override
	public AlertCabinetColumnConfig getConfig(AlertCabinetColumnVo req) {
		Map<String,Object> map = MapUtils.entityToMap(req);
		return alertCabinetColumnDao.getConfig(map);
	}

	@Override
	public void updateConfigData(AlertCabinetColumnVo req) {
		String id = req.getId();
		Map<String,Object> params = Maps.newHashMap();
		params.put("ids", id.split(","));
		params.put("status", req.getStatus());
		params.put("updateTime", new Date());
		params.put("editor", req.getEditor());
		alertCabinetColumnDao.updateConfigDataById(params);
	}

	@Override
	public PageResponse<AlertCabinetColumnConfigData> queryCabinetColumnAlertPageList(PageRequest pageRequest) {
		Page page = PageUtil.convert(pageRequest);
		Map<String,Object> params = page.getParams();
		Integer alertStatus = params.get("alertStatus")==null?0:Integer.parseInt(page.getParams().get("alertStatus").toString());
		Integer status = params.get("status")==null?2:Integer.parseInt(page.getParams().get("status").toString());
        int count = 0;
        if(status==0) {
        	count = alertCabinetColumnDao.queryCCAlertFPageListCount(page);
        }else if(status==1 && alertStatus ==1) {
        	params.put("cabinetType", 1);
        	params.put("cabinetTitle", AlertCommonConstant.CABINET_ALERT_TITLE);
        	params.put("cabinetColumnTitle", AlertCommonConstant.CABINETCOLUMN_ALERT_TITLE);
        	count = alertCabinetColumnDao.queryCCAlertNPageListCount(page);
        }else if(status==1 && alertStatus ==2) {
        	count = alertCabinetColumnDao.queryCCAlertHPageListCount(page);
        }else if(status==1 && alertStatus ==3) {
        	count = alertCabinetColumnDao.queryCCAlertMPageListCount(page);
        }else if(status==1 && alertStatus ==4) {
        	params.put("cabinetType", 4);
        	params.put("cabinetColumnTitle", AlertCommonConstant.CABINETCOLUMN_ALERT_TITLE);
        	count = alertCabinetColumnDao.queryCCAlertDPageListCount(page);
        }else {
        	count = alertCabinetColumnDao.queryCCAlertPageListCount(page);
        }
        	
        PageResponse<AlertCabinetColumnConfigData> pageResponse = new PageResponse<AlertCabinetColumnConfigData>();
        pageResponse.setCount(count);
        List<AlertCabinetColumnConfigData> listAlertFilter  = Lists.newArrayList();
        if (count > 0) {
            if(status==0) {
            	listAlertFilter = alertCabinetColumnDao.queryCCAlertFPageList(page);
            }else if(status==1 && alertStatus ==1) {
            	listAlertFilter = alertCabinetColumnDao.queryCCAlertNPageList(page);
            }else if(status==1 && alertStatus ==2) {
            	listAlertFilter = alertCabinetColumnDao.queryCCAlertHPageList(page);
            }else if(status==1 && alertStatus ==3) {
            	listAlertFilter = alertCabinetColumnDao.queryCCAlertMPageList(page);
            }else if(status==1 && alertStatus ==4) {
            	listAlertFilter = alertCabinetColumnDao.queryCCAlertDPageList(page);
            }else {
            	listAlertFilter = alertCabinetColumnDao.queryCCAlertPageList(page);
            }
            
           //设置列头柜告警状态
			/*
			 * for(AlertCabinetColumnConfigData a:listAlertFilter) { int
			 * cabinetColumnAlertCount = a.getCabinetColumnAlertCount(); int
			 * cabinetAlertCount = a.getCabinetAlertCount(); int deviceAlertCount =
			 * a.getDeviceAlertCount(); if(cabinetColumnAlertCount>0) { a.setAlertStatus(2);
			 * }else if(cabinetAlertCount>0){ a.setAlertStatus(3); }else
			 * if(deviceAlertCount>0) { a.setAlertStatus(4); }else { a.setAlertStatus(1); }
			 * 
			 * }
			 */
        }
        pageResponse.setResult(listAlertFilter);
        return pageResponse;
	}

	@Override
	public List<AlertCabinetColumnConfigData> queryCabinetColumnAlert(PageRequest pageRequest) {
		Page page = PageUtil.convert(pageRequest);
			page.setPageSize(null);
			Map<String,Object> params = page.getParams();
			Integer alertStatus = params.get("alertStatus")==null?0:Integer.parseInt(page.getParams().get("alertStatus").toString());
			Integer status = params.get("status")==null?2:Integer.parseInt(page.getParams().get("status").toString());
	     
			List<AlertCabinetColumnConfigData> listAlertFilter;
			 if(status==0) {
				 listAlertFilter = alertCabinetColumnDao.queryCCAlertFPageList(page);
		        }else if(status==1 && alertStatus ==1) {
		        	params.put("cabinetType", 1);
		        	params.put("cabinetTitle", AlertCommonConstant.CABINET_ALERT_TITLE);
		        	params.put("cabinetColumnTitle", AlertCommonConstant.CABINETCOLUMN_ALERT_TITLE);
		        	listAlertFilter = alertCabinetColumnDao.queryCCAlertNPageList(page);
		        }else if(status==1 && alertStatus ==2) {
		        	listAlertFilter = alertCabinetColumnDao.queryCCAlertHPageList(page);
		        }else if(status==1 && alertStatus ==3) {
		        	listAlertFilter = alertCabinetColumnDao.queryCCAlertMPageList(page);
		        }else if(status==1 && alertStatus ==4) {
		        	params.put("cabinetType", 4);
		        	params.put("cabinetColumnTitle", AlertCommonConstant.CABINETCOLUMN_ALERT_TITLE);
		        	listAlertFilter = alertCabinetColumnDao.queryCCAlertDPageList(page);
		        }else {
		        	listAlertFilter = alertCabinetColumnDao.queryCCAlertPageList(page);
		        }
         
        return listAlertFilter;
	}
	
	
	@Override
	public PageResponse<Map<String,Object>> queryCabinetAlertPageList(PageRequest pageRequest) {
		Page page = PageUtil.convert(pageRequest);
		
        int count = alertCabinetColumnDao.queryCabinetAlertPageListCount(page);
        PageResponse<Map<String,Object>> pageResponse = new PageResponse<Map<String,Object>>();
        pageResponse.setCount(count);
        List<Map<String,Object>> listAlertFilter = Lists.newArrayList();
        if (count > 0) {
            listAlertFilter = alertCabinetColumnDao.queryCabinetAlertPageList(page);
          
            
        }
        pageResponse.setResult(listAlertFilter);
        return pageResponse;
	}

	//@Override
	public List<Map<String,Object>> queryCabinetAlert(PageRequest pageRequest) {
		Page page = PageUtil.convert(pageRequest);
		List<Map<String,Object>> listAlertFilter = alertCabinetColumnDao.queryCabinetAlertPageList(page);
          
        return listAlertFilter;
	}

	@Override
	public String getScheduleConfig(String indexType) {
		List<AlertScheduleIndexVo> list = alertScheduleIndexDao.getAlertScheduleIndexDetail(indexType);
		String value = list.get(0).getIndexValue();
		
		return value;
	}

	@Override
	public PageResponse<Map<String, Object>> queryDeviceAlertPageList(PageRequest pageRequest) {
		Page page = PageUtil.convert(pageRequest);
		Map<String, Object> map = pageRequest.getDynamicQueryFields();
		String cabinetColumn = org.apache.commons.collections.MapUtils.getString(map, "cabinetColumn");
		String cabinetName = org.apache.commons.collections.MapUtils.getString(map, "cabinetName");
		PageResponse<Map<String,Object>> pageResponse = new PageResponse<Map<String,Object>>();
		List<Map<String,Object>> listAlertFilter = Lists.newArrayList();
		int count=0;
		if (StringUtils.isNotBlank(cabinetName)){
			//查询机柜
			count=alertCabinetColumnDao.queryDeviceAlertColumnPageListCount(page);
			if (count > 0) {
				listAlertFilter = alertCabinetColumnDao.queryDeviceAlertColumnPageList(page);
			}
		}else if (StringUtils.isNotBlank(cabinetColumn)){
				count = alertCabinetColumnDao.queryDeviceAlertPageListCount(page);
				if (count > 0) {
					listAlertFilter = alertCabinetColumnDao.queryDeviceAlertPageList(page);
			}
		}
		pageResponse.setCount(count);
        pageResponse.setResult(listAlertFilter);
        return pageResponse;
	}

	@Override
	public PageResponse<Map<String, Object>> queryBizSystemAlertPageList(PageRequest pageRequest) {
		Page page = PageUtil.convert(pageRequest);
		
        int count = 0;
        //根据告警状态查询业务系统数:正常的查全部的业务系统
        String alertStatusStr = MapUtils.getString(page.getParams(), "alertStatus");
        String statusStr = MapUtils.getString(page.getParams(), "status");
        PageResponse<Map<String,Object>> pageResponse = new PageResponse<Map<String,Object>>();
        List<Map<String,Object>> listAlertFilter = Lists.newArrayList();
        if(null!=alertStatusStr && alertStatusStr.equals("1") || null!=statusStr&&statusStr.equals("0")){
        	count = alertCabinetColumnDao.queryBizSystemNormalPageListCount(page);
        	 if (count > 0) {
        		 listAlertFilter = alertCabinetColumnDao.queryBizSystemNormalPageList(page);
        		 pageResponse.setResult(listAlertFilter);
        	 }
        	
        }else {
        	count = alertCabinetColumnDao.queryBizSystemPageListCount(page);
        	 if (count > 0) {
        		 listAlertFilter = alertCabinetColumnDao.queryBizSystemPageList(page);
        		 pageResponse.setResult(listAlertFilter);
        	 }
        }
        pageResponse.setCount(count);
        pageResponse.setResult(listAlertFilter);
        return pageResponse;
	}
	
	@Override
	public List<Map<String, Object>> queryBizSystemAlertList(PageRequest pageRequest) {
		Page page = PageUtil.convert(pageRequest);
		page.setPageSize(null);
		 String alertStatusStr = MapUtils.getString(page.getParams(), "alertStatus");
		 List<Map<String,Object>> listAlertFilter = null;
            if(null!=alertStatusStr && alertStatusStr.equals("1")){
            	listAlertFilter = alertCabinetColumnDao.queryBizSystemNormalPageList(page);
            }else {
            	listAlertFilter = alertCabinetColumnDao.queryBizSystemPageList(page);
            }
           
         
        return listAlertFilter;
	}



	/**
	 * 根据告警类型查询告警相关业务系统信息
	 */
	@Override
	public PageResponse<Map<String, Object>> queryRelateBizsystemList(PageRequest pageRequest) {
		Page page = PageUtil.convert(pageRequest);
		Map<String,Object> params = page.getParams();
		String alertId = params.get("alertId").toString();
        int alertType = org.apache.commons.collections.MapUtils.getIntValue(page.getParams(), "alertType");
        //是否分页
        boolean pageFLag =  org.apache.commons.collections.MapUtils.getBooleanValue(page.getParams(), "pageFlag");
        
        PageResponse<Map<String,Object>> pageResponse = new PageResponse<Map<String,Object>>();
        List<Map<String,Object>> listAlertFilter = Lists.newArrayList();
        int count = 0;
       
        Map<String, Object> alert = null;
        if(alertType ==1) {
        	 alert = alertsV2Dao.detailById(alertId);
        }else if(alertType ==2){
        	alert = alertsHisV2Dao.detailById(alertId);
        }else {
        	log.error("告警状态不存在，请核查");
        }
        if(null==alert) {
        	log.error("告警不存在，请核查");
        	pageResponse.setResult(listAlertFilter);
        	return pageResponse;
        }
        JSONArray array = JSONArray.parseArray(itemInfo);
        params.put("deviceItem", array);
        String keyComment = MapUtils.getString(alert, "key_comment");
        String idcCabinet = MapUtils.getString(alert, "idc_cabinet");
        if(keyComment.equals(AlertCommonConstant.CABINETCOLUMN_ALERT_TITLE)) {
        	params.put("cabinetColumn", idcCabinet.substring(0, 1));
        }else {
        	params.put("cabinetName", idcCabinet);
        }
        String idcType = MapUtils.getString(alert, "idc_type");
        String roomId = MapUtils.getString(alert, "source_room");
        params.put("idcType", idcType);
        params.put("roomId", roomId);
        
        if(alertType ==1) {
        	if(pageFLag) {
        		count = alertCabinetColumnDao.queryBizSystemPageListCount(page);
                if (count > 0) {
        	   		 listAlertFilter = alertCabinetColumnDao.queryBizSystemPageList(page);
        	   		 pageResponse.setResult(listAlertFilter);
        	   	 }
        	}else {
        		page.setPageSize(null);
        		listAlertFilter = alertCabinetColumnDao.queryBizSystemPageList(page);
        	}
        	
        }else {
        	if(pageFLag) {
	        	count = alertCabinetColumnDao.queryBizSystemHisPageListCount(page);
	            if (count > 0) {
	    	   		 listAlertFilter = alertCabinetColumnDao.queryBizSystemHisPageList(page);
	    	   		 pageResponse.setResult(listAlertFilter);
	    	   	 }
        	}else {
        		page.setPageSize(null);
        		 listAlertFilter = alertCabinetColumnDao.queryBizSystemHisPageList(page);
        	}
        }
        for(Map<String,Object> m:listAlertFilter) {
        	m.put("biz_sys", m.get("bizSystem"));
        	m.put("deviceCount", m.get("deviceAlertCount"));
        }
        pageResponse.setCount(count);
        pageResponse.setResult(listAlertFilter);
        return pageResponse;
	}

	/**
	 * 列头柜任务
	 * @throws Exception
	 */
	@Override
	public void CabinetColumnTask() throws Exception{
		List<AlertCabinetColumnConfig> list = alertCabinetColumnDao.getConfigList();
		Map<String,AlertCabinetColumnConfig> roomMap = Maps.newHashMap();
		Map<String,AlertCabinetColumnConfig> idcTypeMap = Maps.newHashMap();
		AlertCabinetColumnConfig commonConfig = null;
		for(AlertCabinetColumnConfig c:list) {
			int configType = c.getConfigType();
			String roomId = c.getRoomId();
			String idcType = c.getIdcType();
			if(configType==3) {
				String key = idcType+"_"+roomId;
				roomMap.put(key, c);
			}
			if(configType==2) {
				String key = idcType;
				idcTypeMap.put(key, c);
			}else {
				commonConfig = c;
			}

		}
		AlertCabinetColumnConfig config = new AlertCabinetColumnConfig();
		config.setConfigType(1);
		//删除不存在的列头柜
		alertCabinetColumnCommonBiz.delConfigData(config);
		//设置业务系统、设备、机柜数量
		int  num = alertCabinetColumnDao.updateConfigDataAll();
		log.info("***update num:{}****",num);

		//获取可能原来没有配置的列头柜,再配置保存
		List<AlertCabinetColumnConfigData> temp = alertCabinetColumnCommonBiz.getConfigData(config);//获取可能原来没有配置的列头柜,再配置保存
		if(null!=temp && temp.size()>0) {
			for(AlertCabinetColumnConfigData a:temp) {
				String roomId = a.getRoomId();
				String idcType = a.getIdcType();
				String roomKey = idcType+"_"+roomId;
				AlertCabinetColumnConfig entity ;
				if(roomMap.containsKey(roomKey)) {
					entity = roomMap.get(roomKey);
					a.setAlertPercentage(entity.getAlertPercentage());
					a.setTimeRange(entity.getTimeRange());
				}else if(idcTypeMap.containsKey(roomKey)) {
					entity = idcTypeMap.get(roomKey);
					a.setAlertPercentage(entity.getAlertPercentage());
					a.setTimeRange(entity.getTimeRange());
				}else {
					a.setAlertPercentage(commonConfig.getAlertPercentage());
					a.setTimeRange(commonConfig.getTimeRange());
				}
			}

			alertCabinetColumnDao.batchInsertCabinetColumnData(temp);
		}

	}
  
   
}
