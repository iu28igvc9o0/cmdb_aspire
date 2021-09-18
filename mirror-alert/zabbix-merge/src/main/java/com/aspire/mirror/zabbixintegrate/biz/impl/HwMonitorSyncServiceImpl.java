package com.aspire.mirror.zabbixintegrate.biz.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.zabbixintegrate.bean.CommonHttpResponse;
import com.aspire.mirror.zabbixintegrate.bean.HwResponse;
import com.aspire.mirror.zabbixintegrate.bean.HwSyncLog;
import com.aspire.mirror.zabbixintegrate.bean.IndicatorInfoHw;
import com.aspire.mirror.zabbixintegrate.bean.InstanceHw;
import com.aspire.mirror.zabbixintegrate.biz.HwMonitorSyncService;
import com.aspire.mirror.zabbixintegrate.config.HWProperties;
import com.aspire.mirror.zabbixintegrate.daoCmdb.AlertRestfulDao;
import com.aspire.mirror.zabbixintegrate.daoCmdb.CmdbInstanceDao;
import com.aspire.mirror.zabbixintegrate.daoCmdb.po.CmdbInstance;
import com.aspire.mirror.zabbixintegrate.util.DateUtil;
import com.aspire.mirror.zabbixintegrate.util.HttpUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HwMonitorSyncServiceImpl implements HwMonitorSyncService {
	private static final Logger LOGGER = LoggerFactory.getLogger(HwMonitorSyncServiceImpl.class);

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	private static final int MONITORSTATUS = 1;
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	
	@Autowired
	private AlertRestfulDao alertRestfulDao;
	
	 @Autowired
	private CmdbInstanceDao cmdbInstanceDao;
		@Autowired
		private HWProperties hWProperties;
	 private String url ;
	 private int tag_type;
	 private String idcTypeTag ;
	 
	 private String tokenHeaderName ;
	 
	 @PostConstruct
	 	private void init(){
			 tag_type = hWProperties.getTag_type();
			 tokenHeaderName = hWProperties.getTokenHeaderName();
			 url = hWProperties.getUrl();
			 idcTypeTag = hWProperties.getIdcType()+"_"+tag_type;
	 	}

	public String getToken() throws Exception {
		// 组装查询条件
		JSONObject ob = new JSONObject();
		ob.put("grantType", "password");
		ob.put("userName", hWProperties.getOc_username());
		ob.put("value",hWProperties.getOc_password());

	
		HwSyncLog syncLog = new HwSyncLog();// 记录日志
		// 查询监控对象类型
		String urlCur = url + hWProperties.getToken_url();
		formLog(syncLog, urlCur, "token");// 设置日志信息
		log.info("获取华为用户认证：{},url:{}", ob,urlCur);
		CommonHttpResponse rs = HttpUtils.httpPut(ob.toJSONString(), urlCur, null, null);
		int status = rs.getStatus();
		String content = rs.getContent();
		if (!rs.isResponsedNew()) {// 重复调用一次
			rs = HttpUtils.httpPut(ob.toJSONString(), urlCur, null, null);
			status = rs.getStatus();
			content = rs.getContent();
		}

		syncLog.setContent(content);
		syncLog.setStatusCode(status);
		if (!rs.isResponsedNew()) {// 判断没有返回
			syncLog.setStatusFail();
		}

		if (status != 200) {
			log.error("华为认证用户失败:{},content:{}", ob.toJSONString(), syncLog.getContent());
			syncLog.setCreateTime(new Date());
			alertRestfulDao.insertHwSyncLog(syncLog);
			throw new Exception("华为认证用户失败:" + content);
		}
		JSONObject obToken = JSONObject.parseObject(content);
		String token = obToken.getString("accessSession");
		log.info("获取华为用户认证token：{}", token);
		return token;

	}

	private void formLog(HwSyncLog syncLog, String url, String config_type) {
		syncLog.setUrl(url);
		syncLog.setConfigType(config_type);
		syncLog.setIdcTypeTag(this.idcTypeTag);
		syncLog.setExecTime(new Date());
	}

	/**
	 * 2.2获取所有监控对象类型
	 * 
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public JSONArray getMonitorTypeData(String token) throws Exception {
		HwSyncLog syncLog = new HwSyncLog();// 记录日志
		// 查询监控对象类型
		String urlCur = url +hWProperties.getMonitor_type_url();
		formLog(syncLog, urlCur, "monitorType");// 设置日志信息
		log.info("华为获取所有监控对象类型url:{}", urlCur);
		CommonHttpResponse rs = HttpUtils.httpGet(urlCur, token, this.tokenHeaderName);
		String content = rs.getContent();
		if (!rs.isResponsedNew()) {// 重复调用一次
			rs = HttpUtils.httpGet(urlCur, token, this.tokenHeaderName);
			content = rs.getContent();
		}
		/*
		 * if (!rs.isResponsedNew()) {// 判断没有返回 syncLog.setStatusFail();
		 * syncLog.setContent(content); }
		 */
		HwResponse ob = JSON.parseObject(content, HwResponse.class);
		if (rs.getStatus() != 200) {// 失败记录日志，跑出异常
			if(!rs.isResponsedNew()) {
				log.error("华为获取所有监控对象类型失败:content:{}", rs.getContent());
				syncLog.setStatusFail();
				syncLog.setStatusCode(rs.getStatus());
				syncLog.setContent(rs.getContent());
				
			}else {
				log.error("华为获取所有监控对象类型失败:content:{}", ob.getError_msg());
				syncLog.setStatusCode(ob.getError_code());
				syncLog.setContent(ob.getError_msg());
				syncLog.setCreateTime(new Date());
			}
			alertRestfulDao.insertHwSyncLog(syncLog);
			throw new Exception("华为获取所有监控对象类型失败:" + syncLog.getContent());
		}

		Object data = ob.getData();
		if (null == data) {
			return new JSONArray();
		}
		JSONArray arr = JSON.parseArray(JSON.toJSONString(data));
		return arr;
	}

	/**
	 * 2.3获取监控对象类型支持的监控指标
	 * 
	 * @param token
	 * @param obTemp
	 * @return
	 * @throws Exception
	 */
	public List<Long> getMonitorIndicatorsRelData(String token, JSONObject obTemp) throws Exception {
		HwSyncLog syncLog = new HwSyncLog();// 记录日志
		long objTypeId = obTemp.getLongValue("obj_type_id");

		String indicatorsRel_url = String.format(this.url + hWProperties.getMonitor_indicatorsrel_url(), objTypeId);
		formLog(syncLog, indicatorsRel_url, "monitorIndicatorsRel");// 设置日志信息
		log.info("华为获取监控对象类型的监控指标url:{}", indicatorsRel_url);
		CommonHttpResponse rs2 = HttpUtils.httpGet(indicatorsRel_url, token, this.tokenHeaderName);
		String content2 = rs2.getContent();
		if (!rs2.isResponsedNew()) {// 重复调用一次
			rs2 = HttpUtils.httpGet(indicatorsRel_url, token, this.tokenHeaderName);
			content2 = rs2.getContent();
		}
		/*
		 * if (!rs2.isResponsed()) {// 判断没有返回 syncLog.setStatusFail();
		 * syncLog.setContent(content2); }
		 */

		HwResponse rsOb = JSON.parseObject(content2, HwResponse.class);
		if (rs2.getStatus() != 200) {// 失败记录日志，跑出异常
			
			if(!rs2.isResponsedNew()) {
				log.error("华为获取监控对象类型的监控指标失败:{}", rs2.getContent());
				syncLog.setStatusFail();
				syncLog.setStatusCode(rs2.getStatus());
				syncLog.setContent(rs2.getContent());
				
			}else {
				log.error("华为获取监控对象类型支持的监控指标失败:{}", rsOb.getError_msg());
				syncLog.setStatusCode(rsOb.getError_code());
				syncLog.setContent(rsOb.getError_msg());
				syncLog.setCreateTime(new Date());
			}
			alertRestfulDao.insertHwSyncLog(syncLog);
			
			throw new Exception("华为获取监控对象类型支持的监控指标失败:" + syncLog.getContent());
		}

		JSONObject ob = JSONObject.parseObject(JSON.toJSONString(rsOb.getData()));
		List<Long> ids = ob.getObject("indicator_ids", List.class);
		return ids;
	}

	/**
	 * 获取监控指标描述信息
	 * 
	 * @param token
	 * @param obTemp
	 * @return
	 * @throws Exception
	 */
	public Map<Object, IndicatorInfoHw> getMonitorIndicators(String token, List<Long> ids) throws Exception {

		HwSyncLog syncLog = new HwSyncLog();// 记录日志
		// 查询指标详情
		String urlCur = this.url + hWProperties.getMonitor_indicators_url();
		formLog(syncLog, urlCur, "monitorIndicators");// 设置日志信息
		log.info("华为获取监控指标描述信息url:{}", urlCur);
		CommonHttpResponse rs3 = HttpUtils.httpPost(urlCur, token, JSON.toJSONString(ids), this.tokenHeaderName);

		String content2 = rs3.getContent();
		if (!rs3.isResponsedNew()) {// 重复调用一次
			rs3 = HttpUtils.httpPost(urlCur, token, JSON.toJSONString(ids), this.tokenHeaderName);
			content2 = rs3.getContent();
		}
		

		HwResponse rsOb = JSON.parseObject(content2, HwResponse.class);
		if (rs3.getStatus() != 200) {// 失败记录日志，跑出异常
			if(!rs3.isResponsedNew()) {
				log.error("华为获取监控指标描述信息:content:{}", rs3.getContent());
				syncLog.setStatusFail();
				syncLog.setStatusCode(rs3.getStatus());
				syncLog.setContent(rs3.getContent());
				
			}else {
				log.error("华为获取监控指标描述信息:content:{}", rsOb.getError_msg());
				syncLog.setStatusCode(rsOb.getError_code());
				syncLog.setContent(rsOb.getError_msg());
				syncLog.setCreateTime(new Date());
			}
			alertRestfulDao.insertHwSyncLog(syncLog);
			throw new Exception("华为获取监控指标描述信息:" + syncLog.getContent());
			
			
		}

		Map<Object, IndicatorInfoHw> ob = JSONObject.parseObject(JSON.toJSONString(rsOb.getData()), Map.class);
		return ob;
	}

	/**
	 * 同步指标
	 * 
	 * @param token
	 * @throws Exception
	 */
	@Override
	public void syncMonitorConfigData() throws Exception {
		log.info("同步监控指标数据开始");
		//获取token
		String token = getToken();
		// 获取type
		JSONArray arr = getMonitorTypeData(token);

		// 查询指标
		for (int i = 0; i < arr.size(); i++) {
			JSONObject obTemp = arr.getJSONObject(i);
			long objTypeId = obTemp.getLongValue("obj_type_id");
			String zhCn = obTemp.getString("zh_cn");
			// 获取关联关系
			List<Long> ids = getMonitorIndicatorsRelData(token, obTemp);
			if (null != ids && ids.size() > 0) {
				// 查询指标
				Map<Object, IndicatorInfoHw> dataMap = getMonitorIndicators(token, ids);
				List<IndicatorInfoHw> dataList = Lists.newArrayList();
				Date date = new Date();
				for (Map.Entry<Object, IndicatorInfoHw> entry : dataMap.entrySet()) {
					Long indicatorId = Long.parseLong(entry.getKey().toString());
					IndicatorInfoHw value = JSON.parseObject(JSONObject.toJSONString(entry.getValue()),IndicatorInfoHw.class);
					value.setIndicator_id(indicatorId);
					value.setObj_type_id(objTypeId);
					value.setZh_cn_obj_type(zhCn);
					//value.setMonitor_status(1);
					value.setUpdate_time(date);
					value.setTag_type(this.tag_type);
					dataList.add(value);
				}
				if (dataList.size() > 0) {
					alertRestfulDao.insertIndicatorInfoHw(dataList);
				}
			}
		}

		log.info("同步监控指标数据结束");
	}

	/*
	 * 2.5查询Region列表
	 */
	public JSONObject getRegionDatas(String token, int pageNo) throws Exception {

		HwSyncLog syncLog = new HwSyncLog();// 记录日志
		// 查询指标详情

		String regionUrl = String.format(this.url + hWProperties.getRegion_url(), hWProperties.getRegionClassName());
		formLog(syncLog, regionUrl, "region");// 设置日志信息
		String contentSelector = "[\"locales\",\"nativeId\",\"resId\"]";
		regionUrl = regionUrl + "?pageNo=" + pageNo + "&pageSize=" + hWProperties.getRegionPageSize()
				+ "&contentSelector="+contentSelector;
		log.info("华查询Region列表url:{}", regionUrl);
		CommonHttpResponse rs = HttpUtils.httpGet(regionUrl, token, this.tokenHeaderName);

		String content2 = rs.getContent();
		if (!rs.isResponsedNew()) {// 重复调用一次
			rs = HttpUtils.httpGet(regionUrl, token, this.tokenHeaderName);
			content2 = rs.getContent();
		}
		

		if (rs.getStatus() != 200) {// 失败记录日志，跑出异常
			log.error("华查询Region列表失败:content:{}", content2);
			syncLog.setStatusFail();
			syncLog.setStatusCode(rs.getStatus());
			syncLog.setContent(content2);
			syncLog.setCreateTime(new Date());
			alertRestfulDao.insertHwSyncLog(syncLog);
			throw new Exception("华查询Region列表失败:" + syncLog.getContent());
		}
		JSONObject ob = JSON.parseObject(content2);
		return ob;
	}

	/**
	 * 查询虚拟机列表
	 * 
	 * @param token
	 * @param pageNo
	 * @param region
	 * @return
	 * @throws Exception
	 */
	public JSONObject getDeviceDatas(String token, int pageNo, JSONObject region) throws Exception {
		log.info("查询集群下设备数据开始");
		HwSyncLog syncLog = new HwSyncLog();// 记录日志
		// 查询指标详情

		String nativeId = region.getString("nativeId");
		String deviceUrl = String.format(this.url + hWProperties.getDevice_url(), hWProperties.getDeviceClassName());
		formLog(syncLog, deviceUrl, "device");// 设置日志信息
		deviceUrl = deviceUrl + "?pageNo="+pageNo+"&pageSize=" + hWProperties.getDevicePageSize()
				+ "&contentSelector=[\"id\",\"name\",\"nativeId\",\"hostId\",\"privateIps\",\"bizRegionId\",\"createdAt\",\"extraSpecs\",\"status\"] "
				+ "&condition={\"constraint\":[{\"simple\":{\"name\":\"bizRegionNativeId\",\"value\":\"" + nativeId
				+ "\"}}]}";
		log.info("查询集群下设备数据URL:{}", deviceUrl);
		CommonHttpResponse rs = HttpUtils.httpGet(deviceUrl, token, this.tokenHeaderName);

		String content2 = rs.getContent();
		if (!rs.isResponsedNew()) {// 重复调用一次
			rs = HttpUtils.httpGet(deviceUrl, token, this.tokenHeaderName);
			content2 = rs.getContent();
		}
		

		if (rs.getStatus() != 200) {// 失败记录日志，跑出异常
			log.error("华为查询虚拟机列表失败:region:{},content:{}", region, content2);
			syncLog.setStatusFail();
			syncLog.setStatusCode(rs.getStatus());
			syncLog.setContent(content2);
			syncLog.setCreateTime(new Date());
			alertRestfulDao.insertHwSyncLog(syncLog);
			
			throw new Exception("华为查询虚拟机列表失败:" + syncLog.getContent());
		}
		JSONObject ob = JSON.parseObject(content2);
		return ob;
	}

	/**
	 * 同步设备数据
	 * 
	 * @param token
	 * @throws Exception
	 */
	@Override
	public void syncDeviceData() throws Exception {
		log.info("同步集群和设备数据开始");
		//获取token
		String token = getToken();
		// 查询region
		JSONObject ob = getRegionDatas(token, 1);
		JSONArray arr = ob.getJSONArray("objList");
		if (null != arr) {

			for (int i = 0; i < arr.size(); i++) {
				JSONObject region = arr.getJSONObject(i);
				// 入库Region下的设备
				DeviceThread thread = new DeviceThread(region, token);
				taskExecutor.execute(thread);
				// handleDeviceData(region,token);
			}

		}
		// 处理region分页
		int totalPageNo = ob.getIntValue("totalPageNo");
		log.info("regionData:{}",ob);
		if (totalPageNo > 1) {
			for (int kk = 2; kk <= totalPageNo; kk++) {
				JSONObject ob2 = getRegionDatas(token, kk);
				JSONArray arr2 = ob2.getJSONArray("objList");
				if (null != arr2) {
					for (int i = 0; i < arr2.size(); i++) {
						JSONObject region = arr2.getJSONObject(i);
						DeviceThread thread = new DeviceThread(region, token);
						taskExecutor.execute(thread);
						// handleDeviceData(region,token);
					}

				}
			}
		}
		log.info("同步集群和设备数据结束");

	}

	public class DeviceThread implements Runnable {
		private JSONObject region;
		private String token;

		public DeviceThread(JSONObject region, String token) {
			this.region = region;
			this.token = token;
		}

		public void run() {
			HwSyncLog syncLog = new HwSyncLog();// 记录日志
			try {
				formLog(syncLog, null, "syncDeviceData");// 设置日志信息
				handleDeviceData(region, token);
			} catch (Exception e) {
				log.error("处理设备数据报错", e);
				syncLog.setStatusFail();
				String message = e.getMessage()==null?"":e.getMessage().toString();
				syncLog.setContent(e.getClass().getName() + ":" + message);
			}finally {
				alertRestfulDao.insertHwSyncLog(syncLog);
			}
		}

	}

	// 处理设备分页数据
	public void handleDeviceData(JSONObject region, String token) throws Exception {
		log.info("处理设备数据开始：{}", region);
		Date date = new Date();
		JSONObject ob2 = getDeviceDatas(token, 1, region);
		JSONArray arr2 = ob2.getJSONArray("objList");
		List<String> idAllList = Lists.newArrayList();
		handleDevicePageData( region,arr2,date,idAllList);
		
		// 分页数
		int totalPageNo = ob2.getIntValue("totalPageNo");
		log.info("设备返回：{},totalPageNo:{}", arr2.size(),totalPageNo);
		if (totalPageNo > 1) {
			for (int kk = 2; kk <= totalPageNo; kk++) {
				JSONObject obTemp = getDeviceDatas(token, kk, region);
				JSONArray arrTemp = obTemp.getJSONArray("objList");
				handleDevicePageData(region,arrTemp,date,idAllList);
			}
		}
		idAllList.clear();
		log.info("处理设备数据结束：{}", region);
	}
	
	public void handleDevicePageData(JSONObject region,JSONArray arr2,Date date, List<String> idAllList){
		Map<String,InstanceHw> idMap = Maps.newHashMap();
		 List<InstanceHw> inertList = Lists.newArrayList();
		 List<String> idList = Lists.newArrayList();
		for (int k = 0; k < arr2.size(); k++) {
			InstanceHw device = arr2.getObject(k, InstanceHw.class);
			device.setUpdate_time(date);
			//device.setMonitor_status(1);
			device.setTag_type(tag_type);
			device.setLocales(region.getString("locales"));
			device.setRegionId(region.getString("nativeId"));
			String id = device.getId();
			if(idAllList.contains(id)) {
				continue;
			}
			idAllList.add(id);
			idList.add(id);
			idMap.put(id, device);
			inertList.add(device);
		}
		if (inertList.size() > 0) {
			List<CmdbInstance> dsList = cmdbInstanceDao.getDeviceInfo(idList);
			for(CmdbInstance info:dsList) {
				String id = info.getInstanceId();
				if(idMap.containsKey(id)) {
					idMap.get(id).setInstance_info(JSONObject.toJSONString(info));
					idMap.get(id).setIdcType(info.getIdcType());
				}
			}
			alertRestfulDao.insertInstanceHw(inertList);
			inertList.clear();
			idMap.clear();
			idList.clear();
		}
	}

	/**
	 * 查监控性能数据
	 * 
	 * @param token
	 * @param ob
	 * @return
	 * @throws Exception
	 */
	public HwResponse getMonitorDatas(String token, JSONObject ob) throws Exception {

		HwSyncLog syncLog = new HwSyncLog();// 记录日志
		// 查询指标详情
		String urlCur = this.url + hWProperties.getMonitor_data_url();
		formLog(syncLog, urlCur, "monitorDatas");// 设置日志信息
		log.info("华为查询指定时间范围的性能数据url:{}",urlCur);
		log.debug("华为查询指定时间范围的性能数据data:{}",ob);
		CommonHttpResponse rs3 = HttpUtils.httpPost(urlCur, token, ob.toJSONString(), this.tokenHeaderName);

		String content2 = rs3.getContent();
		if (!rs3.isResponsedNew()) {// 重复调用一次
			rs3 = HttpUtils.httpPost(urlCur, token, ob.toJSONString(), this.tokenHeaderName);
			content2 = rs3.getContent();
		}
		if (!rs3.isResponsed()) {// 判断没有返回
			syncLog.setStatusFail();
			syncLog.setContent(content2);
		}

		HwResponse rsOb = JSON.parseObject(content2, HwResponse.class);
		//log.info("MonitorDataThread:{}",rsOb);
		if (rs3.getStatus() != 200) {// 失败记录日志，跑出异常
			if(!rs3.isResponsedNew()) {
				log.error("华为查询指定时间范围的性能数据失败:{},content:{}", ob.toJSONString(), content2);
				syncLog.setStatusFail();
				syncLog.setStatusCode(rs3.getStatus());
				syncLog.setContent(rs3.getContent());
				
			}else {
				log.error("华为查询指定时间范围的性能数据失败:{},content:{}", ob.toJSONString(), rsOb.getError_msg());
				syncLog.setStatusCode(rsOb.getError_code());
				syncLog.setContent(rsOb.getError_msg());
				syncLog.setCreateTime(new Date());
			}
			alertRestfulDao.insertHwSyncLog(syncLog);
			throw new Exception("华为查询指定时间范围的性能数据失败:" + syncLog.getContent());
			
		}
		/*
		 * String dataStr = JSON.toJSONString(rsOb.getData()); if
		 * (StringUtils.isBlank(dataStr)) { return null; } Map<String, Map<String,
		 * JSONObject>> monitorOb =
		 * JSONObject.parseObject(JSON.toJSONString(rsOb.getData()), Map.class);
		 */
	
		return rsOb;
	}

	
	/**
	 * 同步性能数据
	 * 
	 * @param token
	 * @throws Exception
	 */
	@Override
	public void syncMonitorDatas(Date startTime,Date endTime) throws Exception {
		log.info("同步华为监控性能数据开始");
		//获取token
		String token = getToken();
				
		JSONObject ob = new JSONObject();
		
	
		ob.put("begin_time", startTime.getTime() + "");
		ob.put("end_time", endTime.getTime() + "");
		ob.put("interval", hWProperties.getInterval());
		ob.put("range", "BEGIN_END_TIME");
		

		Map<String, Object> params = Maps.newHashMap();
		params.put("tagType", this.tag_type);
		params.put("monitorStatus", MONITORSTATUS);
		// 查询监控对象模型
		List<String> objTypeIdList = alertRestfulDao.getIndicatorObjTypeIdList(params);
		int  deviceCount = hWProperties.getDeviceCount();
		for (String objTypeId : objTypeIdList) {
			params.put("objTypeId", objTypeId);
			// 查询监控指标
			List<IndicatorInfoHw> indicatorList = alertRestfulDao.getIndicatorList(params);

			int count = indicatorList.size();
			ob.put("obj_type_id", objTypeId);// 设置监控对象类型id

			for (int i = 0; i < count; i += hWProperties.getIndicatorCount()) {
				List<String> indicators = Lists.newArrayList();
				Map<String, IndicatorInfoHw> IndicatorMap = Maps.newHashMap();
				for (int ii = i; ii < i + hWProperties.getIndicatorCount() && ii<count; ii++) {
					IndicatorInfoHw v = indicatorList.get(ii);
					indicators.add(v.getIndicator_id() + "");
					IndicatorMap.put(v.getIndicator_id() + "", v);
				}
				ob.put("indicator_ids", indicators);// 设备指标项id
				Map<String, Object> instanceParam = Maps.newHashMap();
				instanceParam.put("tagType", this.tag_type);
				instanceParam.put("monitorStatus", this.MONITORSTATUS);
				instanceParam.put("idcTypeList", hWProperties.getIdcType().split(","));
				instanceParam.put("pageSize", deviceCount);
				instanceParam.put("status", "active");
				//查询设备
				int isCount = alertRestfulDao.getInstanceHwPageListCount(instanceParam);
				int getTotalPage = getTotalPage(isCount, deviceCount);
				for (int k = 0; k < getTotalPage; k++) {
					int begin = k * deviceCount;
					instanceParam.put("begin", begin);
					// 查询设备数据
					List<InstanceHw> instanceList = alertRestfulDao.getInstanceHwPageList(instanceParam);
					List<String> instanceIdList = Lists.newArrayList();
					Map<String, InstanceHw> deviceMap = Maps.newHashMap();
					for (InstanceHw is : instanceList) {
						instanceIdList.add(is.getId());
						deviceMap.put(is.getId(), is);
					}
					ob.put("obj_ids", instanceIdList);
					JSONObject obNew = JSONObject.parseObject(ob.toJSONString());
					
					MonitorDataThread thread = new MonitorDataThread(obNew,deviceMap,IndicatorMap, token);
					taskExecutor.execute(thread);
					
				}

			}

		}
		
		log.info("同步集群和设备数据结束");

	}
	
	public class MonitorDataThread implements Runnable {
		private JSONObject ob;
		private Map<String, InstanceHw> deviceMap;
		private Map<String, IndicatorInfoHw> IndicatorMap;
		private String token;

		public MonitorDataThread(JSONObject ob,Map<String, InstanceHw> deviceMap
				,Map<String, IndicatorInfoHw> IndicatorInfoMap,String token) {
			this.ob = ob;
			this.deviceMap = deviceMap;
			this.IndicatorMap = IndicatorInfoMap;
			this.token = token;
		}

		public void run() {
			HwSyncLog syncLog = new HwSyncLog();// 记录日志
			try {
				formLog(syncLog, null, "syncMonitorDatas");// 设置日志信息
				syncLog.setFromTime(DateUtil.format(ob.getDate("begin_time"), DateUtil.DEFAULT_DATETIME_FMT));
				syncLog.setToTime(DateUtil.format(ob.getDate("end_time"), DateUtil.DEFAULT_DATETIME_FMT));
				HwResponse rsOb = getMonitorDatas(token, ob);
				Map<String, Map<String, JSONObject>> map = JSONObject.parseObject(JSON.toJSONString(rsOb.getData()),
						Map.class);
				syncLog.setContent(String.format("%s:code:%s:msg:%s", hWProperties.getIdcType(),rsOb.getError_code(),rsOb.getError_msg()));
				if (null != map && map.size()>0) {
					handleMonitorData( map,  deviceMap,IndicatorMap);
				}
				
				//syncLog.setContent(content);%s
			} catch (Exception e) {
				log.error("处理设备数据报错", e);
				syncLog.setStatusFail();
				String message = e.getMessage()==null?"":e.getMessage().toString();
				syncLog.setContent(e.getClass().getName() + ":" + message);
				
			}finally {
				alertRestfulDao.insertHwSyncLog(syncLog);
			}
		}

	}

	// 处理性能数据
	public void handleMonitorData(Map<String, Map<String, JSONObject>> map, Map<String, InstanceHw> deviceMap,
			Map<String, IndicatorInfoHw> IndicatorInfoMap) {
		// 组装es数据，发送kafka
		for (Map.Entry<String, Map<String, JSONObject>> en : map.entrySet()) {
			String id = en.getKey();
			InstanceHw device = deviceMap.get(id);
			CmdbInstance instance = null;
			String instanceStr = device.getInstance_info();
			if (StringUtils.isNotBlank(instanceStr)) {
				instance = JSONObject.parseObject(instanceStr, CmdbInstance.class);
			}
			Map<String, JSONObject> indicatorMap = en.getValue();
			for (Map.Entry<String, JSONObject> ca : indicatorMap.entrySet()) {
				String indicatorId = ca.getKey();
				IndicatorInfoHw indicator = IndicatorInfoMap.get(indicatorId);
				Map<String, Object> es = Maps.newHashMap();
				if (null != instance) {
					es.put("host", instance.getIp());
					es.put("resourceId", instance.getInstanceId());
					es.put("bizSystem", instance.getBizSystem());
					es.put("department1", instance.getDepartment1());
					es.put("department2", instance.getDepartment2());
					es.put("deviceClass", instance.getDeviceClass());
					es.put("deviceType", instance.getDeviceType());

					es.put("idcType", instance.getIdcType());
					es.put("roomId", instance.getRoomId());
					es.put("podName", instance.getPodName());
					es.put("nodeType", instance.getNodeType());
				} else {
					es.put("idcType", device.getIdcType());
					try {
						String locales = device.getLocales();
						JSONObject l = JSONObject.parseObject(locales);
						//int index = l.getString("zh_cn").indexOf("-");
						es.put("podName", l.getString("zh_cn"));
						es.put("host", device.getPrivateIps().split("@")[1]);
					} catch (Exception e) {
						es.put("host", device.getPrivateIps());
						log.error("华为privateIps数据有误:id:{},ip:{}", id, device.getPrivateIps());
					}
				}

				es.put("item", indicator.getItem());
				es.put("suyanUuid", id);
				es.put("device_source", hWProperties.getDevice_source());// TODO
				String dataType = indicator.getData_type();
				String historyType = indicator.getHistory_type();
				JSONObject ob = ca.getValue();
				List<JSONObject> data = JSONObject.parseObject(ob.getString("series"), List.class);
				for (JSONObject obb : data) {
					Map<String, Object> esTemp = Maps.newHashMap();
					esTemp.putAll(es);
					Set<String> keyset = obb.keySet();
					if(keyset.size()==0) {
						continue;
					}
					boolean flag = false;
					for (String vv : keyset) {
						flag = true;
						long time = Long.parseLong(vv);
						String value = obb.getString(vv);
						String operator = indicator.getOperator();
						String operatorValue = indicator.getOperator_value();
						if(StringUtils.isNotBlank(operator) && StringUtils.isNotBlank(operatorValue) ) {
							Double v = Double.parseDouble(value);
							Long v1 = Long.parseLong(operatorValue);
							if (operator.equals("/")) {
								v = v / v1;
							} else {
								v = v * v1;
							}
							/*
							 * if(!historyType.equals("history")) { value = Math.round(v) +""; }else { value
							 * = v+""; }
							 */
							value = v+"";
						}
						esTemp.put("value", value);
						esTemp.put("datetime", new Date(time).toInstant().toString());
						esTemp.put("clock", time / 1000);
					}
					if(StringUtils.isNotBlank(historyType)) {
						if (historyType.equalsIgnoreCase("history")) {// TODO
							kafkaTemplate.send(hWProperties.getToKafkaTopic(), JSONObject.toJSONString(esTemp));
						} else {
							kafkaTemplate.send(hWProperties.getToKafkaTopic_uint(), JSONObject.toJSONString(esTemp));
						}
					}else {
						if (dataType.equalsIgnoreCase("float") || dataType.equalsIgnoreCase("double")) {// TODO
							kafkaTemplate.send(hWProperties.getToKafkaTopic(), JSONObject.toJSONString(esTemp));
						} else {
							kafkaTemplate.send(hWProperties.getToKafkaTopic_uint(), JSONObject.toJSONString(esTemp));
						}
					}
					if(flag) {//只取一个值
						break;
					}
				}
				

			}
		}
	}

	// 计算总分页数
	public int getTotalPage(int total, int size) {
		int totalPages = total / size;
		if (total % size != 0) {
			totalPages++;
		}
		return totalPages;
	}
}
