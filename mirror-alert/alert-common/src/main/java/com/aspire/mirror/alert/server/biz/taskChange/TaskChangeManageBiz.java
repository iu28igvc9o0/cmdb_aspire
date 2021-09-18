package com.aspire.mirror.alert.server.biz.taskChange;

import com.aspire.mirror.alert.server.vo.taskChange.TaskDetailDTO;
import com.aspire.mirror.alert.server.vo.taskChange.TaskQueryRequestDTO;
import com.aspire.mirror.alert.server.vo.taskChange.TaskRequestDTO;

import java.util.List;
import java.util.Map;

public interface TaskChangeManageBiz {

    /**
     * 新建任务
     */
    String addTask(TaskRequestDTO taskRequestDTO);
    /**
     * 获取任务详情
     */
    TaskDetailDTO getTaskDetail(String uuid);
    /**
     * 编辑任务
     */
    void updateTask(TaskRequestDTO taskRequestDTO);
    /**
     * 删除任务
     */
    void deleteTask(String userName,String uuid);
    /**
     * 编辑反馈意见
     */
    void editTaskMessage(Map<String, String> request);
    /**
     * 开始任务
     */
    void startTask(TaskRequestDTO taskRequestDTO);
    /**
     * 结束任务
     */
    void stopTask(Map<String, String> request);
    /**
     * 获取任务操作列表
     */
    List<Map<String, Object>> getTaskActionList(String uuid);
    /**
     * 获取任务反馈意见列表
     */
    Map<String, Object> getTaskMessageList(String uuid,Integer pageNum,Integer pageSize);
    /**
     * 导出任务列表
     */
    List<Map<String, Object>> exportTask(Map<String, String> request);
    /**
     * 获取任务列表
     */
    Map<String, Object> getTaskList(TaskQueryRequestDTO compTaskQueryRequest);
    /**
     * 自动结束任务
     */
    void autoStopTask();

}
