package com.aspire.mirror.alert.server.controller.taskChange;

import com.aspire.mirror.alert.api.dto.taskChange.TaskDetail;
import com.aspire.mirror.alert.api.dto.taskChange.TaskQueryRequest;
import com.aspire.mirror.alert.api.dto.taskChange.TaskRequest;
import com.aspire.mirror.alert.api.service.taskChange.TaskChangeManageService;
import com.aspire.mirror.alert.server.biz.taskChange.TaskChangeManageBiz;
import com.aspire.mirror.alert.server.vo.taskChange.TaskQueryRequestDTO;
import com.aspire.mirror.alert.server.vo.taskChange.TaskRequestDTO;
import com.aspire.mirror.alert.server.util.PayloadParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class TaskChangeManageController implements TaskChangeManageService {

    @Autowired
    private TaskChangeManageBiz taskChangeManageBiz;

    @Override
    public String addTask(@RequestBody TaskRequest compTaskRequest) {
        return taskChangeManageBiz.addTask(PayloadParseUtil.jacksonBaseParse(TaskRequestDTO.class,compTaskRequest));
    }

    @Override
    public TaskDetail getTaskDetail(@RequestParam("uuid") String uuid) {
        return PayloadParseUtil.jacksonBaseParse(TaskDetail.class, taskChangeManageBiz.getTaskDetail(uuid));
    }

    @Override
    public void updateTask(@RequestBody TaskRequest compTaskRequest) {
        taskChangeManageBiz.updateTask(PayloadParseUtil.jacksonBaseParse(TaskRequestDTO.class,compTaskRequest));
    }

    @Override
    public void deleteTask(@RequestParam("userName") String userName,
                           @RequestParam("uuid") String uuid) {
        taskChangeManageBiz.deleteTask(userName, uuid);
    }

    @Override
    public void editTaskMessage(@RequestBody Map<String, String> request) {
        taskChangeManageBiz.editTaskMessage(request);
    }

    @Override
    public void startTask(@RequestBody TaskRequest compTaskRequest) {
        taskChangeManageBiz.startTask(PayloadParseUtil.jacksonBaseParse(TaskRequestDTO.class,compTaskRequest));
    }

    @Override
    public void stopTask(@RequestBody Map<String, String> request) {
        taskChangeManageBiz.stopTask(request);
    }

    @Override
    public List<Map<String, Object>> getTaskActionList(@RequestParam("uuid") String uuid) {
        return taskChangeManageBiz.getTaskActionList(uuid);
    }

    @Override
    public Map<String, Object> getTaskMessageList(@RequestParam("uuid") String uuid,
                                                  @RequestParam("pageNum") Integer pageNum,
                                                  @RequestParam("pageSize") Integer pageSize) {
        return taskChangeManageBiz.getTaskMessageList(uuid,pageNum,pageSize);
    }

    @Override
    public List<Map<String, Object>> exportTask(@RequestBody Map<String, String> request) {
        return taskChangeManageBiz.exportTask(request);
    }

    @Override
    public void taskNotify(@RequestParam("uuid") String uuid) {

    }

    @Override
    public Map<String, Object> getTaskList(@RequestBody TaskQueryRequest compTaskQueryRequest) {
        return taskChangeManageBiz.getTaskList(PayloadParseUtil.jacksonBaseParse(TaskQueryRequestDTO.class,compTaskQueryRequest));
    }

    @Override
    public void autoStopTask() {
        taskChangeManageBiz.autoStopTask();
    }
}
