package com.migu.tsg.microservice.atomicservice.composite.controller.monitor;

import com.aspire.mirror.common.util.DateUtil;
import com.aspire.mirror.composite.payload.monitor.ComIdcTypePhysicalReq;
import com.aspire.mirror.composite.service.monitor.IComMonitorIndexPageService;
import com.aspire.mirror.elasticsearch.api.dto.DevicePusedTopN;
import com.aspire.mirror.elasticsearch.api.dto.IdcTypePhysicalReq;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.alert.AlertRestfulClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdbInstance.AlertCmdbInstanceServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.monitor.EsIndexPageServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import com.migu.tsg.microservice.atomicservice.composite.helper.CmdbV2Helper;
import com.migu.tsg.microservice.atomicservice.composite.helper.ResourceAuthV2Helper;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.RbacResource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class CompMonitorIndexPageController implements IComMonitorIndexPageService {
	private Logger logger = LoggerFactory.getLogger(CompMonitorIndexPageController.class);

	@Autowired
	private EsIndexPageServiceClient esIndexPageServiceClient;
	
	@Autowired
	private AlertRestfulClient alertRestfulClient;

	
	@Autowired
	private CmdbV2Helper cmdbV2Helper;
	
	 @Autowired
    protected ResourceAuthV2Helper resAuthHelper;

	@Autowired
	private AlertCmdbInstanceServiceClient alertCmdbInstanceServiceClient;

	@Value("${cmdbQueryType.department:alert_query_department_by_bizSystem_id}")
	private String cmdbQueryName;

	/*****************************************************
	 * 监控首页
	 * 
	 * @throws Exception
	 ******************************************************/
	@Override
	public Map<String, Map<String, Long>> idcTypeDeviceUsedRate(
			@RequestBody ComIdcTypePhysicalReq comIdcTypePhysicalReq) throws Exception {

		Map<String, Object> paramMap = new HashMap<>();
		if(StringUtils.isNotBlank(comIdcTypePhysicalReq.getIdcType())) {
			paramMap.put("idcType", comIdcTypePhysicalReq.getIdcType());
		}
		if(StringUtils.isNotBlank(comIdcTypePhysicalReq.getDeviceType())) {
			paramMap.put("deviceType", comIdcTypePhysicalReq.getDeviceType());
		}
		if(StringUtils.isNotBlank(comIdcTypePhysicalReq.getBizSystem())) {
			paramMap.put("bizSystem", comIdcTypePhysicalReq.getBizSystem());
		}
		paramMap.put("deviceClass", "服务器");
		
		Object value = cmdbV2Helper.getCmdbData(paramMap
				,"Alert_API_statisticsDeviceByIdcType", "list");
		Map<String, Map<String, Long>> esData = esIndexPageServiceClient
				.idcTypeDeviceUsedRate(PayloadParseUtil.jacksonBaseParse(IdcTypePhysicalReq.class, comIdcTypePhysicalReq));
		if(null!=value) {
			List<Map<String, Object>> deviceList = (List<Map<String, Object>>)value;
			if (null != deviceList && deviceList.size() > 0) {
				for (Map<String, Object> c : deviceList) {
					long count = 0;
					if (null != c.get("deviceCount")) {
						count = Long.parseLong(c.get("deviceCount").toString());
					}

					String idcType =  c.get("idcType")==null?null:c.get("idcType").toString();
					if (esData.containsKey(idcType)) {
						Map<String, Long> dataMap = esData.get(idcType);
						dataMap.put("deviceCount", count);
						/*
						 * if (dataMap.containsKey("15-40")) { count = count -
						 * Long.parseLong(dataMap.get("15-40").toString()); } if
						 * (dataMap.containsKey("40-80")) { count = count -
						 * Long.parseLong(dataMap.get("40-80").toString()); } if
						 * (dataMap.containsKey("80")) { count = count -
						 * Long.parseLong(dataMap.get("40-80").toString()); } dataMap.put("15", count);
						 */
					} /*
						 * else { Map<String, Long> dataMap = new HashMap<>();
						 * 
						 * dataMap.put("15", count); dataMap.put("15-40", 0l); dataMap.put("40-80", 0l);
						 * dataMap.put("80", 0l);
						 * 
						 * esData.put(idcType, dataMap); dataMap.put("deviceCount", count); }
						 */
				}

			}
		}
		
		return esData;
	}

	@Override
	public Map<String, Map<String, Long>> bizSystemDeviceUsedRate(
			@RequestBody ComIdcTypePhysicalReq comIdcTypePhysicalReq) throws Exception {

		Map<String, Object> paramMap = new HashMap<>();
		if(StringUtils.isNotBlank(comIdcTypePhysicalReq.getIdcType())) {
			paramMap.put("idcType", comIdcTypePhysicalReq.getIdcType());
		}
		if(StringUtils.isNotBlank(comIdcTypePhysicalReq.getDeviceType())) {
			paramMap.put("deviceType", comIdcTypePhysicalReq.getDeviceType());
		}
		if(StringUtils.isNotBlank(comIdcTypePhysicalReq.getBizSystem())) {
			paramMap.put("bizSystem", comIdcTypePhysicalReq.getBizSystem().split(","));
		}
		paramMap.put("deviceClass", "服务器");
		Object value = cmdbV2Helper.getCmdbData(paramMap
				,"Alert_API_statisticsDeviceByDepartment1", "list");

		Map<String, Map<String, Long>> esData = esIndexPageServiceClient
				.bizSystemDeviceUsedRate(PayloadParseUtil.jacksonBaseParse(IdcTypePhysicalReq.class, comIdcTypePhysicalReq));
		cmdbV2Helper.setBizDepartmentData(esData,"department");
		if(null!=value) {
			List<Map<String, Object>> deviceList = (List<Map<String, Object>>)value;
			if (null != deviceList && deviceList.size() > 0) {
				for (Map<String, Object> c : deviceList) {
					long count = 0;
					if (null != c.get("deviceCount")) {
						count = Long.parseLong(c.get("deviceCount").toString());
					}
					String idcType = c.get("department1")==null?null:c.get("department1").toString();
					if (esData.containsKey(idcType)) {
						Map<String, Long> dataMap = esData.get(idcType);
						dataMap.put("deviceCount", count);
					}
				}

			}
		}
		
		return esData;
	}
	
	@Override
	@ResAction(resType="indexpage", action="reportBizsysDeviceUsedrateTopN", loadResFilter=true)
	public Map<String, Map<String, Long>> bizSystemDeviceUsedRateTopN(
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "order") String order)
			throws Exception {
		
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

		if (StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
			Date startDate1 = new Date();
			endDate = DateUtil.format(startDate1, "yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.DATE, -1);
			Date d = c.getTime();
			startDate = DateUtil.format(d, "yyyy-MM-dd");
		}

		Map<String, Map<String, Long>> esData = esIndexPageServiceClient.bizSystemDeviceUsedRateTopN(startDate, endDate,
				size, "bizSystem", order);

		List<String> strList = Lists.newArrayList();
		for (String key : esData.keySet()) {
			strList.add(key);
		}
		/*
		 * Map<String, Object> paramMap = new HashMap<>(); paramMap.put("deviceClass",
		 * "服务器"); paramMap.put("bizSystem", strList);
		 */
		Map<String, Object> params = Maps.newHashMap();
		params.put("bizSystem", strList);
		//params.put("deviceClass", "服务器");
		Object value = cmdbV2Helper.getCmdbData(params
				,"Alert_API_statisticsDeviceByBizSystem", "list");
		cmdbV2Helper.setBizDepartmentData(esData,"bizSystem");
		if(null!=value) {
			List<Map<String, Object>> deviceList = (List<Map<String, Object>>)value;

			if (null != deviceList && deviceList.size() > 0) {
				for (Map<String, Object> c : deviceList) {
					long count = 0;
					if (null != c.get("deviceCount")) {
						count = Long.parseLong(c.get("deviceCount").toString());
					}
					String bizSystem = c.get("bizSystem")==null?"":c.get("bizSystem").toString();
					if (esData.containsKey(bizSystem)) {
						Map<String, Long> dataMap = esData.get(bizSystem);
						dataMap.put("deviceCount", count);
					}
				}

			}
		}
		
		return esData;
	}

	@Override
	public Map<String, Map<String, Long>> department1DeviceUsedRateTopN(
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "order") String order)
			throws Exception {
		if (StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
			Date startDate1 = new Date();
			endDate = DateUtil.format(startDate1, "yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.DATE, -1);
			Date d = c.getTime();
			startDate = DateUtil.format(d, "yyyy-MM-dd");
		}
		Map<String, Map<String, Long>> esData = esIndexPageServiceClient.bizSystemDeviceUsedRateTopN(startDate, endDate,
				size, "department1", order);

		List<String> strList = new ArrayList<>(esData.keySet());

		Map<String, Object> params = Maps.newHashMap();
		params.put("department1", strList);
		params.put("deviceClass", "服务器");
		Object value = cmdbV2Helper.getCmdbData(params
				,"Alert_API_statisticsDeviceByDepartment1", "list");
		cmdbV2Helper.setBizDepartmentData(esData,"department");
		if(null!=value) {
			List<Map<String, Object>> deviceList = (List<Map<String, Object>>)value;
			if (null != deviceList && deviceList.size() > 0) {
				for (Map<String, Object> c : deviceList) {
					long count = 0;
					if (null != c.get("deviceCount")) {
						count =  Long.parseLong(c.get("deviceCount").toString());
					}
					String department1 = c.get("department1")==null?null:c.get("department1").toString();
					if (esData.containsKey(department1)) {
						Map<String, Long> dataMap = esData.get(department1);
						dataMap.put("deviceCount", count);
					}
				}

			}
		}
		
		return esData;
	}

	@Override
	@ResAction(resType="monitorIndexpage", action="view", loadResFilter=true)
	public Map<String, Object> deviceUsedRateTrends(@RequestBody ComIdcTypePhysicalReq comIdcTypePhysicalReq)
			throws Exception {
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());
		String startDate = comIdcTypePhysicalReq.getStartDate();
		String endDate = comIdcTypePhysicalReq.getEndDate();
		int range= 7;
		if(comIdcTypePhysicalReq.getIndexType()==1) {
			range = 14;
		}		
		if (StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
			Date startDate1 = new Date();
			endDate = DateUtil.format(startDate1, "yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.DATE, -range);
			Date d = c.getTime();
			startDate = DateUtil.format(d, "yyyy-MM-dd");
			comIdcTypePhysicalReq.setStartDate(startDate);
			comIdcTypePhysicalReq.setEndDate(endDate);
		}
		Map<String,List<String>> authMap = alertRestfulClient.getAuthField(2);
		IdcTypePhysicalReq req = PayloadParseUtil.jacksonBaseParse(IdcTypePhysicalReq.class, comIdcTypePhysicalReq);
		req.setAuthMap(authMap);
		return esIndexPageServiceClient
				.deviceUsedRateTrends(req);
	}

	@Override
	public List<Map<String, Object>> bizSystemDeviceUsedRateLow(
			@RequestBody ComIdcTypePhysicalReq comIdcTypePhysicalReq) throws Exception {
		List<Map<String, Object>> list = esIndexPageServiceClient
				.bizSystemDeviceUsedRateLow(PayloadParseUtil.jacksonBaseParse(IdcTypePhysicalReq.class, comIdcTypePhysicalReq));
		if (null != list && list.size() > 0) {
			List<String> bizList = list.stream().map(item -> MapUtils.getString(item, "bizSystem"))
					.collect(Collectors.toList());
			formListBizData(list,bizList);
			}
		return list;
	}

	public List<Map<String, Object>> devicePusedTopN (@PathVariable("kpi") String kpi,
											   @RequestParam(name = "idcType", required = false) String idcType,
											   @RequestParam(name = "size", defaultValue = "10") int size) {
		DevicePusedTopN devicePusedTopN = new DevicePusedTopN();
		devicePusedTopN.setIdcType(idcType);
		devicePusedTopN.setSize(size);
		List<Map<String, Object>> list = esIndexPageServiceClient.devicePusedTopN(kpi, devicePusedTopN);
		if (CollectionUtils.isNotEmpty(list)) {
			for (Map<String, Object> map: list) {
				String resourceId = MapUtils.getString(map, "resourceId");
				if (StringUtils.isEmpty(resourceId)) {
					continue;
				}
				Map<String, Object> ciMap = alertCmdbInstanceServiceClient.detailById(resourceId);
				if (ciMap == null || ciMap.isEmpty()) {
					continue;
				}
				map.put("ip", ciMap.get("ip"));
				map.put("bizSystem", ciMap.get("bizSystem"));
			}
		}

		return list;
	}

	private void formListBizData(List<Map<String, Object>> dataList, List<String> bizList) {
		Map<String, Map<String, Object>> bizMap = Maps.newHashMap();
		if (bizList == null || bizList.size() == 0) {
			bizList = com.google.common.collect.Lists.newArrayList();
			for (Map<String, Object> entry : dataList) {
				// 获取租户1，2
				String bizSystem = entry.get("bizSystem").toString();
				bizList.add(bizSystem);

			}
		}
		Map<String, Object> params = Maps.newHashMap();
		params.put("id", bizList);
		Object value = cmdbV2Helper.getCmdbData(params, this.cmdbQueryName, "list");
		if (null != value) {
			List<Map<String, Object>> departList = (List<Map<String, Object>>) value;
			for (Map<String, Object> m : departList) {
				String bizSystem = m.get("id").toString();
				bizMap.put(bizSystem, m);
			}
			for (Map<String, Object> entry : dataList) {
				String bizSystem = entry.get("bizSystem").toString();
				Map<String, Object> bizObj = bizMap.get(bizSystem);

				if (null != bizObj) {
					entry.put("department1", bizObj.get("department1_name"));
					entry.put("department2", bizObj.get("department2_name"));
					entry.put("bizSystem",bizObj.get("bizSystem"));
				}
			}
		}

	}
}
