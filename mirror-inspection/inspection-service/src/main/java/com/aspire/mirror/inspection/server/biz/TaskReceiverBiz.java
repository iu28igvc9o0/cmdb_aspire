package com.aspire.mirror.inspection.server.biz ;

import java.util.List;

import com.aspire.mirror.inspection.server.dao.po.TaskReceiver;
import com.aspire.mirror.inspection.api.dto.model.TaskReceiverDTO;

/**
 * inspection_task_receiver业务层接口
 *
 * 项目名称: 微服务运维平台
 * 包:      com.aspire.mirror.inspection.server.biz
 * 类名称:   TaskReceiverBiz.java
 * 类描述:   inspection_task_receiver业务逻辑层接口
 * 创建人:   ZhangSheng
 * 创建时间: 2018-07-27 13:48:08
 */
public interface TaskReceiverBiz {

    /**
     * 新增数据
     *
     * @param taskReceiverDTO inspection_task_receiverDTO对象
     * @return int 新增数据条数
     */
    int insert(TaskReceiverDTO taskReceiverDTO);

    /**
     * 批量新增inspection_task_receiver数据
     *
     * @param listTaskReceiverDTO inspection_task_receiverDTO集合对象
     * @return int 新增数据条数
     */
    int insertByBatch(List<TaskReceiverDTO> listTaskReceiverDTO);
    /**
     * 根据主键删除数据
     *
     * @param receiverId 主键
     * @return int 删除数据条数
     */
    int deleteByPrimaryKey(String receiverId);

    /**
     * 根据主键数组批量删除数据
     *
     * @param receiverIdArrays 主键数组
     * @return int 删除数据条数
     */
    int deleteByPrimaryKeyArrays(String[] receiverIdArrays);

    /**
     * 根据条件删除数据
     *
     * @param taskReceiverDTO  inspection_task_receiverDTO对象
     * @return int 删除数据条数
     */
    int delete(TaskReceiverDTO taskReceiverDTO);

    /**
     * 根据参数选择性更新数据
     *
     * @param taskReceiverDTO inspection_task_receiverDTO对象
     * @return int 数据条数
     */
    int updateByPrimaryKeySelective(TaskReceiverDTO taskReceiverDTO);

    /**
     * 根据主键更新数据
     *
     * @param taskReceiverDTO inspection_task_receiverDTO对象
     * @return int 数据条数
     */
    int updateByPrimaryKey(TaskReceiverDTO taskReceiverDTO);

    /**
     * 根据主键查询
     *
     * @param receiverId 主键
     * @return TaskReceiverDTO 返回对象
     */
    TaskReceiverDTO selectByPrimaryKey(String receiverId);

    /**
     * 根据主键数组查询
     *
     * @param receiverIdArrays 主键数组
     * @return List<TaskReceiverDTO> 返回集合对象
     */
    List<TaskReceiverDTO> selectByPrimaryKeyArrays(String[] receiverIdArrays);
    /**
     * 根据dto实体查询列表
     *
     * @param taskReceiverDTO inspection_task_receiverDTO对象
     * @return List<TaskReceiver>  返回集合
     */
    List<TaskReceiverDTO> select(TaskReceiverDTO taskReceiverDTO);

    /**
     * 根据DTO实体查询条数
     *
     * @param taskReceiverDTO inspection_task_receiverDTO对象
     * @return int 数据条数
     */
    int selectCount(TaskReceiverDTO taskReceiverDTO);

} 
