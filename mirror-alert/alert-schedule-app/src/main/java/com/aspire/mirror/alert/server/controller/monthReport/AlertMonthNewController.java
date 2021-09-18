package com.aspire.mirror.alert.server.controller.monthReport;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.alert.server.biz.monthReport.AlertMonthDayBiz;
import com.aspire.mirror.alert.server.biz.monthReport.AlertPoorEfficiencyDeviceBiz;
import com.aspire.mirror.alert.server.clientservice.CmdbClient;
import com.aspire.mirror.alert.server.clientservice.payload.ConfigDict;
import com.aspire.mirror.alert.server.constant.AlertCommonConstant;
import com.aspire.mirror.alert.server.vo.monthReport.AlertMonthReportVo;
import com.aspire.mirror.alert.server.util.DateUtils;
import com.aspire.mirror.alert.server.biz.model.AlertFieldBiz;
import com.aspire.mirror.alert.server.biz.helper.AlertsHandleV2Helper;
import com.aspire.mirror.alert.server.constant.AlertConfigConstants;
import com.aspire.mirror.alert.server.vo.model.AlertFieldVo;
import com.aspire.mirror.alert.server.vo.alert.AlertsV2Vo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class AlertMonthNewController {

    private static final Logger logger = LoggerFactory.getLogger(AlertMonthNewController.class);

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;
	
	@Autowired
	private AlertMonthDayBiz alertMonthDayBiz;
	
	@Autowired
	private CmdbClient cmdbClient;

	@Value("${AlertMonitorScanDayTask.scanDeviceMonitorDataFlag:true}")
	private boolean scanDeviceMonitorDataFlag;
	@Autowired
	private AlertsHandleV2Helper alertsHandleV2Helper;
	
	@Autowired
	private AlertPoorEfficiencyDeviceBiz alertPoorEfficiencyDeviceBiz;
	
	//按天从es查业务系统均峰值数据存库
	 @PostMapping(value = "/v1/alert/month/getDataNew")
	public void getData(@RequestBody AlertMonthReportVo monthReportRequest) throws Exception {
		 logger.info("*********getData--begin*********");
			Runnable runnable = new Runnable() {
				public void run() {
					 try {
						 alertMonthDayBiz.syncBizSytemDayNew(monthReportRequest);
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
				}
			};
			taskExecutor.execute(runnable);
					 logger.info("*********getData--end********");
	}
	 
	//按天从es查资源池均峰值数据存库
	 @PostMapping(value = "/v1/alert/month/getIdcTypeData")
		public void getIdcTypeData(@RequestBody AlertMonthReportVo monthReportRequest) throws Exception {
			 logger.info("*********getIdcTypeData--begin*********");
				Runnable runnable = new Runnable() {
					public void run() {
						 try {
							 alertMonthDayBiz.syncIdcTypeDayNew(monthReportRequest);
					        } catch (Exception e) {
					            e.printStackTrace();
					        }
					}
				};
				taskExecutor.execute(runnable);
						 logger.info("*********getIdcTypeData--end********");
		}
	 
	 
	/*
	 * @PostMapping(value = "/v1/alert/month/getIdcTypeIpDataNew") public void
	 * getIdcTypeIpData(@RequestBody AlertMonthReportRequest monthReportRequest)
	 * throws Exception { logger.info("*********getData--begin*********"); Runnable
	 * runnable = new Runnable() { public void run() { try {
	 * alertMonthDayBiz.syncIdcTypeIpDayNew(monthReportRequest); } catch (Exception
	 * e) { e.printStackTrace(); } } }; taskExecutor.execute(runnable);
	 * logger.info("*********getData--end********"); }
	 */
	 
	 @PostMapping(value = "/v1/alert/month/exportTemp")
		public void exportTemp(@RequestParam(value="month",required=true)String month,
				@RequestParam(value="idcType",required=false)String idcType,@RequestParam(value="hisFlag",required=true)int hisFlag) throws Exception {
			alertMonthDayBiz.getData(month, idcType, hisFlag);
		}
	
	 /**
	  * 单个设备性能缺失告警
	  * @param idcType
	  * @param date
	  * @throws Exception
	  */
	 @PostMapping(value = "/v1/alert/day/scanDeviceMonitorData") 
	 public void scanDeviceMonitorData(@RequestParam(value="idcType",required=false)String idcType
			 ,@RequestParam(value="date",required=false)String date) throws Exception{
		
		  if(!scanDeviceMonitorDataFlag) { return; }
		 
		 List<ConfigDict> idcTypeList = cmdbClient.getDictsByType("idcType", null, null, null);
		 for(ConfigDict c:idcTypeList) {
			 if(StringUtils.isBlank(idcType)) {
				 alertMonthDayBiz.scanDeviceMonitorData(c.getId(),c.getValue(),date);
			 }else {
				 if(idcType.equals(c.getValue())) {
					 alertMonthDayBiz.scanDeviceMonitorData(c.getId(),idcType,date);
					 break;
				 }
				 
			 }
			
		 }
	 }
	 /**
	  * 资源池的设备性能分布
	  * @param idcType
	  * @param date
	  * @throws Exception
	  */
	 @PostMapping(value = "/v1/alert/day/scanIdcTypePerformanceData") 
	 public void scanIdcTypePerformanceData(@RequestParam(value="idcType",required=false)String idcType
			 ,@RequestParam(value="date",required=false)String date) throws Exception{
		 Runnable runnable = new Runnable() {
				public void run() {
					 try {
						 List<ConfigDict> idcTypeList = cmdbClient.getDictsByType("idcType", null, null, null);
						 if (StringUtils.isNotBlank(date)) {
							String[] dates = date.split(",");
							for (String d : dates) {
								if (StringUtils.isNotBlank(idcType) && idcType.equals("all")) {
									alertMonthDayBiz.scanIdcTypePerformanceData("all", null, d);
									continue;
								}
								for (ConfigDict c : idcTypeList) {
									if (StringUtils.isBlank(idcType)) {
										alertMonthDayBiz.scanIdcTypePerformanceData(c.getId(), c.getValue(), d);
									} else {
										if (idcType.equals(c.getValue())) {
											alertMonthDayBiz.scanIdcTypePerformanceData(c.getId(), idcType, d);
											break;
										}

									}

								}
							}
						} else {
							if (StringUtils.isNotBlank(idcType) && idcType.equals("all")) {
								alertMonthDayBiz.scanIdcTypePerformanceData("all", null, date);
								return;
							}
							for (ConfigDict c : idcTypeList) {
								if (StringUtils.isBlank(idcType)) {
									alertMonthDayBiz.scanIdcTypePerformanceData(c.getId(), c.getValue(), date);
								} else {

									if (idcType.equals(c.getValue())) {
										alertMonthDayBiz.scanIdcTypePerformanceData(c.getId(), idcType, date);
										break;
									}

								}

							}
						}
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
				}
			};
			taskExecutor.execute(runnable);
		
		 
		
	 }
    
	 @PostMapping(value = "/v1/alert/day/queryPoorEfficiencyDeviceMonth") 
	 public void queryPoorEfficiencyDeviceMonth(@RequestParam(value="date",required=false) String curDate
			 ,@RequestParam(value="item",required=true) String item) throws Exception{
		 if(StringUtils.isNotEmpty(curDate)) {
			 Date curDateVal = DateUtils.stringToDate(DateUtils.DEFAULT_DATETIME_FMT, curDate);
			 alertPoorEfficiencyDeviceBiz.queryMonthData(curDateVal, item);
		 }else {
			 alertPoorEfficiencyDeviceBiz.getPoorEfficiencyDeviceMonthData(item,0);
		 }
		 
	 }
	 
	 @PostMapping(value = "/v1/alert/day/queryPoorEfficiencyDeviceWeek") 
	 public void queryPoorEfficiencyDeviceWeek(@RequestParam(value="date",required=false) String curDate
			 ,@RequestParam(value="item",required=true) String item) throws Exception{
		 if(StringUtils.isNotEmpty(curDate)) {
			 Date curDateVal = DateUtils.stringToDate(DateUtils.DEFAULT_DATETIME_FMT, curDate);
			 alertPoorEfficiencyDeviceBiz.queryWeekData(curDateVal, item);
		 }else {
			 alertPoorEfficiencyDeviceBiz.getPoorEfficiencyDeviceWeekData(item,0);
		 }
	 }
	 

	    @Autowired
	    private AlertFieldBiz alertFieldBiz;
	// @PostMapping(value = "/v1/alert/day/TestAlertCabinetColumnTask") 
	 public void TestAlertCabinetColumnTask() throws Exception {
		 Date date = new Date();
		 AlertsV2Vo dto = new AlertsV2Vo();
		 dto.setIdcType("信息港资源池");
		 dto.setObjectType(3+"");
		 dto.setAlertLevel(3+"");
			dto.setSource("Prometheus");
			dto.setAlertType(AlertsV2Vo.ALERT_REVOKE);
			dto.setMoniObject(AlertCommonConstant.CABINET_COLUMN_MONIOBJECT);
			dto.setCurMoniTime(date);
			dto.setAlertStartTime(date);
			dto.setBizSys("集中化合同管理");
			dto.setMoniIndex("aa");
			dto.setDeviceIp("192.168.77.67");
			dto.setKeyComment("设备电源故障");
		 ObjectMapper objectMapper = new ObjectMapper();
			String jsonString = "{}";
			try {
				jsonString = objectMapper.writeValueAsString(dto);
			} catch (JsonProcessingException e) {
			}
			JSONObject alertJson = JSONObject.parseObject(jsonString);
			List<AlertFieldVo> alertFieldList = alertFieldBiz.getModelField(AlertConfigConstants.REDIS_MODEL_ALERT);
		 alertsHandleV2Helper.handleAlert(dto);
	 }

}
