package com.aspire.mirror.alert.server.helper;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aspire.mirror.alert.server.clientservice.CmdbDictClient;
import com.aspire.mirror.alert.server.clientservice.CmdbResfulClient;
import com.aspire.ums.cmdb.dict.payload.ConfigDict;
import com.aspire.ums.cmdb.restful.payload.StatisticRequestEntity;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lupeng
 */
@Component
@Slf4j
public class UtilsHelper {
	@Autowired
	private CmdbResfulClient cmdbResfulClient;
	@Value("${cmdbQueryType.department:alert_query_department_by_bizSystem_id}")
	private String cmdbQueryName;

	@Autowired
	private CmdbDictClient cmdbDictClient;

	private static String INNER_DEPARTMENT_TYPE = "inner_department_flag";

	private static String CMDB_BIZSYSTEM_QUERY_BY_IDS = "alert_query_bizSystem_info_by_ids";

	private static String CMDB_DEPARTMENT_QUERY_BY_IDS = "alert_query_department_info_by_ids";
	

	// cmdb通用接口调用
	public Object getCmdbData(Map<String, Object> params, String name, String responseType) {
		StatisticRequestEntity entity = new StatisticRequestEntity();
		entity.setName(name);
		entity.setParams(params);
		entity.setResponseType(responseType);
		Object value = cmdbResfulClient.getInstanceStatistics(entity);
		return value;
	}

	// 根据业务系统id查询一二级租户信息
	public void formMapBizData(Map<String, Map<String, Object>> dataMap, List<String> bizList) {
		Map<String, Map<String, Object>> bizMap = Maps.newHashMap();
		if (bizList == null || bizList.size() == 0) {
			bizList = Lists.newArrayList();
			for (Map.Entry<String, Map<String, Object>> entry : dataMap.entrySet()) {
				// 获取租户1，2
				String bizSystem = entry.getValue().get("bizSystem").toString();
				if(StringUtils.isNotEmpty(bizSystem)) {
					bizList.add(bizSystem);
				}
				

			}
		}
		Map<String, Object> params = Maps.newHashMap();
		params.put("id", bizList);
		Object value = getCmdbData(params, this.cmdbQueryName, "list");
		if (null != value) {
			List<Map<String, Object>> departList = (List<Map<String, Object>>) value;
			for (Map<String, Object> m : departList) {
				String bizSystem = m.get("id").toString();
				bizMap.put(bizSystem, m);
			}
			for (Map.Entry<String, Map<String, Object>> entry : dataMap.entrySet()) {
				Map<String, Object> v = entry.getValue();
				String bizSystem = v.get("bizSystem").toString();
				Map<String, Object> bizObj = bizMap.get(bizSystem);
				if (null != bizObj) {
					v.put("bizSystem_id",bizObj.get("id"));
					v.put("department1_id",bizObj.get("department1"));
					v.put("department2_id",bizObj.get("department2"));
					v.put("department1", bizObj.get("department1_name"));
					v.put("department2", bizObj.get("department2_name"));
					v.put("bizSystem",bizObj.get("bizSystem"));
				}
			}
		}
		
	}

	public void formListBizData(List<Map<String, Object>> dataList, List<String> bizList) {
		Map<String, Map<String, Object>> bizMap = Maps.newHashMap();
		if (bizList == null || bizList.size() == 0) {
			bizList = Lists.newArrayList();
			for (Map<String, Object> entry : dataList) {
				// 获取租户1，2
				String bizSystem = entry.get("bizSystem").toString();
				if(StringUtils.isNotEmpty(bizSystem)) {
					bizList.add(bizSystem);
				}
				
			}
		}
		Map<String, Object> params = Maps.newHashMap();
		params.put("id", bizList);
		Object value = getCmdbData(params, this.cmdbQueryName, "list");
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
					entry.put("bizSystem_id",bizObj.get("id"));
					entry.put("department1_id",bizObj.get("department1"));
					entry.put("department2_id",bizObj.get("department2"));
					entry.put("department1", bizObj.get("department1_name"));
					entry.put("department2", bizObj.get("department2_name"));
					entry.put("bizSystem",bizObj.get("bizSystem"));
					
				}
			}
		}
		
	}
	
	public void formListBizIdData(List<Map<String, Object>> dataList, List<String> bizList) {
		Map<String, Map<String, Object>> bizMap = Maps.newHashMap();
		if (bizList == null || bizList.size() == 0) {
			bizList = Lists.newArrayList();
			for (Map<String, Object> entry : dataList) {
				// 获取租户1，2
				String bizSystem = entry.get("bizSystem").toString();
				if(StringUtils.isNotEmpty(bizSystem)) {
					bizList.add(bizSystem);
				}
				

			}
		}
		Map<String, Object> params = Maps.newHashMap();
		params.put("id", bizList);
		Object value = getCmdbData(params, this.cmdbQueryName, "list");
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
					entry.put("department1", bizObj.get("department1"));
					entry.put("department2", bizObj.get("department2"));
					//entry.put("bizSystem",bizObj.get("bizSystem"));
					
				}
			}
		}
		
	}
	
	 

	public ConfigDict getinnerDepartment() {
		List<ConfigDict> list = cmdbDictClient.getDictsByType(this.INNER_DEPARTMENT_TYPE, null, null, null);
		/*
		 * ConfigDict d = new ConfigDict();
		 * d.setName("6c1f1415-aa0d-11e9-995c-0242ac110002"); list.add(d);
		 */
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	// 返回业务系统或租户的id、namelist
	public List<Map<String, Object>> getDepartmentDataListByType(List<String> idList, String type) {
		String responseType = "list";
		String name = "";
		Map<String, Object> params = Maps.newHashMap();
		if (type.equals("bizSystem")) {
			name = this.CMDB_BIZSYSTEM_QUERY_BY_IDS;
			params.put("bizSystem", idList);
		}
		if (type.equals("department")) {
			name = this.CMDB_DEPARTMENT_QUERY_BY_IDS;
			params.put("department", idList);
		}

		List<Map<String, Object>> dataList = (List<Map<String, Object>>) getCmdbData(params, name, responseType);
		return dataList;

	}

	// 返回业务系统或租户的id、name映射
	public Map<String, String> getDepartmentDataMapByTye(List<String> idList, String type) {
		List<Map<String, Object>> dataList = getDepartmentDataListByType(idList, type);
		Map<String, String> map = Maps.newHashMap();
		if (null == dataList || dataList.size() == 0) {
			return map;
		}
		
		for (Map<String, Object> m : dataList) {
			if (type.equals("bizSystem")) {
				map.put( MapUtils.getString(m, "id"), MapUtils.getString(m, "bizSystem"));
			}
			if (type.equals("department")) {
				map.put(MapUtils.getString(m, "id"),MapUtils.getString(m, "department"));
			}

		}
		return map;
	}

	public String getCmdbName(Map<String, Map<String, String>> columnsMap, Map<String, Object> m, String name) {
		Map<String, String> colM = columnsMap.get(name);
		if (null != colM && null != colM.get("type") && colM.get("type").equals("ref")) {
			return m.get(colM.get("ref_name")) == null ? "" : m.get(colM.get("ref_name")).toString();
		} else {
			return m.get(name) == null ? "" : m.get(name).toString();
		}

	}
}
