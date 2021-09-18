package com.aspire.mirror.inspection.server.biz.impl;

import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.util.PageUtil;
import com.aspire.mirror.inspection.api.dto.model.TaskDTO;
import com.aspire.mirror.inspection.api.dto.model.TaskObjectDTO;
import com.aspire.mirror.inspection.api.dto.model.TaskRunDTO;
import com.aspire.mirror.inspection.server.biz.TaskBiz;
import com.aspire.mirror.inspection.server.biz.handler.TaskSyncHandler;
import com.aspire.mirror.inspection.server.controller.authcontext.RequestAuthContext;
import com.aspire.mirror.inspection.server.dao.TaskDao;
import com.aspire.mirror.inspection.server.dao.TaskObjectDao;
import com.aspire.mirror.inspection.server.dao.TaskReceiverDao;
import com.aspire.mirror.inspection.server.dao.po.Task;
import com.aspire.mirror.inspection.server.dao.po.TaskObject;
import com.aspire.mirror.inspection.server.dao.po.TaskReceiver;
import com.aspire.mirror.inspection.server.dao.po.TaskRun;
import com.aspire.mirror.inspection.server.dao.po.transform.TaskObjectTransformer;
import com.aspire.mirror.inspection.server.dao.po.transform.TaskTransformer;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
/**
 * inspection_task业务层实现类
 *
 * 项目名称:  微服务运维平台
 * 包:       com.aspire.mirror.inspection.server.biz.impl
 * 类名称:    TaskBizImpl.java
 * 类描述:    inspection_task业务逻辑层实现类
 * 创建人:    ZhangSheng
 * 创建时间:  2018-07-27 13:48:06
 */
@Service
@Transactional
public class TaskBizImpl implements TaskBiz {
    /**
     * 根据主键删除数据
     *
     * @param taskId 主键
     * @return int 删除数据条数
     */
    public int deleteByPrimaryKey(final String taskId){
        if (StringUtils.isEmpty(taskId)) {
            LOGGER.error("method[eleteByPrimaryKey] param[taskId] is null");
            throw new RuntimeException("param[taskId] is null");
        }
        taskReceiverDao.deleteByTaskId(taskId);
        int result = taskDao.deleteByPrimaryKey(taskId);
        // 添加同步数据
        taskSyncHandler.taskSyncDelete(new String[]{taskId});
        return result;
    }
    /**
    * 根据主键数组批量删除数据
    *
    * @param taskIdArrays 主键数组
    * @return int 删除数据条数
    */
    public int deleteByPrimaryKeyArrays(final String[] taskIdArrays){
        if (ArrayUtils.isEmpty(taskIdArrays)) {
            LOGGER.error("method[deleteByPrimaryKeyArrays] param[taskIdArrays] is null");
            throw new RuntimeException("param[taskIdArrays] is null");
        }
        taskReceiverDao.deleteByTaskIds(taskIdArrays);
        int result = taskDao.deleteByPrimaryKeyArrays(taskIdArrays);
        // 添加同步数据
        taskSyncHandler.taskSyncDelete(taskIdArrays);
        return result;
    }
    
    /**
     * 根据主键修改数据
     * @param taskDTO inspection_taskDTO对象
     * @return int 数据条数
     */
    public int updateByPrimaryKey(final TaskDTO taskDTO){
        if(null == taskDTO ){
            LOGGER.error("method[updateByPrimaryKey] param[taskUpdateRequest] is null");
            throw new RuntimeException("param[taskDTO] is null");
        }
        Task taskValid = taskDao.selectByName(taskDTO.getName());
        if (taskValid != null && !taskValid.getTaskId().equals(taskDTO.getTaskId())) {
            LOGGER.error("This task name is already existed");
            throw new RuntimeException("此任务名称已经存在");
        }
        Task task = new Task();
        //先删除task 用户关系映射表
        String taskId =taskDTO.getTaskId();
        taskReceiverDao.deleteByTaskId(taskId);
        String [] userids= taskDTO.getReceivers().split(",");
        List<TaskReceiver> receivers =Lists.newArrayList();
        for (String userid : userids) {
        	TaskReceiver receiver=new TaskReceiver();
        	String receiverId =UUID.randomUUID().toString();
        	receiver.setReceiverId(receiverId);
        	receiver.setTaskId(taskId);
        	receiver.setUserId(userid);
        	receivers.add(receiver);
		}
        taskReceiverDao.insertByBatch(receivers);
        taskDTO.setUpdateTime(new Date());
        BeanUtils.copyProperties(taskDTO,task);
        //添加任务设备
        taskObjectDao.deleteByTaskId(taskDTO.getTaskId());
        for (TaskObjectDTO TaskObjectDTO : taskDTO.getObjectList()) {
            TaskObjectDTO.setTaskObjectId(UUID.randomUUID().toString());
            TaskObjectDTO.setTaskId(taskId);
        }
        List<TaskObject> taskObjectList = TaskObjectTransformer.toPo(taskDTO.getObjectList());
        taskObjectDao.insertByBatch(taskObjectList);

        int result = taskDao.updateByPrimaryKey(task);
        // 添加同步数据
        taskSyncHandler.taskSyncAll(taskDTO, taskId);
        return result;
    }



