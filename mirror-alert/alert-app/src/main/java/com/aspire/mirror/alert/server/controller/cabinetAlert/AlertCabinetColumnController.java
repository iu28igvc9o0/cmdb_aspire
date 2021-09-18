package com.aspire.mirror.alert.server.controller.cabinetAlert;

import java.util.List;
import java.util.Map;

import com.aspire.mirror.alert.api.dto.cabinetAlert.AlertCabinetColumnConfigDTO;
import com.aspire.mirror.alert.api.dto.cabinetAlert.AlertCabinetColumnConfigDataDTO;
import com.aspire.mirror.alert.api.dto.cabinetAlert.AlertCabinetColumnReq;
import com.aspire.mirror.alert.api.service.cabinetAlert.AlertCabinetColumnService;
import com.aspire.mirror.alert.server.util.PayloadParseUtil;
import com.aspire.mirror.alert.server.vo.cabinetAlert.AlertCabinetColumnVo;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONArray;
import com.aspire.mirror.alert.server.biz.cabinetAlert.AlertCabinetColumnBiz;
import com.aspire.mirror.alert.server.constant.AlertCommonConstant;
import com.aspire.mirror.alert.server.constant.ConstantsCmdb;
import com.aspire.mirror.alert.server.dao.cabinetAlert.po.AlertCabinetColumnConfig;
import com.aspire.mirror.alert.server.dao.cabinetAlert.po.AlertCabinetColumnConfigData;
import com.aspire.mirror.alert.server.helper.UtilsHelper;
import com.aspire.mirror.alert.server.util.Utils;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.FieldUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 告警控制层
 * <p>
 * 项目名称: mirror平台 包: com.aspire.mirror.alert.server.controller 类名称:
 * AlertsHisController.java 类描述: 告警控制层 创建人: JinSu 创建时间: 2018/9/14 17:51 版本: v1.0
 */
