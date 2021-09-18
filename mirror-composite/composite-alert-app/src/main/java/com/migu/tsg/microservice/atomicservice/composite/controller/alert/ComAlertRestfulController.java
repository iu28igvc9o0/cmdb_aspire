package com.migu.tsg.microservice.atomicservice.composite.controller.alert;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import com.aspire.mirror.alert.api.dto.alert.AlertKpiBook;
import com.aspire.mirror.alert.api.dto.alert.AlertStandardization;
import com.aspire.mirror.composite.payload.Result;
import com.aspire.mirror.composite.payload.alert.ComAlertEsDataRequest;
import com.aspire.mirror.composite.payload.alert.ComAlertKpiBook;
import com.aspire.mirror.composite.payload.alert.ComAlertStandardization;
import com.aspire.mirror.composite.payload.dashboard.ComAlertDashboardResponse;
import com.aspire.mirror.elasticsearch.api.dto.DataSetRequest;
import com.aspire.mirror.elasticsearch.api.dto.DevicePusedTopN;
import com.aspire.mirror.elasticsearch.api.dto.HistorySearchRequest;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.dashboard.DashboardEsServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.monitor.EsIndexPageServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.monitor.HistoryServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ToolUtils;
import com.migu.tsg.microservice.atomicservice.composite.helper.AlertHelper;
import com.migu.tsg.microservice.atomicservice.composite.helper.CmdbV2Helper;
import com.migu.tsg.microservice.atomicservice.composite.helper.ResourceAuthV2Helper;
import com.migu.tsg.microservice.atomicservice.composite.vo.alert.AlertFieldDetailVo;
import com.migu.tsg.microservice.atomicservice.composite.vo.alert.QueryFieldVo;
import com.migu.tsg.microservice.atomicservice.composite.vo.alert.QueryParamsVo;
import com.migu.tsg.microservice.atomicservice.composite.vo.cmdb.ResultVo;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.service.alert.IComAlertRestfulService;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.alert.AlertRestfulClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;

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
public class ComAlertRestfulController implements IComAlertRestfulService {

	@Autowired
	private AlertRestfulClient alertRestfulClient;
	@Autowired
	private DashboardEsServiceClient dashboardEsServiceClient;

	@Autowired
	private CmdbV2Helper cmdbV2Helper;

	@Autowired
	private ResourceAuthV2Helper authV2Helper;

	@Autowired
	private EsIndexPageServiceClient esIndexPageServiceClient;

	@Autowired
	private AlertHelper alertHelper;
	@Autowired
	private HistoryServiceClient historyServiceClient;

	@Value("${cmdbQueryType.alert_grafana_query_cpu_info:alert_grafana_query_cpu_info}")
	private String cmdbQueryName;

	@Value("${serverHour:2}")
	private int serverHour;

	@Override
	public List<HashMap<String, String>> getMonitorList(
			@RequestParam(value = "device_class", required = false) String deviceClass,
			@RequestParam(value = "device_type", required = false) String deviceType) {

		return alertRestfulClient.getMonitorList(deviceClass, deviceType);
	}

	@Override
	public String bookAlerts(@RequestBody ComAlertStandardization stand) {

		return alertRestfulClient.bookAlerts(PayloadParseUtil.jacksonBaseParse(AlertStandardization.class, stand));

	}

	@Override
	public String bookKpiList(@RequestBody ComAlertKpiBook regData) {
		return alertRestfulClient.bookKpiList(PayloadParseUtil.jacksonBaseParse(AlertKpiBook.class, regData));
	}

	@Override
	public Map<String, Object> getIdcTypeStoreUseRate(@RequestBody ComAlertEsDataRequest request)
			throws ParseException {
		Map<String, Object> storMap = Maps.newHashMap();
		storMap.put(request.getDeviceType() + "利用率", 0);
		storMap.put("同比上月份", 0);
		return storMap;
	}