    /**
     * 新增任务
     * @param taskDTO task对象
     * @return int 新增数据条数
     */
    public String insert(final TaskDTO taskDTO){
        if(null == taskDTO){
            LOGGER.error("method[insert] param[taskDTO] is null");
            throw new RuntimeException("param[taskDTO] is null");
        }
        Integer count = taskDao.selectCountByName(taskDTO.getName());
        if (count > 0) {
            LOGGER.error("This task name is already existed");
            throw new RuntimeException("此任务名称已经存在");
        }
        Task task = new Task();
        String [] userids= taskDTO.getReceivers().split(",");
        List<TaskReceiver> receivers =Lists.newArrayList();
        String taskId=UUID.randomUUID().toString();
        for (String userid : userids) {
        	TaskReceiver receiver=new TaskReceiver();
        	String receiverId =UUID.randomUUID().toString();
        	receiver.setReceiverId(receiverId);
        	receiver.setTaskId(taskId);
        	receiver.setUserId(userid);
        	receivers.add(receiver);
		}
        BeanUtils.copyProperties(taskDTO,task);
        Date date =new Date();
        task.setTaskId(taskId);
        task.setCreateTime(date);
        task.setUpdateTime(date);
        taskDao.insert(task);
        taskReceiverDao.insertByBatch(receivers);

        //添加任务设备
        for (TaskObjectDTO taskObjectDTO : taskDTO.getObjectList()) {
            taskObjectDTO.setTaskObjectId(UUID.randomUUID().toString());
            taskObjectDTO.setTaskId(taskId);
        }
        List<TaskObject> taskObjectList = TaskObjectTransformer.toPo(taskDTO.getObjectList());
        taskObjectDao.insertByBatch(taskObjectList);

        // 添加同步数据
        taskSyncHandler.taskSyncAll(taskDTO, taskId);
        return taskId;
    }
    /**
     * 根据主键查询任务详情信息
     * @param taskId 主键
     * @return TaskDTO 返回对象
     */
    public TaskDTO selectByPrimaryKey(final String taskId){
        Task task = taskDao.selectByPrimaryKey(taskId);
        if (StringUtils.isEmpty(taskId)) {
            LOGGER.warn("method[selectByPrimaryKey] param[taskId] is null");
            return null;
        }
        TaskDTO taskDTO = TaskTransformer.fromPo(task);
        List<String> userIds=taskReceiverDao.selectByTaskId(taskId);
        if (!CollectionUtils.isEmpty(userIds)) {
            taskDTO.setReceivers(Joiner.on(",").join(userIds));
        }
//        taskDTO.setDeviceNum(deviceNum);
        return taskDTO;
    }
    public Integer getDeviceNumByTaskId(final String taskId) {
        return taskObjectDao.getDeviceNumByTaskId(taskId);
    }
    /**
     * 根据任务分页请求对象查询列表
     * @param:pageRequest 分页信息请求对象
     * @return:List<TaskDTO> 巡检任务业务层对象
     */
    @Override
	public List<TaskDTO> list(final PageRequest pageRequest) {
        Map<String, List<String>> resFilterConfig = RequestAuthContext.currentRequestAuthContext().getUser().getResFilterConfig();
    	Page page =PageUtil.convert(pageRequest);
        page.getParams().put("resFilterMap", resFilterConfig);
    	//查询任务
    	String queryName="com.aspire.mirror.inspection.server.dao.TaskDao.pageList";
    	Integer pageNo =page.getPageNo();
		Integer pageSize =page.getPageSize();
		Integer startIndex = (pageNo-1)*pageSize;
    	List<Task> taskList =sqlSessionTemplate.selectList(queryName, page,new RowBounds(startIndex, pageSize));
    	//将Task装换成业务对象
    	List<TaskDTO> taskDTOs =TaskTransformer.fromPo(taskList);
    	//添加receivers值
    	for (TaskDTO taskDTO : taskDTOs) {
    		List<String> userIds=taskReceiverDao.selectByTaskId(taskDTO.getTaskId());
			taskDTO.setReceivers(Joiner.on(",").join(userIds));
		}
		return taskDTOs;
	}
    
    /**
     * 根据page查询总数
     */
    @Override
	public int selectCount(final PageRequest page) {
    	if(null == page){
            LOGGER.warn("selectCount Object page is null");
        }
        Map<String, List<String>> resFilterConfig = RequestAuthContext.currentRequestAuthContext().getUser().getResFilterConfig();
    	Page page2 =PageUtil.convert(page);
        page2.getParams().put("resFilterMap", resFilterConfig);
		int count =taskDao.selectCountByPage(page2);
		return count;
	}
    
	@Override
	public int selectTaskRunCount(PageRequest pageRequest) {
		Page page =PageUtil.convert(pageRequest);
		int count =taskDao.selectTaskRunCount2(page);
		return count;
	}

    
	@Override
	public List<TaskRunDTO> runList(final PageRequest pageRequest) {
		if (null==pageRequest) {
			LOGGER.warn("runList Object pageRequest is null");
		}
		Page page =PageUtil.convert(pageRequest);
		String queryName="com.aspire.mirror.inspection.server.dao.TaskDao.runList2";
		Integer pageNo =page.getPageNo();
		Integer pageSize =page.getPageSize();
		Integer startIndex = (pageNo-1)*pageSize;
		List<TaskRun> taskRuns=sqlSessionTemplate.selectList(queryName,page,new RowBounds(startIndex, pageSize));
		//将TaskRun装换成业务对象
		List<TaskRunDTO> taskRunDTOs=Lists.newArrayList();
		for (TaskRun taskRun : taskRuns) {
			TaskRunDTO runDTO =TaskTransformer.fromPo(taskRun);
			taskRunDTOs.add(runDTO);
		}
		return taskRunDTOs;
	}

    

    /** slf4j*/
    private static final Logger   LOGGER = LoggerFactory.getLogger(TaskBizImpl.class);

    /** 数据访问层接口*/
    @Autowired
    private TaskDao taskDao;
    
    @Autowired
    private TaskReceiverDao taskReceiverDao;
    
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private TaskObjectDao taskObjectDao;

    @Autowired
    private TaskSyncHandler taskSyncHandler;
} 
