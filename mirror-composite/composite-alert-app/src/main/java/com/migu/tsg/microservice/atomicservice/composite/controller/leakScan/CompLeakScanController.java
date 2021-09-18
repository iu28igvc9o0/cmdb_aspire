package com.migu.tsg.microservice.atomicservice.composite.controller.leakScan;

import com.alibaba.fastjson.JSON;
import com.aspire.mirror.alert.api.dto.leakScan.*;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.leakScan.*;
import com.aspire.mirror.composite.service.leakScan.ICompSecurityScanService;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.leakScan.SecurityLeakScanServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * 告警控制层
 * <p>
 * 项目名称: mirror平台 包:
 * com.migu.tsg.microservice.atomicservice.composite.controller.alert 类名称:
 * CompLeakScanController.java 类描述: 告警控制层 创建人: Liangjun 创建时间: 2019/7/20 17:28
 * 版本: v1.0
 */
@RestController
public class CompLeakScanController implements ICompSecurityScanService {

	private static final Logger logger = LoggerFactory.getLogger(CompLeakScanController.class);
//	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	@Autowired
	private SecurityLeakScanServiceClient securityLeakScanServiceClient;

	@Override
	public PageResponse<CompLeakScanSummaryResponse> leakScanSummary(
			@RequestBody CompLeakScanSummaryRequest compRequest) {
		LeakScanSummaryRequest request = new LeakScanSummaryRequest();
		if (compRequest != null) {
			BeanUtils.copyProperties(compRequest, request);
		}
		PageResponse<LeakScanSummaryResponse> result = securityLeakScanServiceClient.leakScanSummary(request);
		PageResponse<CompLeakScanSummaryResponse> response = new PageResponse<>();
		response.setCount(result.getCount());
		List<CompLeakScanSummaryResponse> list = Lists.newArrayList();
		for (LeakScanSummaryResponse summaryResponse : result.getResult()) {
			CompLeakScanSummaryResponse compLeakScanSummaryResponse = new CompLeakScanSummaryResponse();
			BeanUtils.copyProperties(summaryResponse, compLeakScanSummaryResponse);
			list.add(compLeakScanSummaryResponse);
		}
		response.setResult(list);
		return response;
	}

	@Override
	public void export(CompLeakScanSummaryRequest compRequest, HttpServletResponse response) {
		LeakScanSummaryRequest request = new LeakScanSummaryRequest();
		if (compRequest != null) {
			BeanUtils.copyProperties(compRequest, request);
		}
		String[] headerList = { "一级部门", "二级部门", "归属业务线", "扫描日期", "高危漏洞数量", "中危漏洞数量", "低危漏洞数量", "文件名", "流程工单号", "修复状态" };
		String[] keyList = { "department1", "department2", "bizLine", "scanDate", "highLeaks", "mediumLeaks",
				"lowLeaks", "reportFileName", "bpmId", "repairStat" };
		OutputStream os = null;
		try {
			List<Map<String, Object>> dataLists = securityLeakScanServiceClient.export(request);
			for (Map<String, Object> record : dataLists) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				record.put("repairStat", Integer.valueOf(record.get("repairStat") + "") == 1 ? "已修复" : "未修复");
				record.put("scanDate", sdf.format(record.get("scanDate")));
				if (record.get("bpmId") == null) {
					record.put("bpmId", "");
				}
			}
			logger.info("Leak Scan Excel Content: {}", JSON.toJSONString(dataLists));
			String title = "安全漏洞扫描汇总";
			String fileName = title + ".xlsx";

			os = response.getOutputStream();// 取得输出流
			response.setHeader("Content-Disposition",
					"attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
			response.setHeader("Connection", "close");
			response.setContentType("application/vnd.ms-excel");
			// excel constuct
			ExportExcelUtil eeu = new ExportExcelUtil();
			Workbook book = new SXSSFWorkbook(128);
			eeu.exportExcel(book, 0, title, headerList, dataLists, keyList);
			book.write(os);
			os.flush();
			logger.info("导出/生成文件: {} 成功!", fileName);
		} catch (Exception e) {
			logger.error("导出Excel数据失败!", e);
		} finally {
			IOUtils.closeQuietly(os);
		}
	}