	@Override
	public ComAlertDashboardResponse queryServerData(
			@RequestParam(value = "instanceId", required = true) String instanceId,
			@RequestParam(value = "itermType", required = true) String itermType,
			@RequestParam(value = "deviceClass", required = true) String deviceClass,
			@RequestParam(value = "countTypeFlag", required = false) Boolean countTypeFlag) throws Exception {
		DataSetRequest request = new DataSetRequest();
		if (null == countTypeFlag) {
			countTypeFlag = true;
		}

		request.setInstanceId(instanceId);
		request.setItermType(itermType);
		request.setDeviceClass(deviceClass);
		request.setCountTypeFlag(countTypeFlag);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		request.setEndTime(c.getTimeInMillis());
		c.add(Calendar.HOUR, -serverHour);
		request.setStartTime(c.getTimeInMillis());

		// request.setEndTime(1595857398000l);
		// request.setStartTime(1595785398000l);

		return PayloadParseUtil.jacksonBaseParse(ComAlertDashboardResponse.class, dashboardEsServiceClient.queryDataList(request));
	}

	@Override
	public Map<String, Object> queryServerInfo(@RequestParam(value = "instanceId", required = true) String instanceId,
			@RequestParam(value = "itermType", required = false) String itermType,
			@RequestParam(value = "deviceClass", required = false) String deviceClass) throws Exception {
		Map<String, Object> map = Maps.newHashMap();

		Map<String, Object> params = Maps.newHashMap();
		params.put("condicationCode", "instance_detail");
		params.put("id", instanceId);
		Map<String, Object> deviceInfo = cmdbV2Helper.getInstanceDetail(params);
		if (null == deviceInfo) {
			log.error("设备不存在");
			return map;
		}
		map.put("deviceName", deviceInfo.get("device_name"));
		map.put("ip", deviceInfo.get("ip"));

		DataSetRequest request = new DataSetRequest();
		request.setInstanceId(instanceId);
		request.setItermType("sysTime");
		request.setDeviceClass(deviceClass);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		request.setEndTime(c.getTimeInMillis());
		c.add(Calendar.HOUR, -serverHour);
		request.setStartTime(c.getTimeInMillis());

		try {
			Map<String, Object> timeMap = dashboardEsServiceClient.queryLatestData(request);
			Long runTime = null;
			if (null != timeMap && !timeMap.isEmpty()) {
				Object value = timeMap.get("value");
				if (null != value) {
					String valueTime = value.toString();
					if (!(valueTime.trim().equals(""))) {
						if(value instanceof Long) {
							runTime = Long.parseLong(valueTime);
						}else if(value instanceof Double) {
							Double doubleValue = Double.parseDouble(valueTime);
							runTime = doubleValue.longValue();
						}
						
					}
				}
			}
			if (runTime == null) {
				String insertTime = deviceInfo.get("insert_time").toString();
				if (StringUtils.isNotEmpty(insertTime)) {
					Date time = DateUtils.parseDate(insertTime, new String[] { "yyyy-MM-dd HH:mm:ss" });
					Date cuiDate = new Date();
					runTime = (cuiDate.getTime() - time.getTime()) / 1000;
				}
			}
			if (runTime != null) {
				long yearCount = 365l * 24 * 60 * 60;
				map.put("RunningTimeLong", runTime);
				String runTimeStr = "";
				if (runTime >= yearCount) {
					runTimeStr = ToolUtils.formatDouble(((double) runTime / yearCount), 2) + "年";
				} else {
					long monthCount = 30l * 24 * 60 * 60;
					if (runTime >= monthCount) {
						runTimeStr = ToolUtils.formatDouble(((double) runTime / monthCount), 2) + "月";
					} else {
						int dayCount = 24 * 60 * 60;
						runTimeStr = ToolUtils.formatDouble(((double) runTime / dayCount), 2) + "天";
					}
				}
				map.put("RunningTime", runTimeStr);

			}
			//查询内存总量
			request.setItermType("memorySize");
			Map<String, Object> memoryMap = dashboardEsServiceClient.queryLatestData(request);
			map.put("memoryCount", memoryMap.get("value"));
			
		}catch(Exception e) {
			log.error(e.getMessage());
		}
		params.clear();
		params.put("id", instanceId);
		Object info = cmdbV2Helper.getCmdbData(params, this.cmdbQueryName, "map");
		if (null != info) {
			Map<String, Object> mapInfo = (Map<String, Object>) info;
			//map.put("memoryCount", mapInfo.get("memory_size"));
			map.put("cpuCount", mapInfo.get("cpu_core_number"));
		}
		return map;
	}

