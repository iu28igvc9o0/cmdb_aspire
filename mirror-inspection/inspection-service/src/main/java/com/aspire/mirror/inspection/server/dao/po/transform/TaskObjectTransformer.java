package com.aspire.mirror.inspection.server.dao.po.transform;
import com.aspire.mirror.inspection.api.dto.model.TaskObjectDTO;

import com.aspire.mirror.inspection.server.dao.po.TaskObject;
import com.google.common.collect.Lists;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 任务设备转换类
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.inspection.server.dao.po.transform
 * 类名称:    TaskObjectTransformer.java
 * 类描述:    任务设备转换类
 * 创建人:    JinSu
 * 创建时间:  2018/8/11 14:00
 * 版本:      v1.0
 */
@NoArgsConstructor
public class TaskObjectTransformer {
    /**
     * 任务设备PO转DTO
     * @param TaskObject 任务设备PO对象
     * @return TaskObjectDTO DTO对象
     */
    public static TaskObjectDTO fromPo(final TaskObject taskObject) {
        if (null == taskObject) {
            return null;
        }

        TaskObjectDTO taskObjectDTO = new TaskObjectDTO();
        taskObjectDTO.setTaskObjectId(taskObject.getTaskObjectId());
        taskObjectDTO.setTemplateId(taskObject.getTemplateId());
        taskObjectDTO.setTaskId(taskObject.getTaskId());
        taskObjectDTO.setObjectType(taskObject.getObjectType());
        taskObjectDTO.setObjectId(taskObject.getObjectId());

        return taskObjectDTO;
    }
    /**
     * 任务设备集合PO转DTO
     * @param listTaskObject 任务设备PO对象集合
     * @return TaskObjectDTO DTO对象
     */
    public static List<TaskObjectDTO> fromPo(final List<TaskObject> listTaskObject) {
        if (CollectionUtils.isEmpty(listTaskObject)) {
            return Lists.newArrayList();
        }
        List<TaskObjectDTO> listTaskObjectDTO = Lists.newArrayList();

        for (TaskObject taskObject : listTaskObject) {
        	listTaskObjectDTO.add(TaskObjectTransformer.fromPo(taskObject));
        }
        return listTaskObjectDTO;
    }
    /**
     * 将任务设备DTO转PO
     *
     * @param  TaskObjectDTO DTO实体类
     * @return TaskObject PO实体
     */
    public static TaskObject toPo(final TaskObjectDTO taskObjectDTO) {
        if (null == taskObjectDTO) {
            return null;
        }
        TaskObject taskObject = new TaskObject();
        taskObject.setObjectId(taskObjectDTO.getObjectId());
        taskObject.setTaskId(taskObjectDTO.getTaskId());
        taskObject.setObjectType(taskObjectDTO.getObjectType());
        taskObject.setTaskObjectId(taskObjectDTO.getTaskObjectId());
        taskObject.setTemplateId(taskObjectDTO.getTemplateId());
        return taskObject;
    }

    /**
     * 将任务设备集合DTO转PO
     *
     * @param  listTaskObjectDTO DTO实体类集合
     * @return TaskObject PO实体
     */
    public static List<TaskObject> toPo(final List<TaskObjectDTO> listTaskObjectDTO) {
        if (CollectionUtils.isEmpty(listTaskObjectDTO)) {
            return Lists.newArrayList();
        }
        List<TaskObject> listTaskObject = Lists.newArrayList();

        for (TaskObjectDTO TaskObjectdTO : listTaskObjectDTO) {
            listTaskObject.add(TaskObjectTransformer.toPo(TaskObjectdTO));
        }
        return listTaskObject;
    }
}
