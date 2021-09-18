package com.aspire.ums.cmdb.deviceStatistic.service.impl;

import com.aspire.ums.cmdb.deviceStatistic.payload.ConfigureChangeRequest;
import com.aspire.ums.cmdb.deviceStatistic.service.ConfigureChangeService;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v3.es.service.ICmdbESService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 配置项变更统计
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.ums.cmdb.deviceStatistic.service.impl
 * 类名称:    ConfigureChangeServiceImpl.java
 * 类描述:    设备统计业务层
 * 创建人:    JinSu
 * 创建时间:  2018/9/14 15:55
 * 版本:      v1.0
 */
@Slf4j
@Service
@Transactional
public class ConfigureChangeServiceImpl implements ConfigureChangeService {
    @Autowired
	private ICmdbESService cmdbESService;
	//配置项变更统计
	@Override
	public List<Map<String, Object>> selectConfigureChange(ConfigureChangeRequest configureChangeRequest) {
		log.info("configureChangeRequest is {} ",configureChangeRequest);
		Map<String, Object> hashMap=new HashMap<String, Object>();
		hashMap.put("startDate", configureChangeRequest.getStartDate());
		hashMap.put("endDate", configureChangeRequest.getEndDate());
		hashMap.put("idcType", configureChangeRequest.getIdcType());
		hashMap.put("department1", configureChangeRequest.getDepartment1());
		hashMap.put("department2", configureChangeRequest.getDepartment2());
		hashMap.put("bizSystem", configureChangeRequest.getBizSystem());
		hashMap.put("deviceClass", configureChangeRequest.getDeviceClass());
		hashMap.put("deviceType", configureChangeRequest.getDeviceType());
		List<Map<String, Object>> queryParams = new LinkedList<>();
		if (StringUtils.isNotEmpty(configureChangeRequest.getStartDate())) {
			String startDate = configureChangeRequest.getStartDate();
			String endDate = startDate;
			if (StringUtils.isNotEmpty(configureChangeRequest.getEndDate())) {
				endDate = configureChangeRequest.getEndDate();
			}
			queryParams.add(addCondication("approval_time", "between", Arrays.asList(new String[]{startDate, endDate})));
		}
		String idcType = configureChangeRequest.getIdcType();
		List<String> idcTypeList = configureChangeRequest.getAuthIdcIdList();
		if (StringUtils.isNotEmpty(idcType)) {
			if (idcTypeList != null && !idcTypeList.contains(idcType)) {
				return new ArrayList<>();
			}
			queryParams.add(addCondication("idcType", "=", idcType));
		} else {
			if (idcTypeList != null) {
				queryParams.add(addCondication("idcType", "in", idcTypeList));
			}
		}
		if (StringUtils.isNotEmpty(configureChangeRequest.getDepartment1())) {
			queryParams.add(addCondication("department1", "=", configureChangeRequest.getDepartment1()));
		}
		if (StringUtils.isNotEmpty(configureChangeRequest.getDepartment2())) {
			queryParams.add(addCondication("department2", "=", configureChangeRequest.getDepartment2()));
		}
		String bizSystem = configureChangeRequest.getBizSystem();
		List<String> authSystemList = configureChangeRequest.getAuthBizSystemIdList();
		if (StringUtils.isNotEmpty(bizSystem)) {
			if (!authSystemList.contains(bizSystem)) {
				return new ArrayList<>();
			}
			queryParams.add(addCondication("bizSystem", "=", bizSystem));
		} else {
			if (authSystemList != null) {
				queryParams.add(addCondication("bizSystem", "in", authSystemList));
			}
		}
		if (StringUtils.isNotEmpty(configureChangeRequest.getDeviceClass())) {
			queryParams.add(addCondication("deviceClass", "=", configureChangeRequest.getDeviceClass()));
		}
		String deviceType = configureChangeRequest.getDeviceType();
		List<String> authTypeList = configureChangeRequest.getAuthDeviceTypeList();
		if (StringUtils.isNotEmpty(deviceType)) {
			if (!authTypeList.contains(deviceType)) {
				return new ArrayList<>();
			}
			queryParams.add(addCondication("deviceType", "=", configureChangeRequest.getDeviceType()));
		} else {
			if (authTypeList != null) {
				queryParams.add(addCondication("deviceType", "in", authTypeList));
			}
		}
		queryParams.add(addCondication("approvalStatus", "=", "1"));
		List<String> statsFileds = Arrays.asList(new String[]{"idcType", "department1", "department2", "bizSystem", "deviceClass", "deviceType"});
		Map<String, Object> aggs = new HashMap<>();
		aggs.put("field", statsFileds);
		Map<String, Object> esParams = new HashMap<>();
		esParams.put("params", queryParams);
		esParams.put("aggs", aggs);
		return cmdbESService.stats(esParams, "cmdb_es", "approval");
	}

	private Map<String, Object> addCondication(String filed, String operator, Object value) {
		Map<String, Object> param = new HashMap<>();
		param.put("filed", filed);
		param.put("operator", operator);
		param.put("value", value);
		return param;
	}
}