	@Override
	public Map<String, Object> queryNetInfo(@RequestParam(value = "instanceId", required = true) String instanceId,
			@RequestParam(value = "itermType", required = false) String itermType,
			@RequestParam(value = "deviceClass", required = false) String deviceClass) throws Exception {
		Map<String, Object> map = Maps.newHashMap();

		DataSetRequest request = new DataSetRequest();
		request.setInstanceId(instanceId);
		request.setItermType("sysTime");
		request.setDeviceClass(deviceClass);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		request.setEndTime(c.getTimeInMillis());
		c.add(Calendar.HOUR, -serverHour);
		request.setStartTime(c.getTimeInMillis());
		try {
		Map<String, Object> timeMap = dashboardEsServiceClient.queryLatestData(request);
		Long runTime = null;
		if (null != timeMap && !timeMap.isEmpty()) {
			Object value = timeMap.get("value");
			if (null != value) {
				String valueTime = value.toString();
				if (!(valueTime.trim().equals(""))) {
					if(value instanceof Long) {
						runTime = Long.parseLong(valueTime);
					}else if(value instanceof Double) {
						Double doubleValue = Double.parseDouble(valueTime);
						runTime = doubleValue.longValue();
					}
				}
			}
		}

		if (runTime != null) {
			long yearCount = 365l * 24 * 60 * 60;
			map.put("RunningTimeLong", runTime);
			String runTimeStr = "";
			if (runTime >= yearCount) {
				runTimeStr = ToolUtils.formatDouble(((double) runTime / yearCount), 2) + "年";
			} else {
				long monthCount = 30l * 24 * 60 * 60;
				if (runTime >= monthCount) {
					runTimeStr = ToolUtils.formatDouble(((double) runTime / monthCount), 2) + "月";
				} else {
					int dayCount = 24 * 60 * 60;
					runTimeStr = ToolUtils.formatDouble(((double) runTime / dayCount), 2) + "天";
				}
			}
			map.put("RunningTime", runTimeStr);

		}
		}catch(Exception e) {
			log.error(e.getMessage());
		}

		request.setItermType("interfaceCount");
		List<Map<String, Object>> interfaceCountList = dashboardEsServiceClient.queryTermDatas(request);
		request.setItermType("itemCount");
		List<Map<String, Object>> itemCountList = dashboardEsServiceClient.queryTermDatas(request);
		if (interfaceCountList.size() > 0) {
			map.put("interfaceCount", interfaceCountList.get(0).get("value"));
		}
		if (itemCountList.size() > 0) {
			map.put("itemCount", itemCountList.get(0).get("value"));
		}
		request.setItermType("pingTime");
		Map<String, Object> data = dashboardEsServiceClient.queryLatestData(request);
		if (!data.isEmpty()) {
			map.put("pingTime", data.get("value"));
		}
		return map;
	}

	@Override
	public PageResponse<Map<String, Object>> getCmdbPageList(@RequestBody Map<String, Object> params) {

		return alertRestfulClient.getCmdbPageList(params);
	}

