package com.aspire.mirror.inspection.server.dao;


import com.aspire.mirror.inspection.server.dao.po.TaskObject;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 业务设备Dao
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.inspection.server.dao
 * 类名称:    TaskObjectDao.java
 * 类描述:    业务设备Dao
 * 创建人:    JinSu
 * 创建时间:  2018/8/11 12:59
 * 版本:      v1.0
 */
@Mapper
public interface TaskObjectDao {
    /**
     * 根据任务ID删除任务设备数据
     * @param taskId 任务Id
     * @return int 删除条数
     */
    int deleteByTaskId(String taskId);

    /**
     * 批量创建任务设备数据
     * @param TaskObjectList
     * @return
     */
    int insertByBatch(List<TaskObject> TaskObjectList);

    /**
     * 根据任务ID查询模板设备信息
     * @param taskId 任务ID
     * @return
     */
    List<TaskObject> selectByTaskId(String taskId);

    /**
     * 根据模板ID查询任务数
     * @param templateId 模板ID
     * @return 任务数量
     */
    Integer findTaskCountByTemplateId(String templateId);

    /**
     * 根据任务ID集合查询任务列表
     * @param taskIdArrays 任务Id集合
     * @return 任务设备列表
     */
    List<TaskObject> findByTaskIdArrays(String[] taskIdArrays);

    Integer getDeviceNumByTaskId(String taskId);
}
