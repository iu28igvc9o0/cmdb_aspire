package com.aspire.mirror.inspection.server.dao.po.transform ;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.util.CollectionUtils;

import com.aspire.mirror.inspection.server.dao.po.TaskReceiver;
import com.aspire.mirror.inspection.api.dto.model.TaskReceiverDTO;

import java.util.List;
import java.util.Map;

/**
 * inspection_task_receiver对象转换类
 *
 * 项目名称:  微服务运维平台
 * 包:       com.aspire.mirror.inspection.server.dao.po.transform
 * 类名称:    TaskReceiverTransformer.java
 * 类描述:    inspection_task_receiver对应的PO与DTO的转换类
 * 创建人:    ZhangSheng
 * 创建时间:  2018-07-27 13:48:08
 */
public final class TaskReceiverTransformer  {

    private TaskReceiverTransformer(){
    }

   /**
    * 将inspection_task_receiverPO实体转换为inspection_task_receiverDTO实体
    *
    * @param  taskReceiver inspection_task_receiverPO实体
    * @return TaskReceiverDTO inspection_task_receiverDTO实体
    */
    public static TaskReceiverDTO fromPo(final TaskReceiver taskReceiver) {
        if (null == taskReceiver) {
            return null;
        }
        
        TaskReceiverDTO taskReceiverDTO = new TaskReceiverDTO();
        taskReceiverDTO.setReceiverId(taskReceiver.getReceiverId());
        taskReceiverDTO.setTaskId(taskReceiver.getTaskId());
        taskReceiverDTO.setUserId(taskReceiver.getUserId());

        return taskReceiverDTO;
    }

    /**
     * 将inspection_task_receiver业务实体对象集合转换为inspection_task_receiver持久化对象集合
     *
     * @param listTaskReceiver inspection_task_receiver业务实体对象集合
     * @return List<TaskReceiverDTO> inspection_task_receiver持久化对象集合
     */
    public static List<TaskReceiverDTO> fromPo(final List<TaskReceiver> listTaskReceiver) {
        if (CollectionUtils.isEmpty(listTaskReceiver)) {
            return Lists.newArrayList();
        }
        List<TaskReceiverDTO> listTaskReceiverDTO = Lists.newArrayList();

        for (TaskReceiver taskReceiver : listTaskReceiver) {
            listTaskReceiverDTO.add(TaskReceiverTransformer.fromPo(taskReceiver));
        }
        return listTaskReceiverDTO;
    }

   /**
    * 将inspection_task_receiverDTO实体转换为inspection_task_receiverPO实体
    *
    * @param  taskReceiverDTO inspection_task_receiverDTO实体类
    * @return TaskReceiver inspection_task_receiverPO实体
    */
    public static TaskReceiver toPo(final TaskReceiverDTO taskReceiverDTO) {
        if (null == taskReceiverDTO) {
            return null;
        }

        TaskReceiver taskReceiver = new TaskReceiver();
        taskReceiver.setReceiverId(taskReceiverDTO.getReceiverId());
        taskReceiver.setTaskId(taskReceiverDTO.getTaskId());
        taskReceiver.setUserId(taskReceiverDTO.getUserId());

        return taskReceiver;
    }

    /**
     * 将inspection_task_receiver业务实体对象集合转换为inspection_task_receiver持久化对象集合
     *
     * @param listTaskReceiverDTO inspection_task_receiver业务实体对象集合
     * @return List<TaskReceiver> inspection_task_receiver持久化对象集合
     */
    public static List<TaskReceiver> toPo(final List<TaskReceiverDTO> listTaskReceiverDTO) {
        if (CollectionUtils.isEmpty(listTaskReceiverDTO)) {
            return Lists.newArrayList();
        }
        List<TaskReceiver> listTaskReceiver = Lists.newArrayList();

        for (TaskReceiverDTO taskReceiverdTO : listTaskReceiverDTO) {
            listTaskReceiver.add(TaskReceiverTransformer.toPo(taskReceiverdTO));
        }
        return listTaskReceiver;
    }
    /**
     * 将inspection_task_receiver业务实体对象集合转换为Map
     *
     * @param listTaskReceiverDTO inspection_task_receiver业务实体对象集合
     * @return Map<String, TaskReceiverDTO> Map[key=String|value=TaskReceiverDTO]
     */
    public static Map<String, TaskReceiverDTO> toDTOMap(final List<TaskReceiverDTO> listTaskReceiverDTO) {
        if (CollectionUtils.isEmpty(listTaskReceiverDTO)) {
            return Maps.newHashMap();
        }
        Map<String, TaskReceiverDTO> taskReceiverDTOMaps = Maps.newHashMap();

        for (TaskReceiverDTO taskReceiverDTO : listTaskReceiverDTO) {
            taskReceiverDTOMaps.put(taskReceiverDTO.getReceiverId(), taskReceiverDTO);
        }
        return taskReceiverDTOMaps;
    }

    /**
     * 将inspection_task_receiver业务实体对象集合中的主键进行提取并封装成数组
     *
     * @param listTaskReceiverDTO inspection_task_receiver业务实体对象集合
     * @return String[] 主键数组
     */
    public static String[] toDTOPrimaryKeyArrays(final List<TaskReceiverDTO> listTaskReceiverDTO) {
        if (CollectionUtils.isEmpty(listTaskReceiverDTO)) {
            return null;
        }
        int size = listTaskReceiverDTO.size();
        String[] receiverIdArrays = new String[size];
        for (int i = 0 ;i< size; i++) {
            receiverIdArrays[i] = listTaskReceiverDTO.get(i).getReceiverId();
        }
        return receiverIdArrays;
        }
} 