	@ResAction(action = "view", resType = "cmdb", loadResFilter=true)
	public Result<Map<String, Object>> getInstanceKpiList(@RequestBody Map<String, Object> params,
			@RequestParam(value = "moduleType", required = false) String moduleType) {

		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		RequestAuthContext.RequestHeadUser user = authCtx.getUser();
		// 互联网的IP地址库权限没有加进来, 需要先屏蔽. modify by zhujuwang 2020.06.18
//		if (!user.isAdmin() && !user.isSuperUser()) {
//			Map<String, Set<String>> totalConstraints = resAuthHelper.verifyActionAndGetResource(
//					user, authCtx.getResAction(), authCtx.getFlattenConstraints());
//			// 将参数处理成字符串,添加到请求参数中
//			if (!super.applyGeneralAuthConstraintsWithMap(totalConstraints, params)){
//				return new Result<>();
//			}
//		}
		List<String> permissionList = authV2Helper.listUserActions(user.getNamespace(), user.getUsername(), null);
		if (!permissionList.contains("res:ipAdmin")) {
			params.put("update_person", user.getUsername());
		}
		ResultVo<Map<String, Object>> list = cmdbV2Helper.getInstanceListV3(params, moduleType);
		List<Map<String, Object>> listData = list.getData();
		if (!CollectionUtils.isEmpty(listData)) {
			List<String> idList = listData.stream().map(item -> MapUtils.getString(item, "id"))
					.collect(Collectors.toList());
			DevicePusedTopN devicePusedTopN = new DevicePusedTopN();
			devicePusedTopN.setResourceIds(idList);
			devicePusedTopN.setSize(idList.size());
			List<Map<String, Object>> cpuPusedList = esIndexPageServiceClient.devicePusedTopN("CPU_PUSED",
					devicePusedTopN);
			List<Map<String, Object>> memoryPusedList = esIndexPageServiceClient.devicePusedTopN("MEMORY_PUSED",
					devicePusedTopN);
			List<Map<String, Object>> levelList = alertRestfulClient.getDeviceNewestAlertLevelList(idList);
			for (Map<String, Object> map : listData) {
				String id = MapUtils.getString(map, "id");
				if (StringUtils.isEmpty(id)) {
					continue;
				}
				for (Map<String, Object> cpu : cpuPusedList) {
					String deviceId = MapUtils.getString(cpu, "resourceId");
					if (id.equals(deviceId)) {
						map.put("CPU_PUSED", cpu.get("value"));
						break;
					}
				}
				for (Map<String, Object> mem : memoryPusedList) {
					String deviceId = MapUtils.getString(mem, "resourceId");
					if (id.equals(deviceId)) {
						map.put("MEMORY_PUSED", mem.get("value"));
						break;
					}
				}
				for (Map<String, Object> level : levelList) {
					String deviceId = MapUtils.getString(level, "device_id");
					if (id.equals(deviceId)) {
						map.put("alert_level", level.get("alert_level"));
						break;
					}
				}
			}
		}
		return PayloadParseUtil.jacksonBaseParse(Result.class, list);

	}

