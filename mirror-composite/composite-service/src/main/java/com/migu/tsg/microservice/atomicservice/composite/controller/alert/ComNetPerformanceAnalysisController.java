package com.migu.tsg.microservice.atomicservice.composite.controller.alert;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.composite.service.alert.IComNetPerformanceAnalysisService;
import com.aspire.mirror.elasticsearch.api.dto.NetPerformanceAnalysis;
import com.aspire.mirror.elasticsearch.api.dto.NetSetDto;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.instance.payload.CmdbQueryInstance;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.instance.InstanceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.es.NetPerformanceAnalysisClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;

import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

/**
 * 历史告警服务
 * <p>
 * 项目名称: mirror平台 包: com.aspire.mirror.alert.api.metrics 类名称:
 * AlertsHisService.java 类描述: 历史告警服务 创建人: JinSu 创建时间: 2018/9/19 11:19 版本: v1.0
 */
@RestController
public class ComNetPerformanceAnalysisController implements IComNetPerformanceAnalysisService {

	private Logger logger = LoggerFactory.getLogger(ComNetPerformanceAnalysisController.class);

	@Autowired
	private InstanceClient instanceClient;

	@Autowired
	private NetPerformanceAnalysisClient netPerformanceAnalysisClient;

	@Override
	public Result<Map<String, Object>> getNetPerformanceAnalysiseList(@RequestBody CmdbQueryInstance queryInstance,
			@RequestParam(value = "granularity", required = false) String granularity,
			@RequestParam(value = "sendTimeStart", required = false) String sendTimeStart,
			@RequestParam(value = "sendTimeEnd", required = false) String sendTimeEnd) throws ParseException {
		if (queryInstance == null) {
			logger.error("getNetPerformanceAnalysiseList queryInstance is null");
			throw new RuntimeException("getNetPerformanceAnalysiseList queryInstance is null");
		}
		if(queryInstance.getPageSize() == null) {
			logger.error("getNetPerformanceAnalysiseList queryInstance pageSize is null");
			throw new RuntimeException("getNetPerformanceAnalysiseList queryInstance pageSize is null");
		}
		logger.info(JSONObject.fromObject(queryInstance).toString());
		Map<String,Object> mapParams = getMap(queryInstance);
		
		
		Result<Map<String, Object>> list = instanceClient.getInstanceListV3(mapParams,null);
		if (list.getTotalSize() > 0) {
			List<String> ipList = Lists.newArrayList();
			List<Map<String, Object>> mapList = list.getData();
			Map<String, Map<String, Object>> ipMap = new HashMap<>();
			for (Map<String, Object> map : mapList) {
				if(null==map.get("ip")) {
					continue;
				}
				ipList.add(map.get("ip").toString());
				 map.put("cpuAvg", null); map.put("cpuMax", null); map.put("cpuMin", null);
				 map.put("memAvg", null); map.put("memMax", null); map.put("memMin", null);
				ipMap.put(map.get("ip").toString(), map);
			}

			NetSetDto dto = new NetSetDto();
			dto.setEndTime(sendTimeEnd+ " 23:59:59");
			dto.setGranularity(granularity);
			dto.setIdcType(queryInstance.getIdcType());
			dto.setIps(ipList);
			dto.setSize(queryInstance.getPageSize());
			dto.setStartTime(sendTimeStart+ " 00:00:00");
			List<NetPerformanceAnalysis> dataList = netPerformanceAnalysisClient.getData(dto);
			for (NetPerformanceAnalysis net : dataList) {
				String ip = net.getIp();
				if(ipMap.containsKey(ip)) {
					Map<String, Object> d = ipMap.get(ip);
					d.put("cpuAvg", net.getCpuAvg());
					d.put("cpuMax", net.getCpuMax());
					d.put("cpuMin", net.getCpuMin());
					d.put("memAvg", net.getMemAvg());
					d.put("memMax", net.getMemMax());
					d.put("memMin", net.getMemMin());
				}
			}
			List<Map<String, Object>> valuelist = new ArrayList<>(ipMap.values());
			list.setData(valuelist);
		}

		return list;
	}
	
