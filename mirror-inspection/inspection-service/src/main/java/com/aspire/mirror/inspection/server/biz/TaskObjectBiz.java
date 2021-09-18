package com.aspire.mirror.inspection.server.biz;

import java.util.List;

import com.aspire.mirror.inspection.api.dto.model.TaskObjectDTO;

/**
 * 任务设备业务层接口
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.inspection.server.biz
 * 类名称:    TaskObjectBiz.java
 * 类描述:    任务设备业务层接口
 * 创建人:    JinSu
 * 创建时间:  2018/8/11 12:54
 * 版本:      v1.0
 */
public interface TaskObjectBiz {

    /**
     * 根据任务ID删除业务设备数据
     * @param taskId
     * @return
     */
    int deleteByTaskId(String taskId);

    /**
     * 批量创建TaskObject
     * @param deviceDTOList
     * @return int 创建条数
     */
    int insertBatch(List<TaskObjectDTO> deviceDTOList);

    /**
     * 根据任务ID查询模板设备信息
     * @param taskId 任务ID
     * @return
     */
    List<TaskObjectDTO> findByTaskId(String taskId);

    /**
     * 根据模板ID查询任务数
     * @param templateId 模板ID
     * @return
     */
    Integer findTaskCountByTemplateId(String templateId);

    /**
     * 根据任务ID集合查询任务列表
     * @param taskIdArrays 任务Id集合
     * @return 任务设备列表
     */
    List<TaskObjectDTO> findByTaskIdArrays(String[] taskIdArrays);
}
