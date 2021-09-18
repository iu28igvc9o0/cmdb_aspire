package com.aspire.mirror.alert.server.task.monthReport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.aspire.mirror.alert.server.biz.ftp.FtpService;
import com.aspire.mirror.alert.server.clientservice.CmdbInstanceClient;
import com.aspire.mirror.alert.server.clientservice.ZabbixRatioServiceClient;
import com.aspire.mirror.alert.server.util.ExportExcelUtil;
import com.aspire.mirror.elasticsearch.api.dto.NetPerformanceAnalysis;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@EnableScheduling
@Component
@ConditionalOnProperty(value = "MoniterServerRatioExportTask.flag", havingValue = "normal")
public class MoniterServerRatioExportTask {

	private static final Logger LOGGER = Logger.getLogger(MoniterServerRatioExportTask.class);
	@Autowired
	private CmdbInstanceClient instanceClient;

	@Autowired
	private ZabbixRatioServiceClient zabbixRatioServiceClient;

	@Autowired
	private FtpService ftpService;

	/**
	 * 告警自动化派单定时任务
	 * 
	 * @throws Exception
	 */
	@Scheduled(cron = "${MoniterServerRatioExportTask.cron: 0 0 3 1 * ?}")
	public void syncAlertData() throws Exception {
		 LOGGER.info("synchronization MoniterServerTask begin*****************************");
		List<Map<String, Object>> result = Lists.newArrayList();
		Map<String, Object> queryInstance = Maps.newHashMap();
		String deviceType = "云主机";
		String idcType = "信息港资源池";
		int pageNo = 1;
		int pageSize = 100;
		queryInstance.put("device_type", deviceType);
		queryInstance.put("idcType", idcType);
		queryInstance.put("currentPage", pageNo);
		queryInstance.put("currentPage", pageNo);
		queryInstance.put("pageSize", pageSize);
		// Result<Map<String, Object>> rs =

		getDataList(queryInstance, result);
		
		
		queryInstance.put("device_type", "X86服务器");
		getDataList(queryInstance, result);
		queryInstance.put("device_type", "云主机");
		queryInstance.put("idcType", "南方基地");
		getDataList(queryInstance, result);
		queryInstance.put("device_type", "X86服务器");
		getDataList(queryInstance, result);

//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String fileName = "dailyReportOfServerUtilization"+date.getTime() + ".xlsx";
		String[] header = { "UUID", "管理IP", "业务IP", "类型", "CPU峰值（单位%）", "CPU均值（单位%）", "内存峰值（单位%）", "内存均值（单位%）", "资源池",
				"POD", "一级部门名称", "二级部门名称", "业务系统名称", "加电状态" };
		String[] keys = { "id", "ip", "ip", "device_type", "cpu_max", "cpu_avg", "mem_max", "mem_avg", "idcType",
				"pod_name", "department1", "department2", "bizSystem", "device_status" };

		ExportExcelUtil eeu = new ExportExcelUtil();
		Workbook book = new SXSSFWorkbook(128);
		eeu.exportExcel(book, 0, fileName, header, result, keys);
		ByteArrayOutputStream ops = null;
		ByteArrayInputStream in = null;
		try {
			ops = new ByteArrayOutputStream();
			book.write(ops);
			byte[] b = ops.toByteArray();
			in = new ByteArrayInputStream(b);
			LOGGER.info("syncAlertData导出excel开始*******************************");
			ftpService.uploadtoFTPNew(fileName, in,null);
			LOGGER.info("syncAlertData导出excel结束*******************************");
			ops.flush();
		} catch (Exception e) {
//			logger.error("导出excel失败，失败原因：", e);
           throw new RuntimeException("导出excel失败，失败原因：", e);
		} finally {
			IOUtils.closeQuietly(book);
			IOUtils.closeQuietly(ops);
			IOUtils.closeQuietly(in);
		}

		LOGGER.info("synchronization MoniterServerTask end*****************************");

	}

	public void getDataList(Map<String, Object> queryInstance, List<Map<String, Object>> result) {
		int total = instanceClient.getInstanceListV3(queryInstance,null).getTotalSize();
		int pageSize = Integer.parseInt(queryInstance.get("pageSize").toString());
		int gageAll = total / pageSize;
		int mode = total % pageSize;
		if (mode > 0) {
			gageAll++;
		}
		if (total > 0) {
			for (int i = 1; i < gageAll;i++) {
				queryInstance.put("currentPage", i);
				List<Map<String, Object>> rsList = instanceClient.getInstanceListV3(queryInstance,null).getData();
				List<String> ipList = Lists.newArrayList();
				for (Map<String, Object> map : rsList) {
					if (null != map.get("device_status")) {
						if (map.get("device_status").equals("运行正常")) {
							map.put("device_status", "是");
						} else {
							map.put("device_status", "否");
						}
					} else {
						map.put("device_status", "否");
					}
					if (null != map.get("ip")) {
						ipList.add(map.get("ip").toString());
					}
				}
				String deviceType = queryInstance.get("device_type").toString();
				String idcType = queryInstance.get("idcType").toString();
				Map<String, NetPerformanceAnalysis> dataMap = zabbixRatioServiceClient
						.getServerRatioData(deviceType, idcType, ipList);
				for (Map<String, Object> map : rsList) {
					String ip = map.get("ip").toString();
					if (null != ip) {
						NetPerformanceAnalysis val = dataMap.get(ip);
						if (null != val) {
							map.put("cpu_max", val.getCpuMax());
							map.put("cpu_avg", val.getCpuAvg());
							map.put("mem_max", val.getMemMax());
							map.put("mem_avg", val.getMemAvg());
						}
					}
				}
				result.addAll(rsList);
			}
		}
	}

}
