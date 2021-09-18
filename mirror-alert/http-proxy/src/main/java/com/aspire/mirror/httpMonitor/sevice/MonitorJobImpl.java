package com.aspire.mirror.httpMonitor.sevice;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.httpMonitor.common.MonitorConstant;
import com.aspire.mirror.httpMonitor.dao.MonitorHttpConfigDao;
import com.aspire.mirror.httpMonitor.dao.po.AlertModel;
import com.aspire.mirror.httpMonitor.dao.po.MonitorHttpConfig;
import com.aspire.mirror.httpMonitor.dao.po.MonitorHttpHis;

/**
 * @Description: 任务执行Job类
 *
 * @author xia
 * @date 2017-11-09
 */
public class MonitorJobImpl implements Job {

	// 记录端口是否在检测的标志
	public static String port_flag = "false";
	
	public static String ALERT_SOURCE = "HTTP_MONITOR";

	@Autowired
	private MonitorHttpConfigDao monitorHttpConfigDao;
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	

	@Value("${kafka_http_topic}")
	private String kafkaTopic;
	

	// LoadProperties pro = new LoadProperties();

	private static final Logger LOGGER = LoggerFactory.getLogger(MonitorJobImpl.class);
	

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		//SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		// 获取传过来的对象
		LOGGER.info("---------------execute job" + context.getJobDetail().getKey().getName());
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		Integer monitorId = (Integer) dataMap.get("monitorId");
		
