package com.aspire.mirror.inspection.server.biz;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.inspection.server.dao.po.Task;
import com.github.ltsopensource.core.domain.Job;
import com.github.ltsopensource.jobclient.domain.Response;
import com.google.common.collect.Lists;

/** 
* @author ZhangSheng 
* @version 2018年8月12日 下午6:16:01 
* @describe 自动巡检任务任务调度客户端业务层接口
*/
public interface TaskClientBiz {
	/**
	 * 添加任务调度
	 * @param taskId
	 * @return response
	 */
	public Response addScheduling(String taskId);
	/**
	 * 获取返回结果集中的list
	 * @param response
	 * @return
	 */
	public List<String> getTaskNames(Response response);
	/**
	 * 停止任务调度
	 * @param taskId
	 * @param taskTrackerNodeGroup
	 * @return
	 */
	public Response stopScheduling(String taskId,String taskTrackerNodeGroup);
	
	/**
	 * 立即执行任务
	 * @param taskId
	 * @param string
	 * @return
	 */
	public Response execute(String taskId, String string);
}