	private Map<String, Object> getMap(CmdbQueryInstance queryInstance) {
		Map<String, Object> map = Maps.newHashMap();
		map.put("currentPage", queryInstance.getPageNumber());
		map.put("pageSize", queryInstance.getPageSize());
		map.put("department1", queryInstance.getDepartment1());
		map.put("department2", queryInstance.getDepartment2());
		map.put("bizSystem", queryInstance.getBizSystem());
		map.put("ip", queryInstance.getIp());
		map.put("device_name", queryInstance.getDeviceName());
		map.put("idcType", queryInstance.getIdcType());
		map.put("pod_name", queryInstance.getPod());
		map.put("roomId", queryInstance.getRoom());
		map.put("device_type", queryInstance.getDeviceType());
		map.put("device_model", queryInstance.getDeviceModel());
		map.put("device_class", queryInstance.getDeviceClass());
		map.put("device_class_3", queryInstance.getDeviceClass3());
		map.put("device_sn", queryInstance.getDeviceSN());
		map.put("project_name", queryInstance.getDeviceProject());
		map.put("device_status", queryInstance.getDeviceStatus());
		map.put("mainten_factory", queryInstance.getMaintenanceFactory());
		map.put("device_mfrs", queryInstance.getMfrFactory());
		if(StringUtils.isNotBlank(queryInstance.getStartInsertTime())) {
			map.put("online_time", new String[] {queryInstance.getStartInsertTime(),queryInstance.getEndInsertTime()});
		}
		map.put("dept_operation", queryInstance.getDeptOperation());
		map.put("ops", queryInstance.getOps());
		logger.info(JSONObject.fromObject(map).toString());
		return map;
	}