		httpMonitor(monitorId);
	}

	private Boolean httpMonitor(Integer monitorId) {
		Map<String, Object> responseMap = null;
		MonitorHttpConfig monitor = monitorHttpConfigDao.selectByPrimaryKey(monitorId.toString());
		MonitorHttpHis his = new MonitorHttpHis();
		BeanUtils.copyProperties(monitor, his);
		if (null != monitor) {
			String url = monitor.getRequest_http_addr();
			String responseResult = null;
			String headResponse = null;
			String statusCode = null;
			String startTi = null;
			String endTime = null;
			String timeCon = null;
			String timeOut = null;
			String normal = null;
			// String readTimeOut = null;
			HttpRequest httpRequest = new HttpRequest();
			if (monitor.getRequest_method().equals("GET")) {
				responseMap = httpRequest.sendGet(url, monitor.getRequest_parm(), monitor);
			} else {
				responseMap = httpRequest.sendPost(url, monitor.getRequest_parm(), monitor);
			}
			if (null != responseMap) {
				responseResult = (String) responseMap.get("result");
				headResponse = String.valueOf(responseMap.get("responseHead"));
				statusCode = String.valueOf(responseMap.get("statusCode"));
				startTi = String.valueOf(responseMap.get("startTime"));
				endTime = String.valueOf(responseMap.get("endTime"));
				timeCon = String.valueOf(responseMap.get("timeCon"));
				timeOut = String.valueOf(responseMap.get("timeOut"));
				normal = String.valueOf(responseMap.get("normal"));
			}
			String conclusion = null;
			if ("1".equals(normal)) {// 结果校验
				his.setResponse_status_code(statusCode);
				conclusion = httpRequest.ruleCheck(monitor, responseResult, statusCode);
			} else if ("0".equals(normal)) {
				conclusion = String.valueOf(responseMap.get("conclusion"));
			} else if ("2".equals(normal)) {
				conclusion = String.valueOf(responseMap.get("conclusion"));
			} else if ("3".equals(normal)) {
				conclusion = String.valueOf(responseMap.get("conclusion"));
			}
			if(conclusion.contains("正常")) {
				his.setResult(1);
			}else {
				his.setResult(0);
			}
			his.setConclusion(conclusion);
			his.setRequest_result(responseResult);
			his.setHead_response(headResponse);
			if (StringUtils.isNotEmpty(startTi)) {
				his.setStart_time(startTi);
			}
			if (StringUtils.isNotEmpty(endTime)) {
				his.setEnd_time(endTime);
			}
			if (StringUtils.isNotEmpty(timeCon)) {
				his.setTime_con(timeCon);
			}
			if (StringUtils.isNumeric(normal)) {
				his.setNormal(Integer.parseInt(normal));
			}
			if (StringUtils.isNumeric(timeOut)) {
				his.setTime_out(Integer.parseInt(timeOut));
			} else {
				LOGGER.warn(monitor.getId() + "，响应时间异常：" + timeOut);
			}
			//保存监控日志
			his.setHttp_config_id(his.getId());
			his.setId(null);
			
			MonitorHttpHis Latesthis = monitorHttpConfigDao.getLatestHis(monitorId.toString());
			monitorHttpConfigDao.insertHis(his);
			//发送kafka消息
			sendKafka(Latesthis,his,monitor);

			if (conclusion != null && conclusion.contains(MonitorConstant.CONCLUSION_ABNORMAL)) {
				// monitorWebConfigMapper.updateByPrimaryKeySelective(monitor);
				return false;
			} else {
				// monitorWebConfigMapper.updateByPrimaryKeySelective(monitor);
				/*
				 * if (MonitorConstant.CONCLUSION_NORMAL.equals(conclusion)) {// 正常则消除
				 * httpMonitorAlarmEventService.clearHttpAlarmTicket(monitor); } if
				 * (url.contains(MonitorConstant.PROXY_PORT_MONITOR)) { port_flag = "false"; }
				 */
				return true;
			}

		}
		return true;
	}
	
	public void sendKafka(MonitorHttpHis Latesthis,MonitorHttpHis his,MonitorHttpConfig monitor) {
		if(null == Latesthis) {
			if(his.getResult() ==1) {
				return ;
			}
		}else {
			if(Latesthis.getResult() == 1 && his.getResult() ==1) {
				return;
			}	
		}
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy.MM.dd HH:mm:ss" );
		AlertModel model = new AlertModel();
		if(his.getResult() == 0) {//异常
			model.setMoniResult(AlertModel.MONI_RESULT_ACTIVE);
		}else {
			model.setMoniResult(AlertModel.MONI_RESULT_REVOKE);
		}
		model.setSource(ALERT_SOURCE);
		model.setMonitorObject("application");
		model.setMonitorIndex(his.getConclusion());
		model.setAlertLevel(monitor.getAlert_level() + "");
		model.setCurMoniTime(simpleDateFormat.format(new Date()));
		model.setBusinessSystem(monitor.getIdcType());
		model.setZ_itemId(monitor.getId());
		model.setServSystem(monitor.getBiz_system_id());
		model.setObjectType("2");
		// 发送kafka信息
		kafkaTemplate.send(kafkaTopic, JSONObject.toJSONString(model));
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Send kafka ok.topic:{};content:{}",kafkaTopic,JSONObject.toJSONString(model));
			//LOGGER.info("Send kafka ok.content:-----" + JSONObject.toJSONString(model));
		}
	}

	/*
	 * private MonitorHttpHis testHttpMonitor(Integer monitorId, MonitorHttpConfig
	 * monitor) { Map<String, Object> responseMap = null; // MonitorHttpConfig
	 * monitor = // monitorHttpConfigDao.selectByPrimaryKey(monitorId.toString());
	 * MonitorHttpHis his = new MonitorHttpHis(); BeanUtils.copyProperties(monitor,
	 * his); if (null != monitor) { String url = monitor.getRequest_http_addr();
	 * String responseResult = null; String headResponse = null; String statusCode =
	 * null; String startTi = null; String endTime = null; String timeCon = null;
	 * String timeOut = null; String normal = null; // String readTimeOut = null;
	 * HttpRequest httpRequest = new HttpRequest(); if
	 * (monitor.getRequest_method().equals("GET")) { responseMap =
	 * httpRequest.sendGet(url, monitor.getRequest_parm(), monitor); } else {
	 * responseMap = httpRequest.sendPost(url, monitor.getRequest_parm(), monitor);
	 * } if (null != responseMap) { responseResult = (String)
	 * responseMap.get("result"); headResponse =
	 * String.valueOf(responseMap.get("responseHead")); statusCode =
	 * String.valueOf(responseMap.get("statusCode")); startTi =
	 * String.valueOf(responseMap.get("startTime")); endTime =
	 * String.valueOf(responseMap.get("endTime")); timeCon =
	 * String.valueOf(responseMap.get("timeCon")); timeOut =
	 * String.valueOf(responseMap.get("timeOut")); normal =
	 * String.valueOf(responseMap.get("normal")); } String conclusion = null;
	 * his.setResponse_code(Integer.parseInt(statusCode)); if ("1".equals(normal))
	 * {// 结果校验 conclusion = httpRequest.ruleCheck(monitor, responseResult,
	 * statusCode); } else if ("0".equals(normal)) { conclusion =
	 * String.valueOf(responseMap.get("conclusion")); } else if ("2".equals(normal))
	 * { conclusion = String.valueOf(responseMap.get("conclusion")); } else if
	 * ("3".equals(normal)) { conclusion =
	 * String.valueOf(responseMap.get("conclusion")); }
	 * if(conclusion.contains("正常")) { his.setResult(1); }
	 * his.setConclusion(conclusion); his.setRequest_result(responseResult);
	 * his.setHead_response(headResponse); if (StringUtils.isNotEmpty(startTi)) {
	 * his.setStart_time(startTi); } if (StringUtils.isNotEmpty(endTime)) {
	 * his.setEnd_time(endTime); } if (StringUtils.isNotEmpty(timeCon)) {
	 * his.setTime_con(timeCon); } if (StringUtils.isNumeric(normal)) {
	 * his.setNormal(Integer.parseInt(normal)); } if
	 * (StringUtils.isNumeric(timeOut)) {
	 * his.setTime_out(Integer.parseInt(timeOut)); } else {
	 * LOGGER.warn(monitor.getId() + "，响应时间异常：" + timeOut); }
	 * 
	 * 
	 * } return his; }
	 */

}