@RestController
public class AlertCabinetColumnController implements AlertCabinetColumnService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AlertCabinetColumnController.class);
	@Autowired
	private AlertCabinetColumnBiz alertCabinetColumnBiz;
	
	@Autowired
	private UtilsHelper utilsHelper;
	@Value("${AlertCabinetColumnTask.itemInfo:}")
	private String itemInfo;
	
	//配置列头柜告警
	@Override
	public ResponseEntity<String> initalConfig() {
		AlertCabinetColumnReq req = new AlertCabinetColumnReq();
		req.setConfigType(1);
		AlertCabinetColumnConfig config = alertCabinetColumnBiz.getConfig(PayloadParseUtil.jacksonBaseParse(AlertCabinetColumnVo.class, req));
		
		if(null!=config && config.getConfigType()==1) {
			alertCabinetColumnBiz.getInitialConfigData(config);
		}
		
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

	//初始化列头柜告警配置
	@Override
	public ResponseEntity<String> manageConfig(@RequestBody List<AlertCabinetColumnConfigDTO> configList) {
		if (configList == null) {
			LOGGER.error("manageConfig configList is null");
			throw new RuntimeException("manageConfig configList is null");
		}
		alertCabinetColumnBiz.updateConfig(PayloadParseUtil.jacksonBaseParse(AlertCabinetColumnConfig.class, configList));
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}
	
	
	//查询列头柜告警配置
	@Override
	public AlertCabinetColumnConfigDTO getConfig(@RequestBody AlertCabinetColumnReq req) {
		if (req == null) {
			LOGGER.error("getConfig req is null");
			throw new RuntimeException("getConfig req is null");
		}
		int configType = req.getConfigType();
		AlertCabinetColumnConfig config = alertCabinetColumnBiz.getConfig(PayloadParseUtil.jacksonBaseParse(AlertCabinetColumnVo.class, req));
		if(null==config) {
			if(configType==3) {
				req.setRoomId(null);
				configType = 2;
				req.setConfigType(2);
				config = alertCabinetColumnBiz.getConfig(PayloadParseUtil.jacksonBaseParse(AlertCabinetColumnVo.class, req));
			}
			if(config==null && configType==2) {
				req.setConfigType(1);
				req.setIdcType(null);
				config = alertCabinetColumnBiz.getConfig(PayloadParseUtil.jacksonBaseParse(AlertCabinetColumnVo.class, req));
			}
		}
		AlertCabinetColumnConfigDTO dto = new AlertCabinetColumnConfigDTO();
		BeanUtils.copyProperties(config, dto);
		return dto;
	}
	 /**
	  * 更新列头柜配置状态
	  */
	@Override
	public ResponseEntity<String> updateStatus(@RequestBody AlertCabinetColumnReq req) {
		if (req == null) {
			LOGGER.error("updateStatus req is null");
			throw new RuntimeException("updateStatus req is null");
		}
		 alertCabinetColumnBiz.updateConfigData(PayloadParseUtil.jacksonBaseParse(AlertCabinetColumnVo.class, req));
	
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}
	/**
	 * 查询列头柜告警
	 */
	@Override
	public PageResponse<AlertCabinetColumnConfigDataDTO> queryCabinetColumnAlertPageList(@RequestBody AlertCabinetColumnReq req) {
		if (req == null) {
			LOGGER.error("queryCabinetAlert req is null");
			throw new RuntimeException("queryCabinetAlert req is null");
		}
		PageRequest page = new PageRequest();
		BeanUtils.copyProperties(req, page, Utils.getNullPropertyNames(req));
		Map<String, Object> map = FieldUtil.getFiledMap(req);
		for (String key : map.keySet()) {
			page.addFields(key, map.get(key));
		}
	
		
		JSONArray array = JSONArray.parseArray(itemInfo);
		page.addFields("source", AlertCommonConstant.CABINET_COLUMN_SOURCE);
		page.addFields("keyComment", AlertCommonConstant.CABINET_ALERT_TITLE);
		page.addFields("deviceItem", array);
		page.addFields("keyCommentColumn", AlertCommonConstant.CABINETCOLUMN_ALERT_TITLE);
		PageResponse<AlertCabinetColumnConfigData> pageResult =  alertCabinetColumnBiz.queryCabinetColumnAlertPageList(page);
		List<AlertCabinetColumnConfigDataDTO> listAlert = Lists.newArrayList();
		if (!CollectionUtils.isEmpty(pageResult.getResult())) {
			for (AlertCabinetColumnConfigData filter : pageResult.getResult()) {
				AlertCabinetColumnConfigDataDTO filterDTO = new AlertCabinetColumnConfigDataDTO();
				BeanUtils.copyProperties(filter, filterDTO);
				listAlert.add(filterDTO);
			}
		}

		PageResponse<AlertCabinetColumnConfigDataDTO> result = new PageResponse<AlertCabinetColumnConfigDataDTO>();
		result.setCount(pageResult.getCount());
		result.setResult(listAlert);
		return result;
	}
	
	
	@Override
	public List<AlertCabinetColumnConfigDataDTO> queryCabinetColumnAlert(@RequestBody AlertCabinetColumnReq req) {
		if (req == null) {
			LOGGER.error("queryCabinetAlert req is null");
			throw new RuntimeException("queryCabinetAlert req is null");
		}
		PageRequest page = new PageRequest();
		BeanUtils.copyProperties(req, page, Utils.getNullPropertyNames(req));
		Map<String, Object> map = FieldUtil.getFiledMap(req);
		for (String key : map.keySet()) {
			page.addFields(key, map.get(key));
		}
		//page.addFields("pageFlag", "false");
		JSONArray array = JSONArray.parseArray(itemInfo);
		page.addFields("source", AlertCommonConstant.CABINET_COLUMN_SOURCE);
		page.addFields("keyComment", AlertCommonConstant.CABINET_ALERT_TITLE);
		page.addFields("deviceItem", array);
		List<AlertCabinetColumnConfigData> pageResult =  alertCabinetColumnBiz.queryCabinetColumnAlert(page);
		List<AlertCabinetColumnConfigDataDTO> listAlert = Lists.newArrayList();
		for (AlertCabinetColumnConfigData filter : pageResult) {
			AlertCabinetColumnConfigDataDTO filterDTO = new AlertCabinetColumnConfigDataDTO();
			BeanUtils.copyProperties(filter, filterDTO);
			listAlert.add(filterDTO);
		}
		return listAlert;
	}
	
	
	/**
	 * 查询机柜告警
	 */
	@Override
	public PageResponse<Map<String,Object>> queryCabinetAlertPageList(@RequestBody AlertCabinetColumnReq req) {
		if (req == null) {
			LOGGER.error("queryCabinetAlert req is null");
			throw new RuntimeException("queryCabinetAlert req is null");
		}
		PageRequest page = new PageRequest();
		BeanUtils.copyProperties(req, page, Utils.getNullPropertyNames(req));
		Map<String, Object> map = FieldUtil.getFiledMap(req);
		for (String key : map.keySet()) {
			page.addFields(key, map.get(key));
		}
		JSONArray array = JSONArray.parseArray(itemInfo);
		page.addFields("source", AlertCommonConstant.CABINET_COLUMN_SOURCE);
		page.addFields("keyComment", AlertCommonConstant.CABINET_ALERT_TITLE);
		page.addFields("deviceItem", array);
		PageResponse<Map<String,Object>> pageResult =  alertCabinetColumnBiz.queryCabinetAlertPageList(page);
		
		return pageResult;
	}
	
	@Override
	public PageResponse<Map<String,Object>> queryDeviceAlertPageList(@RequestBody AlertCabinetColumnReq req) {
		if (req == null) {
			LOGGER.error("queryDeviceAlertPageList req is null");
			throw new RuntimeException("queryDeviceAlertPageList req is null");
		}
		PageRequest page = new PageRequest();
		BeanUtils.copyProperties(req, page, Utils.getNullPropertyNames(req));
		Map<String, Object> map = FieldUtil.getFiledMap(req);
		for (String key : map.keySet()) {
			page.addFields(key, map.get(key));
		}
		JSONArray array = JSONArray.parseArray(itemInfo);
		page.addFields("deviceItem", array);
		PageResponse<Map<String,Object>> pageResult =  alertCabinetColumnBiz.queryDeviceAlertPageList(page);
		//设置管理ip、联系人等
		List<String> idList = Lists.newArrayList();
		Map<String, Map<String, Object>> dataMap = Maps.newHashMap();
		for(Map<String,Object> m:pageResult.getResult()) {
			String id =  MapUtils.getString(m, "device_id");
			if(StringUtils.isBlank(id)) {
				continue;
			}
			idList.add(id);
			dataMap.put(id, m);
		}
		if(idList.size()>0) {
			Map<String, Object> params = Maps.newHashMap();
			/*
			 * idList.clear(); idList.add("b6d10520d60c443db5e1a56aa3477749");
			 */
			params.put("idList", idList);
			
			Object value = utilsHelper.getCmdbData(params, ConstantsCmdb.ALERT_API_QUERYINSTANCELISTBYIDLIST, "list");
			if (null != value) {
				List<Map<String, Object>> departList = (List<Map<String, Object>>) value;
 				for (Map<String, Object> m : departList) {
					String id = MapUtils.getString(m, "id");
					if(dataMap.containsKey(id)) {
						Map<String,Object> d = dataMap.get(id);
						d.put("serviceIP", MapUtils.getString(m, "ServiceIP"));
						d.put("ipmiIp", MapUtils.getString(m, "ipmi_ip"));
						d.put("ip", MapUtils.getString(m, "ip"));
						d.put("business_concat", MapUtils.getString(m, "business_concat"));
						d.put("business_concat_phone", MapUtils.getString(m, "business_concat_phone"));
					}
				}
				
			}
		}
		
		
		return pageResult;
	}
	
	/**
	 * 查询机柜告警不分页
	 */
	@Override
	public List<Map<String,Object>> queryCabinetAlert(@RequestBody AlertCabinetColumnReq req) {
		if (req == null) {
			LOGGER.error("queryCabinetAlert req is null");
			throw new RuntimeException("queryCabinetAlert req is null");
		}
		PageRequest page = new PageRequest();
		BeanUtils.copyProperties(req, page, Utils.getNullPropertyNames(req));
		Map<String, Object> map = FieldUtil.getFiledMap(req);
		for (String key : map.keySet()) {
			page.addFields(key, map.get(key));
		}
		page.setPageSize(null);
		map.put("cabinetItem", "");//TODO
		map.put("deviceItem", "");//TODO
		List<Map<String,Object>> pageResult =  alertCabinetColumnBiz.queryCabinetAlert(page);
	
		return pageResult;
	}


	@Override
	public String getScheduleConfig(String indexType) {
		if (org.apache.commons.lang.StringUtils.isBlank(indexType)) {
			LOGGER.error("getScheduleConfig indexType is null");
			throw new RuntimeException("getScheduleConfig indexType is null");
		}
		if(indexType.equals("powerItem")) {
			indexType = AlertCommonConstant.CABINET_POWER_ITEM;
		}else if(indexType.equals("cabinetAlert")) {
			indexType = AlertCommonConstant.CABINET_ITEM;
		}else {//cabinetColumnAlert
			indexType = AlertCommonConstant.CABINET_COLUMN_ITEM;
		}
		String info =  alertCabinetColumnBiz.getScheduleConfig(indexType);
		return info;
	}
	
	
	@Override
	public PageResponse<Map<String,Object>> queryBizSystemPageList(@RequestBody AlertCabinetColumnReq req) {
		if (req == null) {
			LOGGER.error(" req is null");
			throw new RuntimeException("queryqueryBizSystemPageListBizSystemPageList req is null");
		}
		
		
		PageRequest page = new PageRequest();
		BeanUtils.copyProperties(req, page, Utils.getNullPropertyNames(req));
		Map<String, Object> map = FieldUtil.getFiledMap(req);
		for (String key : map.keySet()) {
			page.addFields(key, map.get(key));
		}
		JSONArray array = JSONArray.parseArray(itemInfo);
		page.addFields("deviceItem", array);
		
		Map<String,Map<String, Object>> bizMap = Maps.newHashMap();
		if(StringUtils.isNotBlank(req.getBusinessConcat())) {
			Map<String, Object> params = Maps.newHashMap();
			params.put("businessConcat", req.getBusinessConcat());
			Object value = utilsHelper.getCmdbData(params, ConstantsCmdb.ALERT_API_QUERYBIZSYSTEMLISTBYCONCAT, "list");
			if (null != value) {
				List<Map<String, Object>> departList = (List<Map<String, Object>>) value;
				for (Map<String, Object> m : departList) {
					String bizSystem = m.get("bizSystem").toString();
					bizMap.put(bizSystem, m);
				}
				if(bizMap.size()>0) {
					page.addFields("bizSystemList",  bizMap.keySet());
				}
				
			}
		}
		PageResponse<Map<String,Object>> pageResult =  alertCabinetColumnBiz.queryBizSystemAlertPageList(page);
		setBizSystemInfo(pageResult.getResult(),bizMap);
		
		return pageResult;
	}
	
	public void setBizSystemInfo(List<Map<String,Object>> list,Map<String,Map<String,Object>> bizMap){
		if(bizMap.size()>0) {
			for(Map<String,Object> m:list) {
				
				String bizSystem = MapUtils.getString(m, "bizSystem");
				
				if(bizMap.containsKey(bizSystem)) {
					Map<String,Object> mm = bizMap.get(bizSystem);
					m.put("business_concat", MapUtils.getString(mm, "business_concat"));
					m.put("business_concat_phone", MapUtils.getString(mm, "business_concat_phone"));
				}
			}
		}else {
			List<String> bizList = Lists.newArrayList(); 
			for(Map<String,Object> m:list) {
				
				String biz = MapUtils.getString(m, "bizSystem");
				
	   			if(!StringUtils.isEmpty(biz)) {
	   				bizList.add(biz);
	   				bizMap.put(biz, m);
	   			}
			}
			if(!bizList.isEmpty()) {
	   			Map<String, Object> params = Maps.newHashMap();
	   			params.put("bizSystem", bizList);
	   			Object value = utilsHelper.getCmdbData(params, ConstantsCmdb.QUERY_BIZSYSTEM_INFO_BY_NAME, "list");
	   			if(value!=null) {
	   				List<Map<String,Object>> cmdblist = (List<Map<String,Object>>)value;
	   				for(Map<String,Object> m:cmdblist) {
	   					String biz = MapUtils.getString(m, "bizSystem");
	   					if(bizMap.containsKey(biz)) {
	   						String concat = MapUtils.getString(m, "business_concat");
	   	   					String phone = MapUtils.getString(m, "business_concat_phone");
	   	   				    Map<String,Object> valueMap = bizMap.get(biz);
	   	   				    valueMap.put("business_concat", concat);
	   	   				    valueMap.put("business_concat_phone", phone);
	   					}
	   				}
	   			}
	   		}
		}
		
	}
	
	@Override
	public List<Map<String,Object>> queryBizSystemList(@RequestBody AlertCabinetColumnReq req) {
		if (req == null) {
			LOGGER.error("queryBizSystemPageList req is null");
			throw new RuntimeException("queryBizSystemPageList req is null");
		}
		PageRequest page = new PageRequest();
		BeanUtils.copyProperties(req, page, Utils.getNullPropertyNames(req));
		Map<String, Object> map = FieldUtil.getFiledMap(req);
		for (String key : map.keySet()) {
			page.addFields(key, map.get(key));
		}
		JSONArray array = JSONArray.parseArray(itemInfo);
		page.addFields("deviceItem", array);

		Map<String,Map<String, Object>> bizMap = Maps.newHashMap();
		if(StringUtils.isNotBlank(req.getBusinessConcat())) {
			Map<String, Object> params = Maps.newHashMap();
			params.put("businessConcat", req.getBusinessConcat());
			Object value = utilsHelper.getCmdbData(params, ConstantsCmdb.ALERT_API_QUERYBIZSYSTEMLISTBYCONCAT, "list");
			if (null != value) {
				List<Map<String, Object>> departList = (List<Map<String, Object>>) value;
				for (Map<String, Object> m : departList) {
					String bizSystem = m.get("bizSystem").toString();
					bizMap.put(bizSystem, m);
				}
				if(bizMap.size()>0) {
					page.addFields("bizSystemList",  bizMap.keySet());
				}
				
			}
		}
		List<Map<String,Object>> pageResult =  alertCabinetColumnBiz.queryBizSystemAlertList(page);
		
			setBizSystemInfo(pageResult,bizMap);
		
		
		return pageResult;
	}
}
