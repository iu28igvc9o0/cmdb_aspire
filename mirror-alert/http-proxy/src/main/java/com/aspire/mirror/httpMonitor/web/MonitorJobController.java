package com.aspire.mirror.httpMonitor.web;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.aspire.mirror.httpMonitor.common.MonitorConstant;
import com.aspire.mirror.httpMonitor.dao.MonitorHttpConfigDao;
import com.aspire.mirror.httpMonitor.dao.po.MonitorHttpConfig;
import com.aspire.mirror.httpMonitor.dao.po.MonitorHttpHis;
import com.aspire.mirror.httpMonitor.sevice.HttpRequest;
import com.aspire.mirror.httpMonitor.sevice.MonitorJobImpl;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/httpMonitor")
public class MonitorJobController {
    private static final Logger logger = LoggerFactory.getLogger(MonitorJobController.class);
    
    @Autowired
    private Scheduler scheduler;
    
    
    @Autowired
    private MonitorHttpConfigDao monitorHttpConfigDao;
    
    @ApiOperation(value = "新增監控任務", notes = "删除用户")
    @PostMapping(value = "/add")
    @ResponseStatus(code=HttpStatus.NO_CONTENT)
    public void addJob(@RequestParam(value = "configId")String configId) {
    	MonitorHttpConfig mon = monitorHttpConfigDao.selectByPrimaryKey(configId);
    	try {
            String moniName = mon.getMonitor_name();
            String jobName = configId + "::" + moniName;
            removeJob(jobName);
            JobDetail jobDetail = JobBuilder.newJob(MonitorJobImpl.class)
                    .withIdentity(jobName, MonitorConstant.MONITOR_JOB_GROUP_NAME).build();
            jobDetail.getJobDataMap().put("monitorId", mon.getId());

            SimpleTrigger simpleTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(jobName, MonitorConstant.MONITOR_TRIGGER_GROUP_NAME).startAt(new Date())
                    .withSchedule(simpleSchedule().withIntervalInSeconds(mon.getTest_period()*60)
                            .repeatForever())
                    .build();
            scheduler.scheduleJob(jobDetail, simpleTrigger);
            logger.info("scheduleJob"+jobName);
            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @ApiOperation(value = "修改監控任務", notes = "删除用户")
    @PostMapping(value = "/modify")
    @ResponseStatus(code=HttpStatus.NO_CONTENT)
    public void modifyJob(@RequestParam(value = "configId")String configId) {
        try {
        	logger.info("modifyJob"+configId);
            addJob(configId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @ApiOperation(value = "暂停監控任務", notes = "删除用户")
    @PostMapping(value = "/pause")
    @ResponseStatus(code=HttpStatus.NO_CONTENT)
    public void pauseJob(@RequestParam(value = "configId")String configId) throws SchedulerException {
    	MonitorHttpConfig mon = monitorHttpConfigDao.selectByPrimaryKey(configId);
        String moniName = mon.getMonitor_name();
        String jobName = configId + "::" + moniName;
        JobKey jobKey = JobKey.jobKey(jobName, MonitorConstant.MONITOR_JOB_GROUP_NAME);
        scheduler.pauseJob(jobKey);
        logger.info("pauseJob"+jobName);
    }
    
    @ApiOperation(value = "恢复監控任務", notes = "删除用户")
    @PostMapping(value = "/resume")
    @ResponseStatus(code=HttpStatus.NO_CONTENT)
    public void resumeJob(@RequestParam(value = "configId")String configId) throws SchedulerException {
    	MonitorHttpConfig mon = monitorHttpConfigDao.selectByPrimaryKey(configId);
        String moniName = mon.getMonitor_name();
        String jobName = configId + "::" + moniName;
    	 JobKey jobKey = JobKey.jobKey(jobName, MonitorConstant.MONITOR_JOB_GROUP_NAME);
    	 boolean isExist = scheduler.checkExists(jobKey);
    	 if(isExist){
    		 scheduler.resumeJob(jobKey);
    	 }else {
    		 addJob(configId);
    	 }
        logger.info("resumeJob"+jobName);
    }
    
    @ApiOperation(value = "刪除監控任務", notes = "删除用户")
    @PostMapping(value = "/delete")
    @ResponseStatus(code=HttpStatus.NO_CONTENT)
    public void deleteTask(@RequestParam(value = "configId")String configId,@RequestParam(value = "name")String name) {
        try {
            String jobName = configId + "::" + name;
            removeJob(jobName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @ApiOperation(value = "测试任務", notes = "删除用户",  produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/testHttp")
    public String testHttp(@RequestBody MonitorHttpConfig monitor) {
    	Map<String, Object> responseMap = null;
		// MonitorHttpConfig monitor =
		// monitorHttpConfigDao.selectByPrimaryKey(monitorId.toString());
		MonitorHttpHis his = new MonitorHttpHis();
		BeanUtils.copyProperties(monitor, his);
		String conclusion = null;
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
			if ("1".equals(normal)) {// 结果校验
				his.setResponse_code(Integer.parseInt(statusCode));
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
				logger.warn(monitor.getId() + "，响应时间异常：" + timeOut);
			}

			/*
			 * if (conclusion != null &&
			 * conclusion.contains(MonitorConstant.CONCLUSION_ABNORMAL)) { //
			 * monitorWebConfigMapper.updateByPrimaryKeySelective(monitor); return false; }
			 * else { //monitorWebConfigMapper.updateByPrimaryKeySelective(monitor);
			 * 
			 * if (MonitorConstant.CONCLUSION_NORMAL.equals(conclusion)) {// 正常则消除
			 * httpMonitorAlarmEventService.clearHttpAlarmTicket(monitor); } if
			 * (url.contains(MonitorConstant.PROXY_PORT_MONITOR)) { port_flag = "false"; }
			 * 
			 * return true; }
			 */

		}
		return JSON.toJSONString(his);
    }

    
    public void removeJob(String jobName) {
        try {
            JobKey jobKey = JobKey.jobKey(jobName, MonitorConstant.MONITOR_JOB_GROUP_NAME);
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, MonitorConstant.MONITOR_TRIGGER_GROUP_NAME);
            if(null!=triggerKey) {
            	scheduler.pauseTrigger(triggerKey);
                scheduler.unscheduleJob(triggerKey);
                scheduler.deleteJob(jobKey);// 删除任务
                logger.info("resumeJob"+jobName);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
