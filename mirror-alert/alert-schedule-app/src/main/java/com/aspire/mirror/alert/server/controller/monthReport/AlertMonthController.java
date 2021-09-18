package com.aspire.mirror.alert.server.controller.monthReport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.aspire.mirror.alert.server.biz.dashboard.AlertRepPanelBiz;
import com.aspire.mirror.alert.server.clientservice.CmdbInstanceClient;
import com.aspire.mirror.alert.server.clientservice.EsCloudSysClient;
import com.aspire.mirror.alert.server.clientservice.ZabbixRatioServiceClient;
import com.aspire.mirror.alert.server.task.monthReport.CloudSysSyncKafkaTask;
import com.aspire.mirror.alert.server.util.ExportExcelUtil;
import com.aspire.mirror.elasticsearch.api.dto.AlertEsHistoryReq;
import com.aspire.mirror.elasticsearch.api.dto.NetPerformanceAnalysis;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.aspire.mirror.alert.server.biz.monthReport.AlertMonthDayBiz;
import com.aspire.mirror.alert.server.biz.ftp.FtpService;
import com.aspire.mirror.alert.server.dao.monthReport.AlertMonthReportSyncDao;
import com.aspire.mirror.alert.server.dao.monthReport.po.AlertMonthReportIdcType;
import com.aspire.mirror.alert.server.vo.monthReport.AlertMonthReportVo;
import com.aspire.mirror.alert.server.util.StringUtils;
import com.aspire.mirror.elasticsearch.api.dto.MonthReportRequest;

@RestController
public class AlertMonthController {

	private static final Logger logger = LoggerFactory.getLogger(AlertMonthController.class);

	@Autowired
	private FtpService ftpService;

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	@Autowired
	private AlertMonthReportSyncDao alertMonthReportSyncDao;

	@Autowired
	private AlertMonthDayBiz alertMonthDayBiz;

	@Value("${AlertMonthReportNewDayTask.ftpFilePath:monthly_operation_report}")
	private String ftpFilePath;

	@Value("${AlertMonthReportNewDayTask.ftpFilePath:download}")
	private String downFilePath;

	@Autowired
	private AlertRepPanelBiz alertRepPanelBiz;

	@Autowired
	private CloudSysSyncKafkaTask cloudSysSyncKafkaTask;