	@Override
	public PageResponse<CompLeakScanReportResponse> leakScanReports(
			@RequestBody CompLeakScanReportRequest compRequest) {
		LeakScanReportRequest request = new LeakScanReportRequest();
		if (compRequest != null) {
			BeanUtils.copyProperties(compRequest, request);
		}
		PageResponse<LeakScanReportResponse> result = securityLeakScanServiceClient.leakScanReports(request);
		PageResponse<CompLeakScanReportResponse> response = new PageResponse<>();
		List<CompLeakScanReportResponse> list = Lists.newArrayList();
		response.setCount(result.getCount());
		for (LeakScanReportResponse reportResponse : result.getResult()) {
			CompLeakScanReportResponse compLeakScanReportResponse = new CompLeakScanReportResponse();
			BeanUtils.copyProperties(reportResponse, compLeakScanReportResponse);
			list.add(compLeakScanReportResponse);
		}
		response.setResult(list);
		return response;
	}

	@Override
	public CompLeakScanRecordResponse getLeakScanRecord(@PathVariable(value = "id") String id) {
		LeakScanRecordResponse result = securityLeakScanServiceClient.getLeakScanRecord(id);
		CompLeakScanRecordResponse response = new CompLeakScanRecordResponse();
		if (result != null) {
			BeanUtils.copyProperties(result, response);
		}
		return response;
	}

	@Override
	public void modifyBpmRepairStat(@PathVariable(value = "id") String id) {
		securityLeakScanServiceClient.modifyBpmRepairStat(id);
	}

	@Override
	public PageResponse<CompLeakScanSummaryResponse> leakScanDetailByDate(
			@RequestBody CompLeakScanSummaryRequest compRequest) {
		LeakScanSummaryRequest request = new LeakScanSummaryRequest();
		if (compRequest != null) {
			BeanUtils.copyProperties(compRequest, request);
		}
		PageResponse<LeakScanSummaryResponse> result = securityLeakScanServiceClient.leakScanDetailByDate(request);
		PageResponse<CompLeakScanSummaryResponse> response = new PageResponse<>();
		response.setCount(result.getCount());
		List<CompLeakScanSummaryResponse> list = Lists.newArrayList();
		for (LeakScanSummaryResponse summaryResponse : result.getResult()) {
			CompLeakScanSummaryResponse compLeakScanSummaryResponse = new CompLeakScanSummaryResponse();
			BeanUtils.copyProperties(summaryResponse, compLeakScanSummaryResponse);
			list.add(compLeakScanSummaryResponse);
		}
		response.setResult(list);
		return response;
	}

	@Override
	public void exportDetail(@RequestBody CompLeakScanSummaryRequest compRequest, HttpServletResponse response) {
		LeakScanSummaryRequest request = new LeakScanSummaryRequest();
		if (compRequest != null) {
			BeanUtils.copyProperties(compRequest, request);
		}
		String[] headerList = { "归属业务线", "扫描日期", "高危漏洞数量", "中危漏洞数量", "低危漏洞数量", "文件名", "流程工单号", "修复状态" };
		String[] keyList = { "bizLine", "scanDate", "highLeaks", "mediumLeaks", "lowLeaks", "reportFileName", "bpmId",
				"repairStat" };
		OutputStream os = null;
		try {
			List<Map<String, Object>> dataLists = securityLeakScanServiceClient.export(request);
			for (Map<String, Object> record : dataLists) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				record.put("repairStat", Integer.valueOf(record.get("repairStat") + "") == 1 ? "已修复" : "未修复");
				record.put("scanDate", sdf.format(record.get("scanDate")));
				if (record.get("bpmId") == null) {
					record.put("bpmId", "");
				}
			}
			logger.info("Leak Scan Excel Content: {}", JSON.toJSONString(dataLists));
			String title = "安全漏洞扫描汇总";
			String fileName = title + ".xlsx";

			os = response.getOutputStream();// 取得输出流
			response.setHeader("Content-Disposition",
					"attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
			response.setHeader("Connection", "close");
			response.setContentType("application/vnd.ms-excel");
			// excel constuct
			ExportExcelUtil eeu = new ExportExcelUtil();
			Workbook book = new SXSSFWorkbook(128);
			eeu.exportExcel(book, 0, title, headerList, dataLists, keyList);
			book.write(os);
			os.flush();
			logger.info("导出/生成文件: {} 成功!", fileName);
		} catch (Exception e) {
			logger.error("导出Excel数据失败!", e);
		} finally {
			IOUtils.closeQuietly(os);
		}

	}

	@Override
	public Map<String, Object> leaksRankDistribute(@RequestParam(value = "beginDate", required = false) String beginDate,
												   @RequestParam(value = "endDate", required = false) String endDate) throws IllegalAccessException {
		return securityLeakScanServiceClient.leaksRankDistribute(beginDate, endDate);
	}

	@Override
	public PageResponse<Map<String, Object>> leakStatByBiz(@RequestParam(value = "beginDate", required = false) String beginDate,
												   @RequestParam(value = "endDate", required = false) String endDate) throws IllegalAccessException {
		return securityLeakScanServiceClient.leakStatByBiz(beginDate, endDate);
	}

