package com.aspire.mirror.zabbixintegrate.biz.alert;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.aspire.mirror.zabbixintegrate.dao.ZCmdbInstanceDao;
import com.aspire.mirror.zabbixintegrate.daoAlert.AlertModelFieldMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 告警扫描任务 <br/>
 * Project Name:zabbix-integrate File Name:AlertScanJob.java Package
 * Name:com.aspire.mirror.zabbixintegrate.biz ClassName: AlertScanJob <br/>
 * date: 2018年10月19日 上午10:42:09 <br/>
 * 
 * @author longfeng
 * @version
 * @since JDK 1.6
 */
@Slf4j
@Component
class AlertMonitorScanJob {
	@Value("${alertScan.batchCount}")
	private Integer batchCount;

	@Autowired
	private AlertModelFieldMapper alertModelFieldMapper;
	@Autowired
	private ZCmdbInstanceDao zCmdbInstanceDao;

	

	/**
	 * 监控扫描对账 <br/>
	 *
	 * 作者： longfeng
	 * 
	 * @param fetchAlertList
	 */
	@Scheduled(cron = "${monitorScan.cron:0 0 1 * * ?}")
	public void alertScanComparisonSchedule() {
		log.info("******监控扫描对账开始*******");
		//查询没有关联的设备
		List<Map<String, String>> nullValues = zCmdbInstanceDao.getCmdbNullDevices();
		if (null == nullValues || nullValues.size() == 0) {
			return;
		}
		List<String> hostList = new ArrayList<>();

		//查询资源池
		for (Map<String, String> m : nullValues) {
			String host = m.get("host");
			if (StringUtils.isNotBlank(host)) {
				if (!hostList.contains(host)) {
					hostList.add(host);
				}

			}
		}

		if (hostList.size() > 0) {
			List<Map<String, String>> idcList = alertModelFieldMapper.getIdcTypes(hostList);
			Map<String, String> idcMap = new HashMap<>();
			for (Map<String, String> idc : idcList) {
				idcMap.put(idc.get("proxy_name"), idc.get("idc"));
			}
			for (Map<String, String> m : nullValues) {
				String host = m.get("host");
				if (StringUtils.isNotBlank(host) && null != idcMap.get(host)) {
					m.put("idcType", idcMap.get(host));
				}
			}

		}

		//插入或者更新记录
		List<Map<String, String>> list = new ArrayList<>();
		List<Map<String, Object>> insertList = new ArrayList<>();
		List<Map<String, Object>> updateList = new ArrayList<>();

		Map<String, Map<String, String>> val = new HashMap<>();
		for (Map<String, String> a : nullValues) {
			Map<String, String> m = new HashMap<>();
			String deviceIp = a.get("ip") == null ? null : a.get("ip").toString();
			String idcType = a.get("idcType") == null ? null : a.get("idcType").toString();
			if (org.apache.commons.lang.StringUtils.isNotBlank(deviceIp)) {
				m.put("deviceIp", deviceIp);
			}
			if (org.apache.commons.lang.StringUtils.isNotBlank(idcType)) {
				m.put("idcType", idcType);
			}
			if (m.size() > 0) {
				val.put(deviceIp + idcType, a);
				list.add(m);
			}
		}

		if (list.size() == 0) {
			return;
		}
		List<Map<String, Object>> alertScanComparisionDetailByIpAndPools = alertModelFieldMapper
				.getAlertScanComparisionDetailByIpAndPools(list);

		for (Map<String, Object> s : alertScanComparisionDetailByIpAndPools) {
			String deviceIp = s.get("deviceIp") == null ? null : s.get("deviceIp").toString();
			String idcType = s.get("idcType") == null ? null : s.get("idcType").toString();
			if (val.containsKey(deviceIp + idcType)) {
				updateList.add(s);
				val.remove(deviceIp + idcType);
			} 
		}
		insertList.addAll(new ArrayList(val.values()));
		Date now = new Date();
		for(Map<String, Object> in:insertList) {
			in.put("synStatus", "2");
			in.put("curMoniTime", now);
			in.put("id", UUID.randomUUID().toString());
			in.put("type", 1);
		}
		val.clear();
		if (CollectionUtils.isNotEmpty(insertList))
			alertModelFieldMapper.insertScanComparision(insertList);
		if (CollectionUtils.isNotEmpty(updateList))
			alertModelFieldMapper.batchUpdate(updateList);
		log.info("******监控扫描对账结束*******");
	}
}
