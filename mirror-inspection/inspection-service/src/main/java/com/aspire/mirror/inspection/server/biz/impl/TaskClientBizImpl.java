package com.aspire.mirror.inspection.server.biz.impl;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.inspection.api.dto.constant.TaskCycle;
import com.aspire.mirror.inspection.api.dto.constant.TaskStatus;
import com.aspire.mirror.inspection.server.biz.TaskClientBiz;
import com.aspire.mirror.inspection.server.dao.TaskDao;
import com.aspire.mirror.inspection.server.dao.po.Task;
import com.aspire.mirror.inspection.server.util.CronUtils;
import com.github.ltsopensource.core.domain.Job;
import com.github.ltsopensource.core.support.CronExpressionUtils;
import com.github.ltsopensource.jobclient.JobClient;
import com.github.ltsopensource.jobclient.domain.Response;
import com.google.common.collect.Lists;

/** 
* @author ZhangSheng 
* @version 2018年8月12日 下午7:57:01 
* @describe 
*/
@Service
@SuppressWarnings("rawtypes")
public class TaskClientBizImpl implements TaskClientBiz{
	public static final String INSPECTION_TASK_NAME = "inspection_task";
	
	@Autowired
	private JobClient jobClient;
	
	@Autowired
	private TaskDao taskDao;

	@Override
	public Response  addScheduling(String taskId) {
		Task  task =taskDao.selectByPrimaryKey(taskId);
		Response response = null;
		if(null!=task) {
			Job job = new Job();
			job.setTaskId(task.getTaskId());
			job.setParam("task_identity", INSPECTION_TASK_NAME);
			job.setParam("task", JSONObject.toJSONString(task));
			job.setTaskTrackerNodeGroup("mirror_taskTracker");
			//job.setRepeatCount();
			if(task.getCycle().equals(TaskCycle.CUSTOM.getValue())) {//自定义任务
				String cron=task.getExecTime();
				if(CronExpressionUtils.isValidExpression(cron)) {
					job.setCronExpression(cron);
				}
			}else {//其他周期类型
				String cron =CronUtils.getCorn(task.getCycle(),task.getExecTime());
				if(CronExpressionUtils.isValidExpression(cron)) {
					job.setCronExpression(cron);
				}
			}	
			job.setRelyOnPrevCycle(true);
			job.setNeedFeedback(true);
			job.setReplaceOnExist(true);//当任务队列中存在这个任务的时候，是否替换更新
			response = jobClient.submitJob(job);
			updateNo(taskId);
		}	
		return response;
	}

	@Override
	public List<String> getTaskNames(Response response){
		List<String> taskNames =Lists.newArrayList();
		List<Job> list =response.getFailedJobs();
		if(!list.isEmpty()) {
			for (Job job :list) {
				 String taskStr=job.getParam("task");
				 if(StringUtils.isNotBlank(taskStr)) {
					 Task task =(Task)JSONObject.parse(job.getParam("task"));
					 taskNames.add(task.getName());
				 }
			 }
		}
		return taskNames;
	}
	//更新状态为no
	public void updateNo(String taskId) {
		Task task =new Task();
		task.setTaskId(taskId);
		task.setStatus(TaskStatus.ON.getValue());
		taskDao.updateByPrimaryKeySelective(task);
	}
	
	//更新状态为off
	public void updateOff(String taskId) {
		Task task =new Task();
		task.setTaskId(taskId);
		task.setStatus(TaskStatus.OFF.getValue());
		taskDao.updateByPrimaryKeySelective(task);
	}
	
	@Override
	public Response stopScheduling(String taskId,String taskTrackerNodeGroup) {
		//同时更新业务表任务状态
		updateOff(taskId);
		return jobClient.cancelJob(taskId,taskTrackerNodeGroup);
	}

	@Override
	public Response execute(String taskId, String string) {
		Task  task =taskDao.selectByPrimaryKey(taskId);
		Response response = null;
		if(null!=task) {
			Job job = new Job();
			job.setTaskId(task.getTaskId());
			job.setParam("task_identity", INSPECTION_TASK_NAME);
			job.setParam("task", JSONObject.toJSONString(task));
			job.setPriority(1);//优先级为1 优先被执行
//			job.setCronExpression(null);//手动的时候设置表达式
			job.setTaskTrackerNodeGroup("mirror_taskTracker");
			job.setNeedFeedback(true);
			job.setReplaceOnExist(true);//当任务队列中存在这个任务的时候，是否替换更新
			response = jobClient.submitJob(job);
			updateNo(taskId);
		}	
		return response;
	}
}
