package com.aspire.mirror.inspection.server.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.aspire.mirror.inspection.api.service.TaskClientService;
import com.aspire.mirror.inspection.server.biz.TaskClientBiz;
import com.github.ltsopensource.jobclient.domain.Response;

/** 
* @author ZhangSheng 
* @version 2018年8月12日 下午6:02:20 
* @describe 
*/
@RestController
@CacheConfig(cacheNames = "TaskCache")
public class TaskClientController implements TaskClientService{
	
	@Autowired
	private TaskClientBiz taskClientBiz;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TaskClientController.class);

	@Override
	public ResponseEntity<String> addScheduling(@PathVariable("task_id")final String taskId) {
		
		Response response=null;
		
		try {
			
			response =taskClientBiz.addScheduling(taskId);
			
		} catch (Exception e) {
			
			
			  LOGGER.error("com.aspire.mirror.inspection.server.controller.TaskClientController-->addScheduling error:" + e.getMessage(), e);
			  
			  return new ResponseEntity<String>(taskClientBiz.getTaskNames(response).toString()+"任务调度失败",HttpStatus.SERVICE_UNAVAILABLE);
		
		}
		
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		
	}

	@Override
	public ResponseEntity<String> stopScheduling(@PathVariable("task_id") String taskId) {
		Response response=null;
		try {
			response=taskClientBiz.stopScheduling(taskId,"mirror_taskTracker");
		} catch (Exception e) {
			LOGGER.error("com.aspire.mirror.inspection.server.controller.TaskClientController-->addScheduling error:" + e.getMessage(), e);
			return new ResponseEntity<String>(taskClientBiz.getTaskNames(response).toString()+"任务取消失败！",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}

	@Override
	public ResponseEntity<String> execute(@PathVariable("task_id") String taskId) {
		Response response=null;
		try {
			response=taskClientBiz.execute(taskId,"mirror_taskTracker");
		} catch (Exception e) {
			LOGGER.error("com.aspire.mirror.inspection.server.controller.TaskClientController-->addScheduling error:" + e.getMessage(), e);
			return new ResponseEntity<String>(taskClientBiz.getTaskNames(response).toString()+"任务执行失败！",HttpStatus.SERVICE_UNAVAILABLE);
		}
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}
	
}