	@Override
	public PageResponse<Map<String, Object>> leakStatListByBiz(@RequestParam("beginDate") String beginDate,
			@RequestParam("endDate") String endDate,
			@RequestParam(value = "rankType", required = false) String rankType
			,@RequestParam(value="begin",required=false) Integer begin
            ,@RequestParam(value="pageSize",required=false)Integer pageSize) throws IllegalAccessException {
		return securityLeakScanServiceClient.leakStatListByBiz(beginDate, endDate, rankType,begin,pageSize);
	}

	@Override
	public PageResponse<Map<String, Object>> leakTrend(@RequestParam(value = "beginDate",required = false) String beginDate,
                                               @RequestParam(value = "endDate",required = false) String endDate
                                               ,@RequestParam(value="begin",required=false) Integer begin
                                               ,@RequestParam(value="pageSize",required=false)Integer pageSize) throws IllegalAccessException {
		return securityLeakScanServiceClient.leakTrend(beginDate, endDate,begin,pageSize);
	}

	@Override
	public Map<String, Object> leakSummary(@RequestParam(value = "beginDate",required = false) String beginDate,
										   @RequestParam(value = "endDate",required = false) String endDate) throws IllegalAccessException {
		return securityLeakScanServiceClient.leakSummary(beginDate, endDate);
	}

	@Override
	public void exportScanResult(@RequestParam(value = "beginDate",required = false) String beginDate
			,@RequestParam(value = "endDate",required = false)  String endDate, HttpServletResponse response) {

		String[] headerList = { "扫描时间", "业务系统数", "漏洞总数", "已修复漏洞数"};
		String[]keyList = { "scan_date", "bizCount", "count", "rCount"};
		OutputStream os = null;
		try {
			List<Map<String, Object>> dataLists = securityLeakScanServiceClient.leakTrend(beginDate, endDate,null,null).getResult();
			logger.info("Leak Scan Excel Content: {}", JSON.toJSONString(dataLists));
			String title = "安全扫描结果明细";
			String fileName = title + ".xlsx";

			os = response.getOutputStream();// 取得输出流
			response.setHeader("Content-Disposition",
					"attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
			response.setHeader("Connection", "close");
			response.setContentType("application/vnd.ms-excel");
			// excel constuct
			ExportExcelUtil eeu = new ExportExcelUtil();
			Workbook book = new SXSSFWorkbook(128);
			eeu.exportExcel(book, 0, title, headerList, dataLists, keyList);
			book.write(os);
			os.flush();
			logger.info("导出/生成文件: {} 成功!", fileName);
		} catch (Exception e) {
			logger.error("导出Excel数据失败!", e);
		} finally {
			IOUtils.closeQuietly(os);
		}

	
	}

	@Override
	public void exportleakStatListByBizResult(String beginDate, String endDate, String rankType, HttpServletResponse response) {

		String[] headerList = {"业务系统", "漏洞数", "已修复漏洞"};
		String[] keyList = { "biz_line", "count", "rCount" };
		OutputStream os = null;
		try {
			List<Map<String, Object>> dataLists = securityLeakScanServiceClient.leakStatListByBiz(beginDate, endDate, rankType,null,null).getResult();
			logger.info("Leak Scan Excel Content: {}", JSON.toJSONString(dataLists));
			String title = "业务系统漏洞数汇总";
			String fileName = title + ".xlsx";

			os = response.getOutputStream();// 取得输出流
			response.setHeader("Content-Disposition",
					"attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
			response.setHeader("Connection", "close");
			response.setContentType("application/vnd.ms-excel");
			// excel constuct
			ExportExcelUtil eeu = new ExportExcelUtil();
			Workbook book = new SXSSFWorkbook(128);
			eeu.exportExcel(book, 0, title, headerList, dataLists, keyList);
			book.write(os);
			os.flush();
			logger.info("导出/生成文件: {} 成功!", fileName);
		} catch (Exception e) {
			logger.error("导出Excel数据失败!", e);
		} finally {
			IOUtils.closeQuietly(os);
		}

	
	}

	@Override
	public List<Map<String, Object>> getLeakByBizSystem() {
		return securityLeakScanServiceClient.getLeakByBizSystem();
	}

	@Override
	public List<Map<String, Object>> getLeakByIp() {
		return securityLeakScanServiceClient.getLeakByIp();
	}

	@Override
	public List<Map<String,Object>> getLeakByIdcType() {
		return securityLeakScanServiceClient.getLeakByIdcType();
	}
}