	@Override
	public void exportNetPerformanceAnalysiseList(@RequestBody CmdbQueryInstance queryInstance,
			@RequestParam(value = "granularity", required = false) String granularity,
			@RequestParam(value = "sendTimeStart", required = false) String sendTimeStart,
			@RequestParam(value = "sendTimeEnd", required = false) String sendTimeEnd, HttpServletResponse response) throws Exception {
		if (queryInstance == null) {
			logger.error("getNetPerformanceAnalysiseList queryInstance is null");
			throw new RuntimeException("getNetPerformanceAnalysiseList queryInstance is null");
		}
		logger.info("[getEsData]dataSetsDto is {}.", JSONUtils.valueToString(queryInstance));
		
		Map<String,Object> mapParams = getMap(queryInstance);
		
		Result<Map<String, Object>> list = instanceClient.getInstanceListV3(mapParams,null);
		List<Map<String, Object>> valuelist = Lists.newArrayList();
		if (list.getTotalSize() > 0) {
			List<String> ipList = Lists.newArrayList();
			List<Map<String, Object>> mapList = list.getData();
			Map<String, Map<String, Object>> ipMap = new HashMap<>();
			for (Map<String, Object> map : mapList) {
				ipList.add(map.get("ip").toString());
				
				 map.put("cpuAvg", null); map.put("cpuMax", null); map.put("cpuMin", null);
				 map.put("memAvg", null); map.put("memMax", null); map.put("memMin", null);
				 
				ipMap.put(map.get("ip").toString(), map);
			}

			NetSetDto dto = new NetSetDto();
			dto.setEndTime(sendTimeEnd+ " 23:59:59");
			dto.setGranularity(granularity);
			dto.setIdcType(queryInstance.getIdcType());
			dto.setIps(ipList);
			dto.setSize(queryInstance.getPageSize());
			dto.setStartTime(sendTimeStart+ " 00:00:00");
			List<NetPerformanceAnalysis> dataList = netPerformanceAnalysisClient.getData(dto);
			for (NetPerformanceAnalysis net : dataList) {
				String ip = net.getIp();
				if(ipMap.containsKey(ip)) {
					Map<String, Object> d = ipMap.get(ip);
					d.put("cpuAvg", net.getCpuAvg());
					d.put("cpuMax", net.getCpuMax());
					d.put("cpuMin", net.getCpuMin());
					d.put("memAvg", net.getMemAvg());
					d.put("memMax", net.getMemMax());
					d.put("memMin", net.getMemMin());
				}
			}
			 valuelist = new ArrayList<>(ipMap.values());
		}	
		String[] headerList = { "管理ip", "设备名称", "设备类型", "机房位置", "POD池名称", "CPU最大利用率 （%）", "CPU平均利用率 （%）"
				, "CPU最小利用率 （%）", "内存最大利用率 （%）", "内存平均利用率 （%）", "内存最小利用率 （%）"};
		String[] keyList = { "ip", "device_name", "device_type", "roomId", "pod_name", "cpuMax",
				"cpuAvg", "cpuMin" , "memMax", "memAvg", "memMin"};
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String title = "利用率导出列表_" + sDateFormat.format(new Date());
		String fileName = title + ".xlsx";
		
		
		OutputStream os = response.getOutputStream();// 取得输出流
		response.setHeader("Content-Disposition",
				"attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
		response.setHeader("Connection", "close");
		response.setHeader("Content-Type", "application/vnd.ms-excel");
		// excel constuct
		ExportExcelUtil eeu = new ExportExcelUtil();
		Workbook book = new SXSSFWorkbook(128);
		eeu.exportExcel(book, 0, title, headerList, valuelist, keyList);
		book.write(os);
		   
	}

	@Override
	public Map<String, Object> getNetPerformanceAnalysiseTrends(@RequestParam(value = "ip") String ip,
			@RequestParam(value = "granularity", required = false) String granularity,
			@RequestParam(value = "sendTimeStart", required = false) String sendTimeStart,
			@RequestParam(value = "sendTimeEnd", required = false) String sendTimeEnd,
			@RequestParam(value = "idcType", required = false) String idcType
			,@RequestParam(value = "monitorFlag", required = false) String monitorFlag) throws ParseException {

		   Map<String, Object> map = netPerformanceAnalysisClient.getTrendsData(sendTimeStart + " 00:00:00"
				   , sendTimeEnd + " 23:59:59", ip, granularity, monitorFlag, idcType);
		return map;
	}
	
	@Override
	public void exportNetPerformanceAnalysiseTrends(@RequestParam(value = "ip") String ip,
			@RequestParam(value = "granularity", required = false) String granularity,
			@RequestParam(value = "sendTimeStart", required = false) String sendTimeStart,
			@RequestParam(value = "sendTimeEnd", required = false) String sendTimeEnd,
			@RequestParam(value = "idcType", required = false) String idcType
			,@RequestParam(value = "monitorFlag", required = false) String monitorFlag, HttpServletResponse response) throws Exception {

		String[] headerList = { "时间", "最大利用率(%)", "平均利用率(%)", "最小利用率(%)"};
	
		String[] keyList = { "time", "max", "avg", "min"};
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String title = "利用率导出列表_" + sDateFormat.format(new Date());
		String fileName = title + ".xlsx";
		Map<String, Object> map = netPerformanceAnalysisClient.getTrendsData(sendTimeStart + " 00:00:00"
				   , sendTimeEnd + " 23:59:59", ip, granularity, monitorFlag, idcType);
		List<Map<String, Object>> list = Lists.newArrayList();
		if(null!=map) {
			List<String> timeList = (List<String>)map.get("xAxis");
			Map<String, List<Object>> seriesMap = (Map<String, List<Object>>)map.get("series");
			List<Object> maxList = (List<Object>)seriesMap.get("max");
			List<Object> avgList = (List<Object>)seriesMap.get("avg");
			List<Object> minList = (List<Object>)seriesMap.get("min");
			for(int i=0;i<timeList.size();i++) {
				Map<String, Object> val = new HashMap<>();
				Object max = maxList.get(i);
				Object avg = avgList.get(i);
				Object min = minList.get(i);
				/*
				 * if(null !=max) { String max1= max.toString(); if(""!=max1) { double max2 =
				 * Double.valueOf(max1); max = (double)Math.round(max2*100)/100; } } if(null
				 * !=avg) { String avg1= max.toString(); if(""!=avg1) { double avg2 =
				 * Double.valueOf(avg1); avg = (double)Math.round(avg2*100)/100; } } if(null
				 * !=min) { String min1= max.toString(); if(""!=min1) { double min2 =
				 * Double.valueOf(min1); min = (double)Math.round(min2*100)/100; } }
				 */
				val.put("time", timeList.get(i));
				val.put("max", max);
				val.put("min", min);
				val.put("avg", avg);
				list.add(val);
			}
		}
		OutputStream os = response.getOutputStream();// 取得输出流
		response.setHeader("Content-Disposition",
				"attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
		response.setHeader("Connection", "close");
		response.setHeader("Content-Type", "application/vnd.ms-excel");
		// excel constuct
		ExportExcelUtil eeu = new ExportExcelUtil();
		Workbook book = new SXSSFWorkbook(128);
		eeu.exportExcel(book, 0, title, headerList, list, keyList);
		book.write(os);
		   
	}
}
