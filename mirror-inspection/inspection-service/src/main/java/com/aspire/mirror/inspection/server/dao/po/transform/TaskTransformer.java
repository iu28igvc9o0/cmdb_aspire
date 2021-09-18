package com.aspire.mirror.inspection.server.dao.po.transform ;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.util.CollectionUtils;

import com.aspire.mirror.inspection.server.dao.TaskDao;
import com.aspire.mirror.inspection.server.dao.po.Task;
import com.aspire.mirror.inspection.server.dao.po.TaskRun;
import com.aspire.mirror.inspection.api.dto.model.TaskDTO;
import com.aspire.mirror.inspection.api.dto.model.TaskRunDTO;

import java.util.List;
import java.util.Map;

/**
 * inspection_task对象转换类
 *
 * 项目名称:  微服务运维平台
 * 包:       com.aspire.mirror.inspection.server.dao.po.transform
 * 类名称:    TaskTransformer.java
 * 类描述:    inspection_task对应的PO与DTO的转换类
 * 创建人:    ZhangSheng
 * 创建时间:  2018-07-27 13:48:06
 */
public final class TaskTransformer  {

    private TaskTransformer(){
    }

   /**
    * 将inspection_taskPO实体转换为inspection_taskDTO实体
    *
    * @param  task inspection_taskPO实体
    * @return TaskDTO inspection_taskDTO实体
    */
    public static TaskDTO fromPo(final Task task) {
        if (null == task) {
            return null;
        }
        
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskId(task.getTaskId());
        taskDTO.setName(task.getName());
        taskDTO.setType(task.getType());
        taskDTO.setCycle(task.getCycle());
        taskDTO.setExecTime(task.getExecTime());
        taskDTO.setStatus(task.getStatus());
        taskDTO.setCreateTime(task.getCreateTime());
        taskDTO.setUpdateTime(task.getUpdateTime());
        taskDTO.setRecentRunTime(task.getRecentRunTime());
        taskDTO.setCreater(task.getCreater());
        taskDTO.setUpdater(task.getUpdater());
        return taskDTO;
    }
    
    
    /**
     * 将TaskRun实体转换为TaskRunDTO实体
     * @param  TaskRun实体
     * @return TaskRunDTO实体
     */
     public static TaskRunDTO fromPo(final TaskRun taskRun) {
         if (null == taskRun) {
             return null;
         }
         TaskRunDTO taskRunDTO = new TaskRunDTO();
         taskRunDTO.setTaskId(taskRun.getTaskId());
         taskRunDTO.setName(taskRun.getName());
         taskRunDTO.setStatus(taskRun.getStatus());
         taskRunDTO.setExecTimeStart(taskRun.getExecTimeStart());
         taskRunDTO.setExecTimeEnd(taskRun.getExecTimeEnd());
         return taskRunDTO;
     }

     
    /**
     * 将inspection_task业务实体对象集合转换为inspection_task持久化对象集合
     *
     * @param listTask inspection_task业务实体对象集合
     * @return List<TaskDTO> inspection_task持久化对象集合
     */
    public static List<TaskDTO> fromPo(final List<Task> listTask) {
        if (CollectionUtils.isEmpty(listTask)) {
            return Lists.newArrayList();
        }
        List<TaskDTO> listTaskDTO = Lists.newArrayList();

        for (Task task : listTask) {
            listTaskDTO.add(TaskTransformer.fromPo(task));
        }
        return listTaskDTO;
    }

   /**
    * 将inspection_taskDTO实体转换为inspection_taskPO实体
    *
    * @param  taskDTO inspection_taskDTO实体类
    * @return Task inspection_taskPO实体
    */
    public static Task toPo(final TaskDTO taskDTO) {
        if (null == taskDTO) {
            return null;
        }
        Task task = new Task();
        task.setTaskId(taskDTO.getTaskId());
        task.setName(taskDTO.getName());
        task.setType(taskDTO.getType());
        task.setCycle(taskDTO.getCycle());
        task.setExecTime(taskDTO.getExecTime());
        task.setStatus(taskDTO.getStatus());
        task.setCreateTime(taskDTO.getCreateTime());
        task.setUpdateTime(taskDTO.getUpdateTime());
        task.setCreater(taskDTO.getCreater());
        task.setUpdater(taskDTO.getUpdater());
        return task;
    }

    /**
     * 将inspection_task业务实体对象集合转换为inspection_task持久化对象集合
     *
     * @param listTaskDTO inspection_task业务实体对象集合
     * @return List<Task> inspection_task持久化对象集合
     */
    public static List<Task> toPo(final List<TaskDTO> listTaskDTO) {
        if (CollectionUtils.isEmpty(listTaskDTO)) {
            return Lists.newArrayList();
        }
        List<Task> listTask = Lists.newArrayList();

        for (TaskDTO taskdTO : listTaskDTO) {
            listTask.add(TaskTransformer.toPo(taskdTO));
        }
        return listTask;
    }
    /**
     * 将inspection_task业务实体对象集合转换为Map
     *
     * @param listTaskDTO inspection_task业务实体对象集合
     * @return Map<String, TaskDTO> Map[key=String|value=TaskDTO]
     */
    public static Map<String, TaskDTO> toDTOMap(final List<TaskDTO> listTaskDTO) {
        if (CollectionUtils.isEmpty(listTaskDTO)) {
            return Maps.newHashMap();
        }
        Map<String, TaskDTO> taskDTOMaps = Maps.newHashMap();

        for (TaskDTO taskDTO : listTaskDTO) {
            taskDTOMaps.put(taskDTO.getTaskId(), taskDTO);
        }
        return taskDTOMaps;
    }

    /**
     * 将inspection_task业务实体对象集合中的主键进行提取并封装成数组
     *
     * @param listTaskDTO inspection_task业务实体对象集合
     * @return String[] 主键数组
     */
    public static String[] toDTOPrimaryKeyArrays(final List<TaskDTO> listTaskDTO) {
        if (CollectionUtils.isEmpty(listTaskDTO)) {
            return null;
        }
        int size = listTaskDTO.size();
        String[] taskIdArrays = new String[size];
        for (int i = 0 ;i< size; i++) {
            taskIdArrays[i] = listTaskDTO.get(i).getTaskId();
        }
        return taskIdArrays;
    }
} 
