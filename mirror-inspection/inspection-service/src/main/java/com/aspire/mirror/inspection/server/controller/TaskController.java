package com.aspire.mirror.inspection.server.controller ;

import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.inspection.api.dto.*;
import com.aspire.mirror.inspection.api.dto.model.TaskDTO;
import com.aspire.mirror.inspection.api.dto.model.TaskObjectDTO;
import com.aspire.mirror.inspection.api.dto.model.TaskRunDTO;
import com.aspire.mirror.inspection.api.service.TaskService;
import com.aspire.mirror.inspection.server.biz.TaskBiz;
import com.aspire.mirror.inspection.server.biz.TaskObjectBiz;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * inspection_task控制层实现类
 *
 * 项目名称: 微服务运维平台
 * 包:      com.aspire.mirror.inspection.server.controller
 * 类名称:   TaskController.java
 * 类描述:   inspection_task业务逻辑层实现类
 * 创建人:   ZhangSheng
 * 创建时间: 2018-07-27 13:48:06
 */
@RestController
@CacheConfig(cacheNames = "TaskCache")
public class TaskController implements TaskService {
     /**
     * 根据主键删除inspection_task信息
     * @param taskId 主键
     * @return Result 返回结果
     */
    public ResponseEntity<String> deleteByPrimaryKey(@PathVariable("task_ids") final String taskIds){
    	if (StringUtils.isEmpty(taskIds)) {
            LOGGER.warn("deleteByPrimaryKey param taskId is null");
            return null;
        }
        try {
        	 String[] taskIdArrays = taskIds.split(",");
             taskBiz.deleteByPrimaryKeyArrays(taskIdArrays);
             return new ResponseEntity<String>("删除成功",HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("deleteByProjectId error:" + e.getMessage(), e);
            return new ResponseEntity<String>("删除失败",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 根据主键修改task信息
     * @param taskUpdateRequest task修改请求对象
     * @return TaskUpdateResponse task修改响应对象
     */
    public TaskUpdateResponse modifyByPrimaryKey(@PathVariable("task_id") final String taskId,@Validated @RequestBody final TaskUpdateRequest taskUpdateRequest){
    	if(StringUtils.isEmpty(taskId)){
            LOGGER.error("modifyByPrimaryKey param taskId is null");
            throw new RuntimeException("taskId is null");
        }
    	if(null==taskUpdateRequest) {
    		LOGGER.error("modifyByPrimaryKey param taskUpdateRequest is null");
    		throw new RuntimeException("taskUpdateRequest is null");
    	}

    	TaskDTO taskDTO = new TaskDTO();
        BeanUtils.copyProperties(taskUpdateRequest, taskDTO);
        taskDTO.setTaskId(taskId);
        List<TaskObjectDTO> deviceDTOList = Lists.newArrayList();
        for (TaskObjectCreateRequest createRequest : taskUpdateRequest.getObjectList()) {
            TaskObjectDTO deviceDTO = new TaskObjectDTO();
            BeanUtils.copyProperties(createRequest, deviceDTO);
            deviceDTOList.add(deviceDTO);
        }
        taskDTO.setObjectList(deviceDTOList);
        taskBiz.updateByPrimaryKey(taskDTO);
        TaskDTO findTaskDTO = taskBiz.selectByPrimaryKey(taskId);
        TaskUpdateResponse taskUpdateResponse = new TaskUpdateResponse();
        BeanUtils.copyProperties(findTaskDTO,taskUpdateResponse);
        return taskUpdateResponse;
    }
    
    
    /**
     * 创建任务信息
     * @param taskCreateRequest 任务创建请求对象
     * @return TaskCreateResponse 任务创建响应对象
     */
    public TaskCreateResponse createdTask(@Validated @RequestBody final TaskCreateRequest taskCreateRequest){
        if(null == taskCreateRequest){
            LOGGER.error("created param taskCreateRequest is null");
            throw new RuntimeException("taskCreateRequest is null");
        }
        TaskDTO taskDTO = new TaskDTO();
        BeanUtils.copyProperties(taskCreateRequest, taskDTO);
        List<TaskObjectDTO> deviceDTOList = Lists.newArrayList();
        for (TaskObjectCreateRequest createRequest : taskCreateRequest.getObjectList()) {
            TaskObjectDTO deviceDTO = new TaskObjectDTO();
            BeanUtils.copyProperties(createRequest, deviceDTO);
            deviceDTOList.add(deviceDTO);
        }
        taskDTO.setObjectList(deviceDTOList);
        String taskId = taskBiz.insert(taskDTO);


        TaskCreateResponse taskCreateResponse = new TaskCreateResponse();
        BeanUtils.copyProperties(taskDTO, taskCreateResponse);
        taskCreateResponse.setTaskId(taskId);
        return taskCreateResponse;
    }
    
    
    /**
     * 根据主键查找任务（巡检任务）详情信息
     * @param taskId 巡检任务主键
     * @return TaskVO 巡检任务详情响应对象
     */
    public TaskDetailResponse findByPrimaryKey(@PathVariable("task_id") String taskId){
        if (StringUtils.isEmpty(taskId)) {
            LOGGER.warn("findByPrimaryKey param taskId is null");
            throw new RuntimeException("taskId is null");
        }
        TaskDTO taskDTO = taskBiz.selectByPrimaryKey(taskId);
        TaskDetailResponse  taskVO =new TaskDetailResponse();
        if (null != taskDTO) {
            BeanUtils.copyProperties(taskDTO, taskVO);
        }
        return taskVO;
    }
    
    
    /**
     * 根据分页查询对象查询查询分页集合信息
     * @param taskPageRequest 任务分页请求信息
     * @return PageResponse<TaskDetailResponse>: 查询响应结果
     */
    public PageResponse<TaskDetailResponse> list(@Validated @RequestBody TaskPageRequest taskPageRequest){
        if (taskPageRequest==null) {
            LOGGER.error("list param taskPageRequest is null");
            return null;
        }
        PageRequest  page =new PageRequest();
        //配置查询条件
        page.addFields("template_id",taskPageRequest.getTemplateId());
        page.addFields("name",taskPageRequest.getName());
        page.addFields("type",taskPageRequest.getType());
        page.addFields("exec_time_start",taskPageRequest.getExecTimeStart());
        page.addFields("exec_time_end",taskPageRequest.getExecTimeEnd());
        page.setPageNo(taskPageRequest.getPageNo());
        page.setPageSize(taskPageRequest.getPageSize());
        
        List<TaskDTO> taskDTOs = taskBiz.list(page);
        List<TaskDetailResponse> listTaskDeta = Lists.newArrayList();
        for (TaskDTO taskDTO : taskDTOs) {
        	TaskDetailResponse detailResponse =new TaskDetailResponse();
        	BeanUtils.copyProperties(taskDTO, detailResponse);
        	listTaskDeta.add(detailResponse);
		}
        PageResponse<TaskDetailResponse> pageResponse =new PageResponse<TaskDetailResponse>();
        pageResponse.setResult(listTaskDeta);
        pageResponse.setCount(taskBiz.selectCount(page));
        return pageResponse;
    }

	@Override
	public PageResponse<TaskRunDetailResponse> runList(final @Validated @RequestBody TaskRunRequest taskRunRequest) {
		if(taskRunRequest==null) {
			LOGGER.error("list param taskRunRequest is null");
            return null;
		}
		PageRequest pageRequest =new PageRequest();
		pageRequest.addFields("task_id",taskRunRequest.getTaskId());
		pageRequest.addFields("name",taskRunRequest.getName());
		pageRequest.addFields("type",taskRunRequest.getType());
		pageRequest.addFields("exec_time_start",taskRunRequest.getExecTimeStart());
		pageRequest.addFields("exec_time_end",taskRunRequest.getExecTimeEnd());
		pageRequest.setPageSize(taskRunRequest.getPageSize());
		pageRequest.setPageNo(taskRunRequest.getPageNo());
		List<TaskRunDTO> taskRunDTOs=taskBiz.runList(pageRequest);
		List<TaskRunDetailResponse> detailResponses =Lists.newArrayList();
		for (TaskRunDTO taskRunDTO : taskRunDTOs) {
			TaskRunDetailResponse detailResponse=new TaskRunDetailResponse();
			List<String> templateIds =Lists.newArrayList();
			List<TaskObjectDTO> deviceDTOs=deviceBiz.findByTaskId(taskRunDTO.getTaskId());
			for (TaskObjectDTO TaskObjectDTO : deviceDTOs) {
				templateIds.add(TaskObjectDTO.getTemplateId());
			}
			BeanUtils.copyProperties(taskRunDTO,detailResponse);
			detailResponse.setTemplateIds(templateIds);
			detailResponses.add(detailResponse);
		}
		PageResponse<TaskRunDetailResponse> pageResponse =new PageResponse<TaskRunDetailResponse>();
        pageResponse.setResult(detailResponses);
        pageResponse.setCount(taskBiz.selectTaskRunCount(pageRequest));
		return pageResponse;
	}
    
    
    /** slf4j*/
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);
    
    
    @Autowired
    private TaskBiz taskBiz;
    
    @Autowired
    private TaskObjectBiz deviceBiz;




}