package com.aspire.mirror.alert.server.controller.third;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.aspire.mirror.alert.api.dto.monthReport.AlertsLevelCountsResponse;
import com.aspire.mirror.alert.api.dto.third.AlertThirdRequest;
import com.aspire.mirror.alert.api.dto.third.AlertsCountDTO;
import com.aspire.mirror.alert.api.dto.third.AlertsDeviceCountsDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.aspire.mirror.alert.api.service.third.AlertMonitorService;
import com.aspire.mirror.alert.server.clientservice.MonthDayReportNewServiceClient;
import com.aspire.mirror.alert.server.clientservice.ZabbixRatioServiceClient;
import com.aspire.mirror.alert.server.clientservice.elasticsearch.EsIndexPageServiceClient;
import com.aspire.mirror.alert.server.clientservice.elasticsearch.ResourceRateScreenServiceClient;
import com.aspire.mirror.alert.server.constant.ConstantsCmdb;
import com.aspire.mirror.alert.server.dao.monthReport.AlertMonthReportSyncDao;
import com.aspire.mirror.alert.server.dao.alert.AlertsIndexPageDao;
import com.aspire.mirror.alert.server.dao.monthReport.AlertsPoorEfficiencyDeviceDao;
import com.aspire.mirror.alert.server.helper.UtilsHelper;
import com.aspire.mirror.alert.server.util.DateUtils;
import com.aspire.mirror.alert.server.util.Utils;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.elasticsearch.api.dto.IdcTypePhysicalReq;
import com.aspire.mirror.elasticsearch.api.dto.MonthReportRequest;
import com.aspire.mirror.elasticsearch.api.dto.TenantRateRequest;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class ThirdAlertMonitorController implements AlertMonitorService {

	@Autowired
	private AlertsIndexPageDao alertsIndexPageDao;

	@Autowired
	private AlertMonthReportSyncDao alertMonthReportSyncDao;

	@Autowired
	private ZabbixRatioServiceClient zabbixRatioServiceClient;

	@Autowired
	private UtilsHelper utilsHelper;

	@Autowired
	private EsIndexPageServiceClient esIndexPageServiceClient;

	@Value("${cmdbQueryType:bpm_department_index_bizSystem_instance_stats}")
	private String cmdbQueryName;

	@Value("${cmdbQueryType.department_by_idc:alert_query_bizsystem_department_by_idc}")
	private String cmdbQueryNameIdc;

	/*
	 * @Value("${IT_COMPANY_INTERNAL:信息技术中心}") private String IT_COMPANY_INTERNAL;
	 */

	@Autowired
	private MonthDayReportNewServiceClient monthDayReportNewServiceClient;
	
	@Autowired
	private AlertsPoorEfficiencyDeviceDao alertsPoorEfficiencyDeviceDao;
	
	@Autowired
	private ResourceRateScreenServiceClient resourceRateScreenServiceClient;

	@Override
	public AlertsCountDTO idcTypeAlertCount(@RequestParam(value = "idc_type", required = false) String idcType) {
		/*
		 * final CloseableHttpClient httpClient = HttpClients.createDefault();
		 * 
		 * Calendar calendar = Calendar.getInstance(); calendar.setTime(new Date());
		 * DateFormat returndf = new SimpleDateFormat("yyyyMMdd");
		 * 
		 * for(int i=0;i<31;i++) { calendar.add(Calendar.DATE, -1); String t =
		 * returndf.format(calendar.getTime()); HttpResponse httpResponse = null; final
		 * HttpPost httpPost = new
		 * HttpPost("http://10.12.70.40:9200/history-"+t+"/history");
		 * httpPost.setHeader("Content-Type", "application/json"); String ss
		 * ="{\"itemid\": 28846," + "    \"deviceClass\": \"服务器\"," +
		 * "    \"idcType\": \"哈尔滨资源池\"," + "    \"@version\": \"1\"," +
		 * "    \"item\": \"icmppingloss[,30,500,,]\"," +
		 * "    \"host\": \"10.12.70.40\"," + "    \"hostid\": 10263," +
		 * "    \"type\": \"history\"," + "    \"roomId\": null," +
		 * "    \"clock\": 1577807164," + "    \"value\": 0," +
		 * "    \"bizSystem\": \"统一门户\"," +
		 * "    \"@timestamp\": \"2019-12-31T15:45:01.694Z\"," +
		 * "    \"deviceType\": \"云主机\"" + "  }"; final StringEntity entity = new
		 * StringEntity(JSON.toJSONString(ss), "UTF-8"); httpPost.setEntity(entity); try
		 * { httpResponse = httpClient.execute(httpPost); } catch
		 * (ClientProtocolException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); } }
		 */

		int count = alertsIndexPageDao.getAlertsCount(idcType);
		int countHis = alertsIndexPageDao.getAlertsHisCount(idcType);
		AlertsCountDTO c = new AlertsCountDTO();
		c.setResolved(countHis);
		c.setToBeResolved(count);
		return c;
	}

	@Override
	public AlertsLevelCountsResponse toBeResolvedLevalCounts(
			@RequestParam(value = "idc_type", required = false) String idcType) {
		List<Map<String, Object>> levelCount = alertsIndexPageDao.getLevelCounts(idcType);
		AlertsLevelCountsResponse level = new AlertsLevelCountsResponse();
		for (Map<String, Object> map : levelCount) {
			String alertLevel = map.get("alert_level").toString();
			int count = Integer.parseInt(map.get("count").toString());
			setLevel(map, level, alertLevel, count);
		}
		return level;
	}

	public void setLevel(Map<String, Object> map, AlertsLevelCountsResponse level, String alertLevel, int count) {
		if (alertLevel.equals("4")) {
			level.setHigh(count);
		} else if (alertLevel.equals("3")) {
			level.setMedium(count);
		} else if (alertLevel.equals("2")) {
			level.setLow(count);
		} else/* (map.get("alert_level").equals("5")) */ {
			level.setSerious(count);
		}
	}

	@Override
	public AlertsDeviceCountsDTO deviceLevelCount() throws Exception {
		List<String> deviceList = Lists.newArrayList();

		deviceList.add("网络设备");
		deviceList.add("安全设备");
		deviceList.add("存储设备");
		List<Map<String, Object>> list = alertsIndexPageDao.getDeviceLevelCounts(deviceList);
		deviceList.clear();
		deviceList.add("X86服务器");
		deviceList.add("云主机");
		List<Map<String, Object>> list1 = alertsIndexPageDao.getDeviceTypeLevelCounts(deviceList);
		list.addAll(list1);
		// Map<String,AlertsLevelCountsResponse>
		AlertsDeviceCountsDTO rs = new AlertsDeviceCountsDTO();
		for (Map<String, Object> map : list) {
			String deviceClass = map.get("device").toString();
			String alertLevel = map.get("alert_level").toString();
			int num = Integer.parseInt(map.get("num").toString());
			if (deviceClass.equals("X86服务器")) {
				AlertsLevelCountsResponse level = rs.getX86();
				if (null == level) {
					level = new AlertsLevelCountsResponse();
					rs.setX86(level);
				}
				setLevel(map, level, alertLevel, num);
			} else if (deviceClass.equals("云主机")) {
				AlertsLevelCountsResponse level = rs.getCloud();
				if (null == level) {
					level = new AlertsLevelCountsResponse();
					rs.setCloud(level);
				}
				setLevel(map, level, alertLevel, num);
			} else if (deviceClass.equals("网络设备")) {
				AlertsLevelCountsResponse level = rs.getNetwork();
				if (null == level) {
					level = new AlertsLevelCountsResponse();
					rs.setNetwork(level);
				}
				setLevel(map, level, alertLevel, num);
			} else if (deviceClass.equals("安全设备")) {
				AlertsLevelCountsResponse level = rs.getSafety();
				if (null == level) {
					level = new AlertsLevelCountsResponse();
					rs.setSafety(level);
				}
				setLevel(map, level, alertLevel, num);
			} else {
				AlertsLevelCountsResponse level = rs.getStorage();
				if (null == level) {
					level = new AlertsLevelCountsResponse();
					rs.setStorage(level);
				}
				setLevel(map, level, alertLevel, num);
			}
		}
		return rs;
	}

	@Override
	public List<Map<String, String>> department1Rate() throws Exception {
		MonthReportRequest monthReportRequest = new MonthReportRequest();
		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.DATE, -7);

		Date startTime = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		monthReportRequest.setStartTime(sdf.format(startTime));
		monthReportRequest.setEndTime(sdf.format(now));
		monthReportRequest.setTopn(10);
		monthReportRequest.setItem("cpu");
		Map<String, Map<String, Object>> map = zabbixRatioServiceClient.getDepartmentRatioData(monthReportRequest);
		monthReportRequest.setDataMap(map);
		monthReportRequest.setItem("mem");
		List<String> list = new ArrayList(map.keySet());
		monthReportRequest.setDepartments(list);
		map = zabbixRatioServiceClient.getDepartmentRatioData(monthReportRequest);
		return new ArrayList(map.values());
	}

	void formIdcData(Map<String, List<Map<String, Object>>> idcMap, Map<String, Object> d) {
		List<Map<String, Object>> listRt = Lists.newArrayList();
		Map<String, Object> cpuMap = Maps.newHashMap();
		String key = d.get("idcType").toString();
		cpuMap.put("title", "CPU");
		cpuMap.put("avgPeak", d.get("cpu_max"));
		cpuMap.put("avg", d.get("cpu_avg"));

		Map<String, Object> memMap = Maps.newHashMap();
		memMap.put("title", "memory");
		memMap.put("avgPeak", d.get("memory_max"));
		memMap.put("avg", d.get("memory_avg"));

		/*
		 * Map<String,Object> storeMap = Maps.newHashMap(); storeMap.put("title", "存储");
		 * storeMap.put("avgPeak", null); storeMap.put("avg", null);
		 */
		listRt.add(cpuMap);
		listRt.add(memMap);
		// listRt.add(storeMap);
		idcMap.put(key, listRt);
	}

	// 直真接口
	/**
	 * p1.1:4资源池设备数量统计
	 */
	@Override
	public Map<String, List<Map<String, Object>>> idcTypeUserRate() throws ParseException {
		Map<String, Map<String, Object>> dataMap = Maps.newHashMap();
		MonthReportRequest request = new MonthReportRequest();
		request.setDataMap(dataMap);
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String startDate = DateUtils.getPastDate(date, -1, sdf);
		// String dataEnd = dateList.get(i+4);
		request.setStartTime(startDate + " 00:00:00");
		request.setEndTime(startDate + " 23:59:59");

		request.setItem("cpu");
		request.setType(1);

		dataMap = monthDayReportNewServiceClient.queryIdcTypeByMonth(request);
		request.setItem("memory");
		request.setDataMap(dataMap);

		dataMap = monthDayReportNewServiceClient.queryIdcTypeByMonth(request);

		Map<String, Object> allMemory = monthDayReportNewServiceClient.queryIdcTypeAllByMonth(request);
		request.setItem("cpu");
		Map<String, Object> allCpu = monthDayReportNewServiceClient.queryIdcTypeAllByMonth(request);
		allCpu.putAll(allMemory);

		// allCpu.put("day", date);
		Map<String, List<Map<String, Object>>> idcMap = Maps.newHashMap();
		if (!allCpu.isEmpty()) {
			allCpu.put("idcType", "总览");
			formIdcData(idcMap, allCpu);
		}

		for (Map.Entry<String, Map<String, Object>> m : dataMap.entrySet()) {
			formIdcData(idcMap, m.getValue());
		}
		return idcMap;

	}

	// p2.6资源利用率
	@Override
	public List<Map<String, Object>> bizSystemTopUseRate(
			@RequestParam(value = "idcType", required = false) String idcType) throws ParseException {
		Map<String, Object> params = Maps.newHashMap();
		if (StringUtils.isNotBlank(idcType)) {
			params.put("idcType", idcType);
		}
		params.put("top", 10);
		Object value = utilsHelper.getCmdbData(params, this.cmdbQueryNameIdc, "list");
		List<Map<String, Object>> list = (List<Map<String, Object>>) value;
		List<String> department1List = Lists.newArrayList();
		List<String> department2List = Lists.newArrayList();
		Map<String, Map<String, Object>> map = Maps.newLinkedHashMap();
		for (Map<String, Object> m : list) {
			Map<String, Object> v = Maps.newHashMap();
			String department = m.get("department_name").toString();
			String type = m.get("department_type").toString();
			int number = Integer.parseInt(m.get("number").toString());
			if (type.equals("department1")) {
				department1List.add(department);
			} else {
				department2List.add(department);
			}
			v.put("department", department);
			v.put("count", number);
			map.put(department, v);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String startDate = DateUtils.getPastDate(new Date(), -1, sdf);
		TenantRateRequest raterequest = new TenantRateRequest();
		raterequest.setIdcType(idcType);
		raterequest.setDepartment1List(department1List);
		raterequest.setDepartment2List(department2List);
		raterequest.setStartTime(startDate + " 00:00:00");
		raterequest.setEndTime(startDate + " 23:59:59");
		raterequest.setItem("cpu");
		Map<String, Double> cpu = resourceRateScreenServiceClient.queryDepartmentUseRateData(raterequest);
		raterequest.setItem("memory");
		Map<String, Double> memory = resourceRateScreenServiceClient.queryDepartmentUseRateData(raterequest);
		for (Map.Entry<String, Map<String, Object>> en : map.entrySet()) {
			String department = en.getKey();
			Map<String, Object> v = en.getValue();
			if (cpu.containsKey(department)) {
				v.put("cpuMax", cpu.get(department));
			}
			if (memory.containsKey(department)) {
				v.put("memoryMax", memory.get(department));
			}
		}

		return new ArrayList<Map<String, Object>>(map.values());

	}

	private String getDepartmentId(String department1) {
		Map<String, Object> params = Maps.newHashMap();
		if (StringUtils.isNotBlank(department1)) {
			List<String> list = Lists.newArrayList();
			list.add(department1);
			params.put("departmentNames", list);
			params.put("parentId", 0);
		} else {
			return null;
		}
		Object value = utilsHelper.getCmdbData(params, ConstantsCmdb.ALERT_QUERY_DEPARTMENT_ID_BY_NAME, "map");
		if (null == value) {
			return department1;
		}
		Map<String, Object> map = (Map<String, Object>) value;
		return map.get("id") == null ? department1 : map.get("id").toString();
	}

	// p1.2:4低利用率应用系统
	@Override
	public List<Map<String, Object>> bizSystemLowUseRate(@RequestBody AlertThirdRequest params) {
		Map<String, Object> request = Maps.newHashMap();
		if(StringUtils.isNotBlank(params.getDepartmentType())) {
			if(params.getDepartmentType().equals("department1")) {
				request.put("department1", params.getDepartment());
			}else {
				request.put("department2", params.getDepartment());
			}
		}
		

		request.put("hisFlag", 0);
		if (StringUtils.isNotBlank(params.getIdcType())) {
			request.put("idcType", params.getIdcType());
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String day = DateUtils.getPastDate(new Date(), -1, sdf);
		request.put("day", day);

		List<Map<String, Object>> sumList = alertMonthReportSyncDao.querybizSysDayType2CountAll(request);

		List<Map<String, Object>> rs = Lists.newArrayList();
		int phySum = 0;
		int vmSum = 0;
		for (Map<String, Object> m : sumList) {
			String deviceType = m.get("deviceType").toString();
			int count = Integer.parseInt(m.get("count").toString());
			if (deviceType.equals("X86服务器")) {
				phySum = count;
			} else {
				vmSum = count;
			}

		}

		request.put("deviceType", "X86服务器");
		request.put("cpuMax", 30);
		request.put("memoryMax", 30);
		int phyCount = alertMonthReportSyncDao.querybizSysDayType2Count(request);
		Map<String, Object> cpuMap = Maps.newHashMap();
		cpuMap.put("name", "phy");
		// cpuMap.put("desc", "峰值利用率低于30");
		cpuMap.put("count", phyCount);
		if (phySum > 0) {
			cpuMap.put("per", Utils.formatDouble((float) phyCount * 100 / phySum, 2));
		} else {
			cpuMap.put("per", 0);
		}

		request.put("deviceType", "云主机");
		request.remove("memoryMax");
		int vmCpuCount = alertMonthReportSyncDao.querybizSysDayType2Count(request);
		Map<String, Object> vmMemMap = Maps.newHashMap();
		vmMemMap.put("name", "vmMemory");
		// vmMemMap.put("desc", "峰值利用率低于30%");
		vmMemMap.put("count", vmCpuCount);
		if (vmSum > 0) {
			vmMemMap.put("per", Utils.formatDouble((float) vmCpuCount * 100 / vmSum, 2));
		} else {
			vmMemMap.put("per", 0);
		}

		request.remove("cpuMax");
		request.put("memoryMax", 30);
		int vmMemCount = alertMonthReportSyncDao.querybizSysDayType2Count(request);
		Map<String, Object> vCpumMap = Maps.newHashMap();
		vCpumMap.put("name", "vmCpu");
		// vCpumMap.put("desc", "峰值利用率低于30%");
		vCpumMap.put("count", vmMemCount);
		if (vmSum > 0) {
			vCpumMap.put("per", Utils.formatDouble((float) vmMemCount * 100 / vmSum, 2));
		} else {
			vCpumMap.put("per", 0);
		}
		rs.add(vCpumMap);
		rs.add(vmMemMap);
		rs.add(cpuMap);
		return rs;
	}

	// p1.2:4/1低利用率应用系统
	@Override
	public List<Map<String, Object>> bizSystemLowUseRateTopN(@RequestBody AlertThirdRequest params) {
		Map<String, Object> request = Maps.newHashMap();
		//request.put("department1", getDepartmentId(department1));
		if(StringUtils.isNotBlank(params.getDepartmentType())) {
			if(params.getDepartmentType().equals("department1")) {
				request.put("department1", params.getDepartment());
			}else {
				request.put("department2", params.getDepartment());
			}
		}
		
		request.put("hisFlag", 0);
		String orderType = params.getOrderType();
		if (StringUtils.isBlank(orderType)) {
			orderType = "phyCpu";
		}
		if (orderType.equals("phyCpu")) {
			request.put("deviceType", "X86服务器");
			request.put("order", "cpuMax");
		}
		if (orderType.equals("phyMemory")) {
			request.put("deviceType", "X86服务器");
			request.put("order", "memoryMax");
		}
		if (orderType.equals("vmCpu")) {
			request.put("deviceType", "云主机");
			request.put("order", "cpuMax");
		}
		if (orderType.equals("vmMemory")) {
			request.put("deviceType", "云主机");
			request.put("order", "memoryMax");
		}

		if (StringUtils.isNotBlank(params.getIdcType())) {
			request.put("idcType", params.getIdcType());
		}
		Integer size = params.getSize();

		if (null == size) {
			size = 5;
		}
		request.put("size", size);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String day = DateUtils.getPastDate(new Date(), -1, sdf);
		request.put("day", day);

		if (orderType.equals("phyCpu") || orderType.equals("phyMemory")) {
			request.put("cpuMax", 30);
			request.put("memoryMax", 30);
		} else if (orderType.equals("vmMemory")) {
			request.put("memoryMax", 30);
		} else {
			request.put("cpuMax", 30);
		}
		List<Map<String, Object>> dataList = alertMonthReportSyncDao.querybizSysDayType2CountTopN(request);
		if (!CollectionUtils.isEmpty(dataList)) {
			List<String> bizIdList = dataList.stream().map(item -> MapUtils.getString(item, "bizSystem"))
					.collect(Collectors.toList());
			Map<String, String>  map = utilsHelper.getDepartmentDataMapByTye(bizIdList, "bizSystem");
			for(Map<String, Object> m:dataList) {
				String bizSystem =  MapUtils.getString(m, "bizSystem");
				if(map.containsKey(bizSystem)) {
					m.put("bizSystem", map.get(bizSystem));
				}
			}
		}

		return dataList;
	}

	// p1.2:3/1低利用率应用系统
	@Override
	public List<Map<String, Object>> bizSysDayType2TopN(@RequestBody AlertThirdRequest params) {
		Map<String, Object> request = Maps.newHashMap();
		if(StringUtils.isNotBlank(params.getDepartmentType())) {
			if(params.getDepartmentType().equals("department1")) {
				request.put("department1", params.getDepartment());
			}else {
				request.put("department2", params.getDepartment());
			}
		}
		request.put("hisFlag", 2);
		String orderType = params.getOrderType();
		if (StringUtils.isBlank(orderType)) {
			orderType = "cpuMax";
		}
		if (orderType.equals("cpuMax")) {
			request.put("order", "cpuMax");
		}
		if (orderType.equals("memoryMax")) {
			request.put("order", "memoryMax");
		}
		if (orderType.equals("cpuAvg")) {
			request.put("order", "cpuAvg");
		}
		if (orderType.equals("memoryAvg")) {
			request.put("order", "memoryAvg");
		}

		if (StringUtils.isNotBlank(params.getIdcType())) {
			request.put("idcType", params.getIdcType());
		}
		Integer size = params.getSize();

		if (null == size) {
			size = 5;
		}

		request.put("size", size);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String day = DateUtils.getPastDate(new Date(), -1, sdf);
		request.put("day", day);
		List<Map<String, Object>> dataList = alertMonthReportSyncDao.querybizSysDayType2CountTopN(request);
		if (!CollectionUtils.isEmpty(dataList)) {
			List<String> bizIdList = dataList.stream().map(item -> MapUtils.getString(item, "bizSystem"))
					.collect(Collectors.toList());
			Map<String, String>  map = utilsHelper.getDepartmentDataMapByTye(bizIdList, "bizSystem");
			for(Map<String, Object> m:dataList) {
				String bizSystem =  MapUtils.getString(m, "bizSystem");
				if(map.containsKey(bizSystem)) {
					m.put("bizSystem", map.get(bizSystem));
				}
			}
			
		}
		return dataList;
	}

	// p1.2:3低利用率应用系统
	@Override
	public List<Map<String, Object>> bizSysDayType2UseRate(@RequestBody AlertThirdRequest params) throws Exception {
		List<Map<String, Object>> rs = Lists.newArrayList();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String day = DateUtils.getPastDate(new Date(), -1, sdf);

		IdcTypePhysicalReq req = new IdcTypePhysicalReq();
		if(StringUtils.isNotBlank(params.getDepartmentType())) {
			if(params.getDepartmentType().equals("department1")) {
				req.setDepartment1(params.getDepartment());
			}else {
				req.setDepartment2(params.getDepartment());
			}
		}
		
		//req.setDepartment1(getDepartmentId(department1));
		req.setSourceType("cpu");
		req.setStartDate(day + " 00:00:00");
		req.setEndDate(day + " 23:59:59");
		Map<String, Object> cpuMaps = esIndexPageServiceClient.deviceUtilization(req);
		float cpuMax = 0f;
		float cpuAvg = 0f;
		float memMax = 0f;
		float memAvg = 0f;
		float memMaxLast = 0f;
		float memAvgLast = 0f;
		float cpuMaxLast = 0f;
		float cpuAvgLast = 0f;
		if (cpuMaps.size() > 0) {
			Map<String, Object> cpuMap = (Map<String, Object>) cpuMaps.get("");
			cpuMax = cpuMap.get("max") == null ? 0 : Float.parseFloat(cpuMap.get("max").toString());
			cpuAvg = cpuMap.get("avg") == null ? 0 : Float.parseFloat(cpuMap.get("avg").toString());
		}

		req.setSourceType("memory");
		Map<String, Object> memMaps = esIndexPageServiceClient.deviceUtilization(req);
		if (memMaps.size() > 0) {
			Map<String, Object> memMap = (Map<String, Object>) memMaps.get("");
			memMax = memMap.get("max") == null ? 0 : Float.parseFloat(memMap.get("max").toString());
			memAvg = memMap.get("avg") == null ? 0 : Float.parseFloat(memMap.get("avg").toString());
		}

		day = DateUtils.getPastDate(new Date(), -2, sdf);
		req.setStartDate(day + " 00:00:00");
		req.setEndDate(day + " 23:59:59");
		Map<String, Object> memMapLasts = esIndexPageServiceClient.deviceUtilization(req);
		if (memMapLasts.size() > 0) {
			Map<String, Object> memMapLast = (Map<String, Object>) memMapLasts.get("");
			memMaxLast = memMapLast.get("max") == null ? 0 : Float.parseFloat(memMapLast.get("max").toString());
			memAvgLast = memMapLast.get("avg") == null ? 0 : Float.parseFloat(memMapLast.get("avg").toString());
		}

		req.setSourceType("cpu");
		Map<String, Object> cpuMapLasts = esIndexPageServiceClient.deviceUtilization(req);
		if (cpuMapLasts.size() > 0) {
			Map<String, Object> cpuMapLast = (Map<String, Object>) cpuMapLasts.get("");
			cpuMaxLast = cpuMapLast.get("max") == null ? 0 : Float.parseFloat(cpuMapLast.get("max").toString());
			cpuAvgLast = cpuMapLast.get("avg") == null ? 0 : Float.parseFloat(cpuMapLast.get("avg").toString());
		}

		rs.add(formData(cpuMax, cpuMaxLast, "cpuMax"));
		rs.add(formData(cpuAvg, cpuAvgLast, "cpuAvg"));
		rs.add(formData(memMax, memMaxLast, "memoryMax"));
		rs.add(formData(memAvg, memAvgLast, "memoryAvg"));
		return rs;
	}

	Map<String, Object> formData(float value, float valueLast, String name) {
		Map<String, Object> cpuMaxRs = Maps.newHashMap();
		cpuMaxRs.put("name", name);
		cpuMaxRs.put("value", value);
		cpuMaxRs.put("per", Utils.formatDouble(value - valueLast, 2));
		return cpuMaxRs;
	}

	/**
	 * 查询性能打分数据
	 */
	@Override
	public PageResponse<Map<String, Object>> getPoorEfficiencyDevice(@RequestBody Map<String,Object> params) {
	  int count = alertsPoorEfficiencyDeviceDao.getDeviceDataCount(params);
        PageResponse<Map<String,Object>> pageResponse = new PageResponse< Map<String,Object>>();
        pageResponse.setCount(count);
        if (count > 0) {
            List<Map<String,Object>> data = alertsPoorEfficiencyDeviceDao.getDeviceDataPageList(params);
            pageResponse.setResult(data);
        }
		return pageResponse;
	}
}