	@GetMapping(value = "/v1/alert/month/exportExcel")
	public void exportExcel(@RequestParam(value = "idcType", required = false) String idcType,
			@RequestParam(value = "month", required = false) String monthStr,
			@RequestParam(value = "hisFlag", required = false) int hisFlag) throws Exception {
		if (org.apache.commons.lang.StringUtils.isBlank(monthStr)) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, -1);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);

			Date startTime = calendar.getTime();
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
			monthStr = sdf1.format(startTime);
		}

		alertMonthDayBiz.exportBizSystemMonthExcel(monthStr, idcType, hisFlag);
	}

	@PostMapping(value = "/v1/alert/month/getBizDataByMinute")
	public void getBizDataByMinute(@RequestBody AlertMonthReportVo monthReportRequest) throws Exception {
		logger.info("*********getBizDataByMinute--begin*********");
		Runnable runnable = new Runnable() {
			public void run() {
				try {
					alertMonthDayBiz.syncBizSytemDayByMinite(monthReportRequest);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		taskExecutor.execute(runnable);
		logger.info("*********getBizDataByMinute--end********");
	}

	@PostMapping(value = "/v1/alert/month/getData")
	public void getData(@RequestBody AlertMonthReportVo monthReportRequest) throws Exception {
		logger.info("*********getData--begin*********");
		Runnable runnable = new Runnable() {
			public void run() {
				try {
					alertMonthDayBiz.syncBizSytemDay(monthReportRequest);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		taskExecutor.execute(runnable);
		logger.info("*********getData--end********");
	}

	// 暂时没有用
	@PostMapping(value = "/v1/alert/month/getIdcTypeIpData")
	public void getIdcTypeIpData(@RequestBody AlertMonthReportVo monthReportRequest) throws Exception {
		logger.info("*********getIdcTypeIpData--begin*********");
		Runnable runnable = new Runnable() {
			public void run() {
				try {
					alertMonthDayBiz.syncIdcTypeIpDay(monthReportRequest);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		taskExecutor.execute(runnable);
		logger.info("*********getIdcTypeIpData--end********");
	}

	// 按月查询资源池的均峰值存库（从数据库捞数据）
	@PostMapping(value = "/v1/alert/month/getIdcTypeNewData")
	public void getIdcTypeNewData(@RequestParam(value = "month", required = false) String month) throws Exception {
		logger.info("*********getIdcTypeNewData--begin*********");
		Runnable runnable = new Runnable() {
			public void run() {
				try {
					alertMonthDayBiz.IdcTypeMonthData(month);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		taskExecutor.execute(runnable);
		logger.info("*********getIdcTypeNewData--end********");
	}

	// 按月查询业务系统的均峰值存库（从数据库捞数据）
	@PostMapping(value = "/v1/alert/month/getBizSystemNewData")
	public void getBizSystemNewData(@RequestParam(value = "month", required = true) String month) throws Exception {
		logger.info("*********getBizSystemNewData--begin*********");
		Runnable runnable = new Runnable() {
			public void run() {
				try {
					alertMonthDayBiz.bizSystemMonthData();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		taskExecutor.execute(runnable);
		logger.info("*********getBizSystemNewData--end********");
	}

	// 按月查询net的值存库
	@PostMapping(value = "/v1/alert/month/getNetData")
	public void getNetData(@RequestParam(value = "month", required = false) String month) throws Exception {
		logger.info("*********getData--begin*********");
		Runnable runnable = new Runnable() {
			public void run() {
				try {
					alertMonthDayBiz.netMonthData(month);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		taskExecutor.execute(runnable);
		logger.info("*********getData--end********");
	}

	private void getMonthDate(Map<String, Map<String, Object>> dataMap, MonthReportRequest monthReportRequest,
			List<String> dateList) {
		StringBuffer sb = new StringBuffer();
		sb.append("_").append(monthReportRequest.getDeviceType()).append("_").append(monthReportRequest.getItem())
				.append("_max");
		String keyMax = sb.toString();
		sb.setLength(0);
		sb.append("_").append(monthReportRequest.getDeviceType()).append("_").append(monthReportRequest.getItem())
				.append("_avg");
		String keyAvg = sb.toString();
		for (Map.Entry<String, Map<String, Object>> map : dataMap.entrySet()) {
			Map<String, Object> data = map.getValue();
			double sum = 0;
			int count = 0;
			double max = 0;
			for (String list : dateList) {
				Object valMax = data.get(list + keyMax);
				Object valAvg = data.get(list + keyAvg);
				if (null != valAvg) {
					String vv = valAvg.toString();
					if (StringUtils.isNotEmpty(vv)) {
						double v = Double.parseDouble(vv);
						count++;
						sum += v;
					}

				}
				if (null != valMax) {
					String vv = valMax.toString();
					if (StringUtils.isNotEmpty(vv)) {
						double v = Double.parseDouble(vv);
						if (v > max) {
							max = v;
						}
					}

				}
			}
			if (max > 0) {
				data.put("日均均值" + keyMax, max);
			}
			if (count > 0) {
				double countff1 = (double) sum / count;
				double avg = new BigDecimal(countff1).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				sb.setLength(0);
				data.put("日均均值" + keyAvg, avg);
			}

		}

	}

	@PostMapping(value = "/v1/alert/month/setConfig")
	public void setConfig(@RequestBody Map<String, String> config) {
		String type = config.get("type");
		List<Map<String, Object>> list = alertMonthReportSyncDao.queryDaysConfig(type);
		if (null != list && list.size() > 0) {
			alertMonthReportSyncDao.updateDaysConfig(config);
		} else {
			alertMonthReportSyncDao.insertDaysConfig(config);
		}
	}

	private void compareIdcTypeDate(Map<String, AlertMonthReportIdcType> map, List<Map<String, Object>> rateData,
			int flag, Map<String, Integer> sumMap) {
		for (Map<String, Object> ratio : rateData) {
			String idcType = ratio.get("idcType") == null ? "" : ratio.get("idcType").toString();
			String deviceType = ratio.get("deviceType") == null ? "" : ratio.get("deviceType").toString();
			String biz = ratio.get("bizSystem") == null ? "" : ratio.get("bizSystem").toString();
			String key = idcType + "_" + deviceType + "_" + biz;
			int sum = 0;
			if (sumMap.containsKey(key)) {
				sum = sumMap.get(key);
			}

			if (sum == 0) {
				continue;
			}
			int count = ratio.get("count") == null ? 0 : Integer.parseInt(ratio.get("count").toString());
			double ratioTemp = (double) count / sum;
			double ratioVal = new BigDecimal(ratioTemp * 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			if (map.containsKey(key)) {
				AlertMonthReportIdcType idc = map.get(key);
				if (flag == 1) {
					idc.setCpu_fifteen_ratio(ratioVal + "");
				}
				if (flag == 2) {
					idc.setCpu_fourty_ratio(ratioVal + "");
				}
				if (flag == 3) {
					idc.setCpu_eighty_ratio(ratioVal + "");
				}
				if (flag == 4) {
					idc.setCpu_eighty_more_ratio(ratioVal + "");
				}

				if (flag == 5) {
					idc.setMemory_fifteen_ratio(ratioVal + "");
				}
				if (flag == 6) {
					idc.setMemory_fourty_ratio(ratioVal + "");
				}
				if (flag == 7) {
					idc.setMemory_eighty_ratio(ratioVal + "");
				}
				if (flag == 8) {
					idc.setMemory_eighty_more_ratio(ratioVal + "");
				}

			}

		}
	}

	@GetMapping(value = "/v1/alert/month/exportDayExcel")
	public void exportDayExcel(@RequestParam(value = "day", required = true) String day,
			@RequestParam(value = "hisFlag", required = false) int hisFlag) throws Exception {
		logger.info("**exportDayExcel--begin**************");
		alertMonthDayBiz.exportBizSytemDayExcel(day, hisFlag);
		logger.info("******exportDayExcel--end**************");
	}

	@GetMapping(value = "/v1/alert/month/exportIdcTypeIpDayExcel")
	public void exportIdcTypeIpDayExcel(@RequestParam(value = "day", required = true) String day) throws Exception {

		logger.info("**exportIdcTypeIpDayExcel--begin**************");
		alertMonthDayBiz.exportIdcTypeIpDayExcel(day);
		logger.info("******exportIdcTypeIpDayExcel--end**************");
	}

//新的运营月报
	@PostMapping(value = "/v1/alert/month/syncMonthReportIdcTypeData2")
	public void syncMonthReportIdcTypeData2(@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "month", required = false) String month) throws Exception {
		logger.info("synchronization-syncMonthReportIdcTypeData2-begin******************");
		Runnable runnable = new Runnable() {
			public void run() {
				try {
					if(null==type) {
						alertMonthDayBiz.IdcTypeMonthData2(1,month);
						alertMonthDayBiz.IdcTypeMonthData2(2,month);
						alertMonthDayBiz.IdcTypeMonthData2(3,month);
						alertMonthDayBiz.IdcTypeMonthData2(4,month);
						alertMonthDayBiz.IdcTypeMonthData2(5,month);
						alertMonthDayBiz.IdcTypeMonthData2(0,month);
						alertMonthDayBiz.phyMonthData(6,month);
					}else {
						if(type>6) {
							return;
						}
						if(type>5) {
							alertMonthDayBiz.phyMonthData(6,month);
						}else {
							alertMonthDayBiz.IdcTypeMonthData2(type,month);
						}
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		taskExecutor.execute(runnable);
		
		
		logger.info("synchronization-syncMonthReportIdcTypeData2-end*****************");

	}

//@GetMapping(value = "/v1/alert/month/download")
	public void download(@RequestParam(value = "fileName", required = false) String fileName,
			@RequestParam(value = "localPath", required = false) String path,
			@RequestParam(value = "dayPath", required = false) String day) throws Exception {
		if (org.apache.commons.lang.StringUtils.isBlank(path)) {
			path = "d:";
		}
		ftpService.download(fileName, this.ftpFilePath, path + "/" + this.downFilePath, day);
	}
	

	
	//新的运营月报
	@PostMapping(value = "/v1/alert/month/syncMonthDepartment2")
	public void syncMonthDepartment2(@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "month", required = false) String month) throws Exception {
		logger.info("synchronization-syncMonthDepartment2-begin******************");
		Runnable runnable = new Runnable() {
			public void run() {
				try {
					if(null==type) {
						alertMonthDayBiz.syncDepartment2Data(1,month);
						alertMonthDayBiz.syncDepartment2Data(2,month);
						alertMonthDayBiz.syncBizSystem2Data(3,month);
					}else {
						if(type ==3) {
							alertMonthDayBiz.syncBizSystem2Data(3,month);
						}
						if(type ==1|| type ==2) {
							alertMonthDayBiz.syncDepartment2Data(type,month);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		taskExecutor.execute(runnable);
		
		
		 logger.info("synchronization-syncMonthDepartment2-end*****************");
		
	}


	@Autowired
	private CmdbInstanceClient instanceClient;

	@Autowired
	private ZabbixRatioServiceClient zabbixRatioServiceClient;

	@Value("${MoniterServerRatioExportTask.ftpPath}")
	private String ftpPath;

	/**
	 * 告警自动化派单定时任务
	 *
	 * @throws Exception
	 */
	@GetMapping(value = "/v1/alert/syncAlertData")
	public void syncAlertData() throws Exception {
		logger.info("synchronization syncAlertData begin*****************************");
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
		//ueryInstance.put("pageSize", pageSize);
		//queryInstance.setQueryType("normal");
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
			logger.info("syncAlertData导出excel开始*******************************");
			ftpService.uploadtoFTPNew(fileName, in,null);
			logger.info("syncAlertData导出excel结束*******************************");
			ops.flush();
		} catch (Exception e) {
//			logger.error("导出excel失败，失败原因：", e);
			throw new RuntimeException("导出excel失败，失败原因：", e);
		} finally {
			IOUtils.closeQuietly(book);
			IOUtils.closeQuietly(ops);
			IOUtils.closeQuietly(in);
		}

		logger.info("synchronization syncAlertData end*****************************");

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
			for (int i = 1; i <= gageAll;i++) {
				//queryInstance.setPageNumber(i);
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
						.getServerRatioData(deviceType,idcType, ipList);
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

	@Autowired
	private EsCloudSysClient esCloudSysClient;

	@PostMapping(value = "/v1/alert/getPhyData")
	public void getPhyData() throws Exception {
		AlertEsHistoryReq es = new AlertEsHistoryReq();
		es.setSuyanResid("331e300e-a39d-492a-b883-a200d39dd3ab");
		es.setItemListStr("{\"col_time\":\"2020-04-10 14:30:41\",\"cpu_avg_util_percent_percent_avg\":22.24,"
				+ "\"create_time\":\"2020-04-10 14:30:41\",\"mem_mem_total\":503.39,\"mem_mem_used\":495.87,"
				+ "\"mem_percent_usedwobufferscaches\":97.07,\"res_id\":\"0fa7ab1cf418490f84fee42004353b5b\","
				+ "\"res_type\":\"服务器\"}");
		esCloudSysClient.insertCloudPhy(es);

	}

	@PostMapping(value = "/v1/alert/syncSuyanData")
	public void syncSuyanData(@RequestParam(value = "startTimeConfig", required = false) String startTimeConfig,
							  @RequestParam(value = "endTimeConfig", required = false) String endTimeConfig,
							  @RequestParam(value = "deviceType", required = false) String deviceType) throws Exception {

		Runnable runnable = new Runnable() {
			public void run() {
				try {
					getSuyanData(startTimeConfig,endTimeConfig,deviceType);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		taskExecutor.execute(runnable);

	}
	public void getSuyanData(String startTimeConfig,String endTimeConfig,String deviceType) throws Exception {
		logger.info("**getSuyanData-begin*********");
		Date start = DateUtils.parseDate(startTimeConfig, new String[] { "yyyyMMddHHmmss" });
		Date end = DateUtils.parseDate(endTimeConfig, new String[] { "yyyyMMddHHmmss" });
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar calendar = Calendar.getInstance();
		long period = 10*60*1000;
		for(long i=start.getTime();i<end.getTime();i+=period) {
			Date start1 = new Date(i);
			String timeStr = sdf.format(start1);

			calendar.setTime(start1);
			calendar.add(Calendar.MINUTE, 10);
			Date end1 = calendar.getTime();
			cloudSysSyncKafkaTask.getData(timeStr, sdf.format(end1), deviceType, "re2"+deviceType, new Date());
		}
		logger.info("**getSuyanData-end*********");
	}

}
