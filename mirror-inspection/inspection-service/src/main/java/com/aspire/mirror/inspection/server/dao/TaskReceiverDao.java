package com.aspire.mirror.inspection.server.dao ;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.mirror.inspection.server.dao.po.TaskReceiver;

/**
 * inspection_task_receiver数据访问层接口
 *
 * 项目名称:  微服务运维平台
 * 包:       com.aspire.mirror.inspection.server.dao
 * 类名称:    TaskReceiverDao.java
 * 类描述:    inspection_task_receiver数据访问层接口
 * 创建人:    ZhangSheng
 * 创建时间:  2018-07-27 13:48:08
 */
@Mapper
public interface TaskReceiverDao {
	
	/**
	 * 根据任务ID查询返回userId
	 * @param taskId
	 * @return
	 */
	List<String> selectByTaskId(@Param("taskId")String taskId);
	
	/**
	 * 根据任务ID批量删除
	 * @param taskIds
	 * @return
	 */
	int deleteByTaskIds(String [] taskIdArrays);
	
	/**
	 * 根据任务ID删除
	 * @param taskId
	 * @return
	 */
	
	int deleteByTaskId(@Param(value="taskId")String  taskId);

    /**
     * 新增数据
     *
     * @param taskReceiver inspection_task_receiverPO对象
     * @return int 新增数据条数
     */
    int insert(TaskReceiver taskReceiver);

    /**
     * 批量新增inspection_task_receiver数据
     *
     * @param listTaskReceiver inspection_task_receiverPO集合对象
     * @return int 新增数据条数
     */
    int insertByBatch(List<TaskReceiver> listTaskReceiver);
    /**
     * 根据主键删除数据
     *
     * @param receiverId 主键
     * @return int 删除数据条数
     */
    int deleteByPrimaryKey(@Param(value="receiverId")String receiverId);

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
     * @param taskReceiver  inspection_task_receiverPO对象
     * @return int 删除数据条数
     */
    int delete(TaskReceiver taskReceiver);

    /**
     * 根据参数选择性更新数据
     *
     * @param taskReceiver inspection_task_receiverPO对象
     * @return int 数据条数
     */
    int updateByPrimaryKeySelective(TaskReceiver taskReceiver);

    /**
     * 根据主键更新数据
     *
     * @param taskReceiver inspection_task_receiverPO对象
     * @return int 数据条数
     */
    int updateByPrimaryKey(TaskReceiver taskReceiver);

    /**
     * 根据主键查询
     *
     * @param receiverId 主键
     * @return TaskReceiver 返回对象
     */
    TaskReceiver selectByPrimaryKey(@Param(value="receiverId")String receiverId);

    /**
     * 根据主键数组查询
     *
     * @param receiverIdArrays 主键数组
     * @return List<TaskReceiver> 返回集合对象
     */
    List<TaskReceiver> selectByPrimaryKeyArrays(String[] receiverIdArrays);
    /**
     * 根据po实体查询列表
     *
     * @param taskReceiver inspection_task_receiverPO对象
     * @return List<TaskReceiver>  返回集合
     */
    List<TaskReceiver> select(TaskReceiver taskReceiver);

    /**
     * 根据po实体查询条数
     *
     * @param taskReceiver inspection_task_receiverPO对象
     * @return int 数据条数
     */
    int selectCount(TaskReceiver taskReceiver);

	

} 
