package com.aspire.mirror.alert.server.dao.taskChange;

import com.aspire.mirror.alert.server.vo.taskChange.TaskDetailDTO;
import com.aspire.mirror.alert.server.vo.taskChange.TaskQueryRequestDTO;
import com.aspire.mirror.alert.server.vo.taskChange.TaskRequestDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface TaskChangeManageDao {
    /**
     * 新建任务
     */
    void addTask(TaskRequestDTO taskRequestDTO);
    /**
     * 获取任务详情
     */
    TaskDetailDTO getTaskDetail(@Param("uuid") String uuid);
    /**
     * 新建任务
     */
    void updateTask(TaskRequestDTO taskRequestDTO);
    /**
     * 删除任务
     */
    void deleteTask(@Param("uuid") String uuid);
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
    List<Map<String, Object>> getTaskActionList(@Param("uuid") String uuid);
    /**
     * 获取任务操作列表
     */
    int getTaskMessageCount(@Param("uuid") String uuid,
                            @Param("pageNum") Integer pageNum,
                            @Param("pageSize") Integer pageSize);
    List<Map<String, Object>> getTaskMessageList(@Param("uuid") String uuid,
                                                 @Param("pageNum") Integer pageNum,
                                                 @Param("pageSize") Integer pageSize);
    /**
     * 导出任务列表
     */
    List<Map<String, Object>> exportTask(Map<String, String> request);
    /**
     * 更新反馈意见条数
     */
    void updateMessageCount(@Param("uuid") String uuid);
    /**
     * 获取任务列表
     */
    List<TaskDetailDTO> getMonWeekViewTaskList(TaskQueryRequestDTO taskQueryRequestDTO);
    int getDayWeekViewTaskCount(TaskQueryRequestDTO taskQueryRequestDTO);
    List<TaskDetailDTO> getDayWeekViewTaskList(TaskQueryRequestDTO taskQueryRequestDTO);

    /**
     * 重复命名
     */
    TaskDetailDTO getTaskDetailByName(@Param("taskName") String taskName);
}
