package com.aspire.mirror.alert.server.biz.taskChange.impl;

import com.aspire.mirror.alert.server.biz.taskChange.TaskChangeManageBiz;
import com.aspire.mirror.alert.server.dao.operateLog.AlertOperateLogMapper;
import com.aspire.mirror.alert.server.dao.taskChange.TaskChangeManageDao;
import com.aspire.mirror.alert.server.dao.operateLog.po.AlertOperateLog;
import com.aspire.mirror.alert.server.vo.taskChange.TaskDetailDTO;
import com.aspire.mirror.alert.server.vo.taskChange.TaskQueryRequestDTO;
import com.aspire.mirror.alert.server.vo.taskChange.TaskRequestDTO;
import com.aspire.mirror.alert.server.util.StringUtils;
import com.aspire.mirror.common.entity.PageResponse;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class TaskChangeManageBizImpl implements TaskChangeManageBiz {

    @Autowired
    private TaskChangeManageDao taskChangeManageDao;
    @Autowired
    private AlertOperateLogMapper alertOperateLogMapper;

    @Override
    public String addTask(TaskRequestDTO taskRequestDTO) {
        try {
            TaskDetailDTO taskDetailByName = taskChangeManageDao.getTaskDetailByName(taskRequestDTO.getTaskName());
            if (null != taskDetailByName) {
                log.error("[TaskChangeManage] taskName of Adding Task is exist");
                throw new RuntimeException("taskName of Adding Task is exist");
            }
            String uuid = UUID.randomUUID().toString();
            taskRequestDTO.setUuid(uuid);
            taskChangeManageDao.addTask(taskRequestDTO);
            AlertOperateLog alertOperateLog = new AlertOperateLog();
            alertOperateLog.setOperateContent("创建任务");
            alertOperateLog.setOperateModel("task_change_manage_config");
            alertOperateLog.setOperateModelDesc("计划变更管理");
            alertOperateLog.setOperater(taskRequestDTO.getCreator());
            alertOperateLog.setOperateTime(new Date());
            alertOperateLog.setOperateType("insert");
            alertOperateLog.setOperateTypeDesc("创建任务");
            alertOperateLog.setRelationId(taskRequestDTO.getUuid());
            alertOperateLogMapper.insert(alertOperateLog);
            return uuid;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new RuntimeException(e);
        }
    }

    @Override
    public TaskDetailDTO getTaskDetail(String uuid) {
        return taskChangeManageDao.getTaskDetail(uuid);
    }

    @Override
    public void updateTask(TaskRequestDTO taskRequestDTO) {
        try {
            taskChangeManageDao.updateTask(taskRequestDTO);
            AlertOperateLog alertOperateLog = new AlertOperateLog();
            alertOperateLog.setOperateContent("编辑任务");
            alertOperateLog.setOperateModel("task_change_manage_config");
            alertOperateLog.setOperateModelDesc("计划变更管理");
            alertOperateLog.setOperater(taskRequestDTO.getCreator());
            alertOperateLog.setOperateTime(new Date());
            alertOperateLog.setOperateType("update");
            alertOperateLog.setOperateTypeDesc("编辑任务");
            alertOperateLog.setRelationId(taskRequestDTO.getUuid());
            alertOperateLogMapper.insert(alertOperateLog);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteTask(String userName,String uuid) {
        try {
            taskChangeManageDao.deleteTask(uuid);
            AlertOperateLog alertOperateLog = new AlertOperateLog();
            alertOperateLog.setOperateContent("删除任务");
            alertOperateLog.setOperateModel("task_change_manage_config");
            alertOperateLog.setOperateModelDesc("计划变更管理");
            alertOperateLog.setOperater(userName);
            alertOperateLog.setOperateTime(new Date());
            alertOperateLog.setOperateType("delete");
            alertOperateLog.setOperateTypeDesc("删除任务");
            alertOperateLog.setRelationId(uuid);
            alertOperateLogMapper.insert(alertOperateLog);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void editTaskMessage(Map<String, String> request) {
        taskChangeManageDao.updateMessageCount(request.get("uuid"));
        AlertOperateLog alertOperateLog = new AlertOperateLog();
        alertOperateLog.setOperateContent(request.get("content"));
        alertOperateLog.setOperateModel("task_change_manage_config");
        alertOperateLog.setOperateModelDesc("计划变更管理");
        alertOperateLog.setOperater(request.get("userName"));
        alertOperateLog.setOperateTime(new Date());
        alertOperateLog.setOperateType("insert");
        alertOperateLog.setOperateTypeDesc("添加反馈意见");
        alertOperateLog.setRelationId(request.get("uuid"));
        alertOperateLog.setRemark("message");
        alertOperateLogMapper.insert(alertOperateLog);
    }

    @Override
    public void startTask(TaskRequestDTO taskRequestDTO) {
        taskChangeManageDao.startTask(taskRequestDTO);
        AlertOperateLog alertOperateLog = new AlertOperateLog();
        alertOperateLog.setOperateContent(taskRequestDTO.getOrderId());
        alertOperateLog.setOperateModel("task_change_manage_config");
        alertOperateLog.setOperateModelDesc("计划变更管理");
        alertOperateLog.setOperater(taskRequestDTO.getUpdater());
        alertOperateLog.setOperateTime(new Date());
        alertOperateLog.setOperateType("start");
        alertOperateLog.setOperateTypeDesc("执行开始");
        alertOperateLog.setRelationId(taskRequestDTO.getUuid());
        alertOperateLogMapper.insert(alertOperateLog);
    }

    @Override
    public void stopTask(Map<String, String> request) {
        taskChangeManageDao.stopTask(request);
        AlertOperateLog alertOperateLog = new AlertOperateLog();
        alertOperateLog.setOperateContent(request.get("taskResult").equals("1") ? "执行成功" : request.get("massage"));
        alertOperateLog.setOperateModel("task_change_manage_config");
        alertOperateLog.setOperateModelDesc("计划变更管理");
        alertOperateLog.setOperater(request.get("userName"));
        alertOperateLog.setOperateTime(new Date());
        alertOperateLog.setOperateType("stop");
        alertOperateLog.setOperateTypeDesc(request.get("taskResult").equals("1") ? "执行成功" : "执行失败");
        alertOperateLog.setRelationId(request.get("uuid"));
        alertOperateLogMapper.insert(alertOperateLog);
    }

    @Override
    public List<Map<String, Object>> getTaskActionList(String uuid) {
        return taskChangeManageDao.getTaskActionList(uuid);
    }

    @Override
    public Map<String, Object> getTaskMessageList(String uuid,Integer pageNum,Integer pageSize) {
        Map<String, Object> response = Maps.newHashMap();
        response.put("count", taskChangeManageDao.getTaskMessageCount(uuid,pageNum,pageSize));
        response.put("result", taskChangeManageDao.getTaskMessageList(uuid,pageNum,pageSize));
        return response;
    }

    @Override
    public List<Map<String, Object>> exportTask(Map<String, String> request) {
        return taskChangeManageDao.exportTask(request);
    }

    @Override
    public Map<String, Object> getTaskList(TaskQueryRequestDTO compTaskQueryRequest) {
        String taskMassage = compTaskQueryRequest.getTaskMassage();
        if (StringUtils.isNotEmpty(taskMassage) && taskMassage.contains("1")) {
            compTaskQueryRequest.setTaskMassage(taskMassage.replace("1","-1"));
        }
        String taskStatus = compTaskQueryRequest.getTaskStatus();
        if (StringUtils.isNotEmpty(taskStatus) && taskStatus.contains("3")) {
            String[] split = taskStatus.split(",");
            List<String> strings = new ArrayList<>(Arrays.asList(split));
            strings.add("4");
            compTaskQueryRequest.setTaskStatus(Joiner.on(",").join(strings));
        }
        Map<String, Object> response = Maps.newHashMap();
        switch (compTaskQueryRequest.getViewType()) {
            case "1":
                List<TaskDetailDTO> monViewTaskList = taskChangeManageDao.getMonWeekViewTaskList(compTaskQueryRequest);
                response.put("result",monViewTaskList);
                break;
            case "2":
                List<TaskDetailDTO> weekViewTaskList = taskChangeManageDao.getMonWeekViewTaskList(compTaskQueryRequest);
                response.put("result",weekViewTaskList);
                break;
            case "3":
                PageResponse<TaskDetailDTO> res = new PageResponse<TaskDetailDTO>();
                res.setCount(taskChangeManageDao.getDayWeekViewTaskCount(compTaskQueryRequest));
                res.setResult(taskChangeManageDao.getDayWeekViewTaskList(compTaskQueryRequest));
                response.put("result",res);
                break;
            default:
                break;
        }
        return response;
    }

    @Override
    public void autoStopTask() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String curTime = sdf.format(new Date());
        TaskQueryRequestDTO taskQueryRequestDTO = new TaskQueryRequestDTO();
        taskQueryRequestDTO.setViewType("1");
        taskQueryRequestDTO.setTaskStatus("2");
        List<TaskDetailDTO> monViewTaskList = taskChangeManageDao.getMonWeekViewTaskList(taskQueryRequestDTO);
        monViewTaskList.forEach(item -> {
            if (item.getTaskEndTime().compareTo(curTime) < 0) {
                Map<String, String> param = Maps.newHashMap();
                param.put("uuid",item.getUuid());
                param.put("taskResult","1");
                param.put("task_status","4");
                taskChangeManageDao.stopTask(param);
                AlertOperateLog alertOperateLog = new AlertOperateLog();
                alertOperateLog.setOperateContent("自动执行成功");
                alertOperateLog.setOperateModel("task_change_manage_config");
                alertOperateLog.setOperateModelDesc("计划变更管理");
                alertOperateLog.setOperater(item.getCreator());
                alertOperateLog.setOperateTime(new Date());
                alertOperateLog.setOperateType("stop");
                alertOperateLog.setOperateTypeDesc( "执行成功");
                alertOperateLog.setRelationId(item.getUuid());
                alertOperateLogMapper.insert(alertOperateLog);
            }
        });
    }
}
