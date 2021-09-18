package com.aspire.mirror.inspection.server.biz;

/**
* 巡检任务执行业务接口  <br/>
* Project Name:inspection-service
* File Name:TaskExecuteBiz.java
* Package Name:com.aspire.mirror.inspection.server.biz
* ClassName: TaskExecuteBiz <br/>
* date: 2018年8月30日 下午4:04:41 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
public interface TaskExecuteBiz {
	public void executeInspectionTask(String taskId);
}
