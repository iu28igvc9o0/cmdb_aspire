package com.aspire.mirror.inspection.server.controller;

import com.aspire.mirror.inspection.api.dto.TaskObjectBatchCreateRequst;
import com.aspire.mirror.inspection.api.dto.TaskObjectCreateRequest;
import com.aspire.mirror.inspection.api.dto.TaskObjectDetailResponse;
import com.aspire.mirror.inspection.api.dto.model.TaskObjectDTO;
import com.aspire.mirror.inspection.api.service.TaskObjectService;
import com.aspire.mirror.inspection.server.biz.TaskObjectBiz;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 任务设备实现
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.inspection.server.controller
 * 类名称:    TaskObjectController.java
 * 类描述:    任务设备实现
 * 创建人:    JinSu
 * 创建时间:  2018/8/11 11:16
 * 版本:      v1.0
 */
@RestController
public class TaskObjectController implements TaskObjectService {

    private static final Logger LOGGER= LoggerFactory.getLogger(TaskObjectController.class);

    @Autowired
    private TaskObjectBiz TaskObjectBiz;
    /**
     * 根据设备ID删除数据
     * @param taskId 任务ID
     * @return
     */
    @Override
    public ResponseEntity<String> deleteByTaskId(@PathVariable("task_id") final String taskId) {
        if (StringUtils.isEmpty(taskId)) {
            throw new RuntimeException("method[deleteByTaskId] param taskId is empty");
        }
        try {
            TaskObjectBiz.deleteByTaskId(taskId);
            return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            LOGGER.error("deleteByTaskId error:" + e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 批量新增数据
     * @param batchCreateRequst 批量创建请求
     * @return
     */
    @Override
    public ResponseEntity<String> batchCreate(@RequestBody final TaskObjectBatchCreateRequst batchCreateRequst) {
        if (null == batchCreateRequst) {
            throw new RuntimeException("method[batchCreate] param batchCreateRequst is null");
        }
        if (CollectionUtils.isEmpty(batchCreateRequst.getObjectList())) {
            throw new RuntimeException("method[batchCreate] param deviceList is empty");
        }
        List<TaskObjectDTO> deviceDTOList = Lists.newArrayList();
        for (TaskObjectCreateRequest createRequest : batchCreateRequst.getObjectList()) {
            TaskObjectDTO deviceDTO = new TaskObjectDTO();
            BeanUtils.copyProperties(createRequest, deviceDTO);
            deviceDTOList.add(deviceDTO);
        }
        TaskObjectBiz.insertBatch(deviceDTOList);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    /**
     * 根据taskId查询
     * @param taskId 任务Id
     * @return
     */
    @Override
    public List<TaskObjectDetailResponse> findByTaskId(@PathVariable("task_id") final String taskId) {
        if(StringUtils.isEmpty(taskId)) {
            LOGGER.info("method[findByTaskId] param taskId is empty");
            return null;
        }
        List<TaskObjectDTO> deviceDTOList = TaskObjectBiz.findByTaskId(taskId);
        List<TaskObjectDetailResponse> responseList = Lists.newArrayList();
        for (TaskObjectDTO TaskObjectDTO : deviceDTOList) {
            TaskObjectDetailResponse TaskObjectDetailResponse = new TaskObjectDetailResponse();
            BeanUtils.copyProperties(TaskObjectDTO, TaskObjectDetailResponse);
            responseList.add(TaskObjectDetailResponse);
        }
        return responseList;
    }

    /**
     * 根据任务Id查询设备
     * @param taskIds 任务Id集合
     * @return TaskObjectDetailResponse 任务设备返回
     */
    @Override
    public List<TaskObjectDetailResponse> findByTaskIds(@PathVariable("task_ids") final String taskIds) {
        if(StringUtils.isEmpty(taskIds)) {
            LOGGER.info("method[findTaskCountByTemplateId] param templateId is empty");
            return null;
        }
        String[] taskIdArrays = taskIds.split(",");
        List<TaskObjectDTO> deviceDTOList = TaskObjectBiz.findByTaskIdArrays(taskIdArrays);
        List<TaskObjectDetailResponse> responseList = Lists.newArrayList();
        for (TaskObjectDTO TaskObjectDTO : deviceDTOList) {
            TaskObjectDetailResponse TaskObjectDetailResponse = new TaskObjectDetailResponse();
            BeanUtils.copyProperties(TaskObjectDTO, TaskObjectDetailResponse);
            responseList.add(TaskObjectDetailResponse);
        }
        return responseList;
    }

    /**
     * 根据模板Id获取任务数
     * @param templateId 模板ID
     * @return Integer 任务数量
     */
    @Override
    public Integer findTaskCountByTemplateId(@PathVariable("template_id") final String templateId) {
        if(StringUtils.isEmpty(templateId)) {
            LOGGER.info("method[findTaskCountByTemplateId] param templateId is empty");
            return 0;
        }
        return TaskObjectBiz.findTaskCountByTemplateId(templateId);
    }


}