	/**
	 * 获取告警设备指标列表
	 * 
	 * @param params
	 * @return
	 */
	@ResAction(action = "view", resType = "alert", loadResFilter=true)
	public Result<Map<String, Object>> getAlertKpiList(@RequestBody Map<String, Object> params) {
		List<AlertFieldDetailVo> fieldList = alertHelper.getModelFromRedis("alert_alerts", null);
		QueryParamsVo queryParams = new QueryParamsVo();
		queryParams.setPageNum(MapUtils.getInteger(params, "currentPage"));
		queryParams.setPageSize(MapUtils.getInteger(params, "pageSize"));
		List<QueryFieldVo> list = Lists.newArrayList();
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			String key = entry.getKey();
			String value = MapUtils.getString(params, key);
			for (AlertFieldDetailVo alertFieldDetail : fieldList) {
				String ciCode = alertFieldDetail.getCiCode();
				if (StringUtils.isNotEmpty(ciCode) && ciCode.startsWith(key)) {
					QueryFieldVo queryField = new QueryFieldVo();
					queryField.setFieldName(alertFieldDetail.getFieldCode());
					queryField.setFieldType("and");
					queryField.setFieldValue(value);
					list.add(queryField);
					break;
				}
				if (key.equalsIgnoreCase(alertFieldDetail.getFieldCode())) {
					QueryFieldVo queryField = new QueryFieldVo();
					queryField.setFieldName(alertFieldDetail.getFieldCode());
					queryField.setFieldType("and");
					queryField.setFieldValue(value);
					list.add(queryField);
					break;
				}
			}
		}
		queryParams.setList(list);
		PageResponse<Map<String, Object>> mapPageResponse = alertHelper.queryDeviceAlertList(queryParams);
		List<Map<String, Object>> list1 = mapPageResponse.getResult();
		if (!CollectionUtils.isEmpty(list1)) {
			List<Map<String, Object>> paramsList = list1.stream().map(item -> {
				if (!item.containsKey("item_key")) {
					return null;
				}
				Map<String, Object> map = new HashMap<>(2);
				map.put("item", item.get("item_key"));
				map.put("resourceId", item.get("device_id"));
				return map;
			}).filter(item -> null != item).collect(Collectors.toList());
			List<Map<String, Object>> kpiValueList = esIndexPageServiceClient.getKpiListByKey(paramsList);
			for (Map<String, Object> map : list1) {
				String id = MapUtils.getString(map, "device_id");
				if (StringUtils.isEmpty(id)) {
					continue;
				}
				for (Map<String, Object> kpiValue : kpiValueList) {
					String deviceId = MapUtils.getString(kpiValue, "resourceId");
					if (id.equals(deviceId)) {
						map.put("kpi_value", kpiValue.get("value"));
						break;
					}
				}
			}
		}
		Result<Map<String, Object>> result = new Result<>();
		result.setData(list1);
		result.setTotalSize(mapPageResponse.getCount());
		return result;
	}

	@Override
	@ResAction(action = "view", resType = "cmdb", loadResFilter=true)
	public Result<Map<String, Object>> getInstanceMonitorValueList(@RequestBody Map<String, Object> params,
			@RequestParam(value = "moduleType", required = false) String moduleType) {
		params.put("condicationCode", "getInstanceMonitorValueList");
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		RequestAuthContext.RequestHeadUser user = authCtx.getUser();

		String date = params.get("monitorValueQuerydate").toString();
		String kpi = params.get("monitorValueQuerykpi").toString();
		if (kpi.equals("cpu")) {
			kpi = "CPU_PUSED";
		} else {
			kpi = "MEMORY_PUSED";
		}

		List<String> permissionList = authV2Helper.listUserActions(user.getNamespace(), user.getUsername(), null);
		if (!permissionList.contains("res:ipAdmin")) {
			params.put("update_person", user.getUsername());
		}
		 

		ResultVo<Map<String, Object>> list = cmdbV2Helper.getInstanceListV3(params, moduleType);
		List<Map<String, Object>> listData = list.getData();
		if (!CollectionUtils.isEmpty(listData)) {
			List<String> idList = listData.stream().map(item -> MapUtils.getString(item, "id"))
					.collect(Collectors.toList());
			HistorySearchRequest request = new HistorySearchRequest();
			request.setIdList(idList);
			request.setStartTime(date + " 00:00:00");
			request.setEndTime(date + " 23:59:59");
			request.setKpi(kpi);
			Map<String, List<Map<String, Object>>> map = historyServiceClient.getInstanceMonitorValue(request);
			Map<String, Map<String, Object>> contactMap = Maps.newHashMap();
			for (Map<String, Object> m : listData) {
				String id = MapUtils.getString(m, "id");
				String idcTypeId = MapUtils.getString(m, "idcType");
				String bizSystem = MapUtils.getString(m, "bizSystem");
				String pod_name = MapUtils.getString(m, "pod_name");
				String key = pod_name + bizSystem + idcTypeId;
				if (contactMap.containsKey(key)) {
					Map<String, Object> v = contactMap.get(key);
					m.put("business_concat", v.get("business_concat"));
					m.put("business_concat_phone", v.get("business_concat_phone"));
					m.put("business_concat_email", v.get("business_concat_email"));
				} else {
					Map<String, Object> params2 = Maps.newHashMap();
					params2.put("bizSystem", bizSystem);
					params2.put("idcType", idcTypeId);
					params2.put("pod_name", pod_name);
					// params.put("id", instanceId);
					Object info = cmdbV2Helper.getCmdbData(params2, "alert_resource_effi_query_biz_concat", "list");
					if(null!=info) {
						List<Map<String, Object>> infoList = (List<Map<String, Object>>) info;
						if (null != infoList && infoList.size() > 0) {
							Map<String, Object> v = infoList.get(0);
							contactMap.put(key, v);
							m.put("business_concat", v.get("business_concat"));
							m.put("business_concat_phone", v.get("business_concat_phone"));
							m.put("business_concat_email", v.get("business_concat_email"));
						}
					}
					
				}

				if (map.containsKey(id)) {
					m.put("resourceValueList", map.get(id));
				}
			}
		}
		return PayloadParseUtil.jacksonBaseParse(Result.class, list);

	}
	
	@Override
	@ResAction(action = "view", resType = "cmdb", loadResFilter=true)
	public void  exportInstanceMonitorValueList(@RequestBody Map<String, Object> params,
			@RequestParam(value = "moduleType", required = false) String moduleType, HttpServletResponse response) throws Exception {
		params.put("condicationCode", "getInstanceMonitorValueList");
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		RequestAuthContext.RequestHeadUser user = authCtx.getUser();

		String date = params.get("monitorValueQuerydate").toString();
		String kpi = params.get("monitorValueQuerykpi").toString();
		if (kpi.equals("cpu")) {
			kpi = "CPU_PUSED";
		} else {
			kpi = "MEMORY_PUSED";
		}

		List<String> permissionList = authV2Helper.listUserActions(user.getNamespace(), user.getUsername(), null);
		if (!permissionList.contains("res:ipAdmin")) {
			params.put("update_person", user.getUsername());
		}

		ResultVo<Map<String, Object>> list = cmdbV2Helper.getInstanceListV3(params, moduleType);
		List<Map<String, Object>> listData = list.getData();
		Map<String, Map<String, String>>  columnsMap = list.getColumns();
		List<Map<String, Object>> listRs = Lists.newArrayList();
		List<String> titleList = getTitle();
		if (!CollectionUtils.isEmpty(listData)) {
			List<String> idList = listData.stream().map(item -> MapUtils.getString(item, "id"))
					.collect(Collectors.toList());
			HistorySearchRequest request = new HistorySearchRequest();
			request.setIdList(idList);
			request.setStartTime(date + " 00:00:00");
			request.setEndTime(date + " 23:59:59");
			request.setKpi(kpi);
			Map<String, List<Map<String, Object>>> map = historyServiceClient.getInstanceMonitorValue(request);
			Map<String, Map<String, Object>> contactMap = Maps.newHashMap();
			
			for (Map<String, Object> m : list.getData()) {
				Map<String, Object> mr = Maps.newHashMap();
				String id = MapUtils.getString(m, "id");
				String idcTypeId = MapUtils.getString(m, "idcType");
				String bizSystem = MapUtils.getString(m, "bizSystem");
				String pod_name = MapUtils.getString(m, "pod_name");
				String key = pod_name + bizSystem + idcTypeId;
				List<String> nameList = Lists.newArrayList();
				nameList.add("idcType");
				nameList.add("bizSystem");
				nameList.add("pod_name");
				nameList.add("device_type");
				nameList.add("department1");
				nameList.add("department2");
				setCmdbName(mr,columnsMap,m,nameList);//设置cmdb中文
				mr.put("device_name", m.get("device_name"));
				mr.put("ip", m.get("ip"));
				
				if (contactMap.containsKey(key)) {
					Map<String, Object> v = contactMap.get(key);
					mr.put("business_concat", v.get("business_concat"));
					mr.put("business_concat_phone", v.get("business_concat_phone"));
					mr.put("business_concat_email", v.get("business_concat_email"));
				} else {
					Map<String, Object> params2 = Maps.newHashMap();
					params2.put("bizSystem", bizSystem);
					params2.put("idcType", idcTypeId);
					params2.put("pod_name", pod_name);
					// params.put("id", instanceId);
					Object info = cmdbV2Helper.getCmdbData(params2, "alert_resource_effi_query_biz_concat", "list");
					if(null!=info) {
						List<Map<String, Object>> infoList = (List<Map<String, Object>>) info;
						if (null != infoList && infoList.size() > 0) {
							Map<String, Object> v = infoList.get(0);
							contactMap.put(key, v);
							mr.put("business_concat", v.get("business_concat"));
							mr.put("business_concat_phone", v.get("business_concat_phone"));
							mr.put("business_concat_email", v.get("business_concat_email"));
						}
					}
					
				}
				
				if (map.containsKey(id)) {
					List<Map<String, Object>> valueList =  map.get(id);
					for(Map<String, Object> mm:valueList) {
						String time = mm.get("time").toString();
						String title = time.split(" ")[1].substring(0, 5);
						//titleList.add(title);
						mr.put(title, mm.get("value"));
					}
				}
				listRs.add(mr);
			}
			
		}
		String[] headerList = { "设备类型", "设备名称", "资源池", "POD", "IP","一级部门", "二级部门", "业务系统"
				, "联系人", "联系电话"};
		
		String[] keyList = { "device_type_name", "device_name", "idcType_name", "pod_name_name"
				,"ip", "department1_name", "department2_name","bizSystem_name",
				"business_concat", "business_concat_phone" };
		String[] colArr = titleList.toArray(new String[titleList.size()]);
		headerList = (String[]) ArrayUtils.addAll(headerList, colArr);
		keyList = (String[]) ArrayUtils.addAll(keyList, colArr);
		String title = "资源性能数据";
		String fileName = title + ".xlsx";
		
		
		OutputStream os = response.getOutputStream();// 取得输出流
		response.setHeader("Content-Disposition",
				"attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
		response.setHeader("Connection", "close");
		response.setHeader("Content-Type", "application/vnd.ms-excel");
		// excel constuct
		ExportExcelUtil eeu = new ExportExcelUtil();
		Workbook book = new SXSSFWorkbook(128);
		eeu.exportExcel(book, 0, title, headerList, listRs, keyList);
		book.write(os);

	}
	
	private List<String> getTitle() {
		List<String> titleList = Lists.newArrayList();
		Calendar c = Calendar.getInstance();
		//c.add(Calendar.DATE, -1);
	    c.set(Calendar.HOUR_OF_DAY, 0);
	    c.set(Calendar.MINUTE, 0);
	    c.set(Calendar.SECOND, 0);
	    c.set(Calendar.MILLISECOND, 0);
	  
	    for(int i=0;i<288;i++) {
	    	 SimpleDateFormat shortSdf = new SimpleDateFormat("HH:mm");
		  	    String date = shortSdf.format(c.getTime());
		  	    titleList.add(date);
		  	  c.add(Calendar.MINUTE, 5);
	    }
		return titleList;
	}

	private void setCmdbName(Map<String, Object> mr,Map<String, Map<String, String>>  columnsMap
			,Map<String, Object> m,List<String> nameList){
		for(String name:nameList) {
			Map<String, String> colM = columnsMap.get(name);
			if(null!=colM && null!=colM.get("type")&& colM.get("type").equals("ref")) {
				mr.put(name+"_name", m.get(colM.get("ref_name")));
			}else {
				mr.put(name+"_name", m.get(name));
			}
		}
		
	}
	
	@Override
	public List<Map<String, Object>> getIdcTypePerformanceData(@RequestBody Map<String,String> params) {
		if(null==params.get("idcType") || params.get("idcType").toString().equals("")) {
			params.put("idcType", "all");
		}
		return alertRestfulClient.getIdcTypePerformanceData(params);
	}
}
