package com.aspire.mirror.inspection.server.biz.impl;

import com.aspire.mirror.inspection.api.dto.model.TaskObjectDTO;
import com.aspire.mirror.inspection.server.biz.TaskObjectBiz;
import com.aspire.mirror.inspection.server.dao.TaskObjectDao;
import com.aspire.mirror.inspection.server.dao.po.TaskObject;
import com.aspire.mirror.inspection.server.dao.po.transform.TaskObjectTransformer;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * 任务设备业务层实现
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.inspection.server.biz.impl
 * 类名称:    TaskObjectBizImpl.java
 * 类描述:    任务设备业务层实现
 * 创建人:    JinSu
 * 创建时间:  2018/8/11 12:56
 * 版本:      v1.0
 */
@Service
public class TaskObjectBizImpl implements TaskObjectBiz {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskObjectBizImpl.class);
    /**
     * 根据任务Id删除任务设备数据
     * @param taskId
     * @return int 删除数量
     */
    @Autowired
    private TaskObjectDao TaskObjectDao;
    @Override
    public int deleteByTaskId(String taskId) {
        return TaskObjectDao.deleteByTaskId(taskId);
    }
    /**
     * 批量创建TaskObject
     * @param deviceDTOList
     * @return int 创建条数
     */
    @Override
    public int insertBatch(List<TaskObjectDTO> deviceDTOList) {
        for (TaskObjectDTO TaskObjectDTO : deviceDTOList) {
            TaskObjectDTO.setTaskObjectId(UUID.randomUUID().toString());
        }
        List<TaskObject> TaskObjectList = TaskObjectTransformer.toPo(deviceDTOList);
        return TaskObjectDao.insertByBatch(TaskObjectList);
    }
    /**
     * 根据任务ID查询模板设备信息
     * @param taskId 任务ID
     * @return
     */
    @Override
    public List<TaskObjectDTO> findByTaskId(String taskId) {
        List<TaskObject> TaskObjectList = TaskObjectDao.selectByTaskId(taskId);
        return TaskObjectTransformer.fromPo(TaskObjectList);
    }

    /**
     * 根据模板ID查询任务数
     * @param templateId 模板ID
     * @return 任务数量
     */
    @Override
    public Integer findTaskCountByTemplateId(String templateId) {
        return TaskObjectDao.findTaskCountByTemplateId(templateId);
    }

    /**
     * 根据任务ID集合查询任务列表
     * @param taskIdArrays 任务Id集合
     * @return 任务设备列表
     */
    @Override
    public List<TaskObjectDTO> findByTaskIdArrays(String[] taskIdArrays) {
        if (ArrayUtils.isEmpty(taskIdArrays)) {
            LOGGER.warn("method[findByTaskIdArrays] param[taskIdArrays] is null");
            return Collections.emptyList();
        }
        List<TaskObject> listTemplate = TaskObjectDao.findByTaskIdArrays(taskIdArrays);
        return TaskObjectTransformer.fromPo(listTemplate);
    }
}
