package com.aspire.mirror.inspection.server.biz.impl ;

import java.util.Collections;
import java.util.List;

import com.aspire.mirror.inspection.server.dao.TaskReceiverDao;
import com.aspire.mirror.inspection.api.dto.model.TaskReceiverDTO;
import com.aspire.mirror.inspection.server.biz.TaskReceiverBiz;
import com.aspire.mirror.inspection.server.dao.po.TaskReceiver;
import com.aspire.mirror.inspection.server.dao.po.transform.TaskReceiverTransformer;

import org.apache.commons.lang3.ArrayUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.util.CollectionUtils;

/**
 * inspection_task_receiver业务层实现类
 *
 * 项目名称:  微服务运维平台
 * 包:       com.aspire.mirror.inspection.server.biz.impl
 * 类名称:    TaskReceiverBizImpl.java
 * 类描述:    inspection_task_receiver业务逻辑层实现类
 * 创建人:    ZhangSheng
 * 创建时间:  2018-07-27 13:48:08
 */
@Service
public class TaskReceiverBizImpl implements TaskReceiverBiz {

    /**
     * 新增数据
     *
     * @param taskReceiverDTO inspection_task_receiverDTO对象
     * @return int 新增数据条数
     */
    public int insert(final TaskReceiverDTO taskReceiverDTO){
        if(null == taskReceiverDTO){
            LOGGER.error("method[insert] param[taskReceiverDTO] is null");
            throw new RuntimeException("param[taskReceiverDTO] is null");
        }
        TaskReceiver taskReceiver = TaskReceiverTransformer.toPo(taskReceiverDTO);
        return taskReceiverDao.insert(taskReceiver);
    }

    /**
     * 批量新增inspection_task_receiver数据
     *
     * @param listTaskReceiverDTO inspection_task_receiverDTO集合对象
     * @return int 新增数据条数
     */
    public int insertByBatch(final List<TaskReceiverDTO> listTaskReceiverDTO){
        if (CollectionUtils.isEmpty(listTaskReceiverDTO)) {
            LOGGER.error("method[insertByBatch] param[listTaskReceiverDTO] is null");
            throw new RuntimeException("param[listTaskReceiverDTO] is null");
        }
        List<TaskReceiver> listTaskReceiver = TaskReceiverTransformer.toPo(listTaskReceiverDTO);
        return taskReceiverDao.insertByBatch(listTaskReceiver);
    }
    /**
     * 根据主键删除数据
     *
     * @param receiverId 主键
     * @return int 删除数据条数
     */
    public int deleteByPrimaryKey(final String receiverId){
        if (StringUtils.isEmpty(receiverId)) {
            LOGGER.error("method[eleteByPrimaryKey] param[receiverId] is null");
            throw new RuntimeException("param[receiverId] is null");
        }
        return taskReceiverDao.deleteByPrimaryKey(receiverId);
    }
    /**
    * 根据主键数组批量删除数据
    *
    * @param receiverIdArrays 主键数组
    * @return int 删除数据条数
    */
    public int deleteByPrimaryKeyArrays(final String[] receiverIdArrays){
        if (ArrayUtils.isEmpty(receiverIdArrays)) {
            LOGGER.error("method[deleteByPrimaryKeyArrays] param[receiverIdArrays] is null");
            throw new RuntimeException("param[receiverIdArrays] is null");
        }
        return taskReceiverDao.deleteByPrimaryKeyArrays(receiverIdArrays);
    }

    /**
     * 根据条件删除数据
     *
     * @param taskReceiverDTO  inspection_task_receiverDTO对象
     * @return int 删除数据条数
     */
    public int delete(final TaskReceiverDTO taskReceiverDTO){
        if(null == taskReceiverDTO){
            LOGGER.error("method[delete] param[taskReceiverDTO] is null");
            throw new RuntimeException("param[taskReceiverDTO] is null");
        }
        TaskReceiver taskReceiver = TaskReceiverTransformer.toPo(taskReceiverDTO);
        return taskReceiverDao.delete(taskReceiver);
    }

    /**
     * 根据参数选择性更新数据
     *
     * @param taskReceiverDTO inspection_task_receiverDTO对象
     * @return int 数据条数
     */
    public int updateByPrimaryKeySelective(final TaskReceiverDTO taskReceiverDTO){
        if(null == taskReceiverDTO){
            LOGGER.error("method[updateByPrimaryKey] param[taskReceiverDTO] is null");
            throw new RuntimeException("param[taskReceiverDTO] is null");
        }
        TaskReceiver taskReceiver = TaskReceiverTransformer.toPo(taskReceiverDTO);
        return taskReceiverDao.updateByPrimaryKeySelective(taskReceiver);
    }

    /**
     * 根据主键更新数据
     *
     * @param taskReceiverDTO inspection_task_receiverDTO对象
     * @return int 数据条数
     */
    public int updateByPrimaryKey(final TaskReceiverDTO taskReceiverDTO){
        if(null == taskReceiverDTO){
            LOGGER.error("method[updateByPrimaryKey] param[taskReceiverDTO] is null");
            throw new RuntimeException("param[taskReceiverDTO] is null");
        }
        if (StringUtils.isEmpty(taskReceiverDTO.getReceiverId())) {
            LOGGER.warn("method[updateByPrimaryKey] param[receiverId] is null");
            throw new RuntimeException("param[receiverId] is null");
        }
        TaskReceiver taskReceiver = TaskReceiverTransformer.toPo(taskReceiverDTO);
        return taskReceiverDao.updateByPrimaryKey(taskReceiver);
    }

    /**
     * 根据主键查询
     *
     * @param receiverId 主键
     * @return TaskReceiverDTO 返回对象
     */
    public TaskReceiverDTO selectByPrimaryKey(final String receiverId){
        TaskReceiver taskReceiver = taskReceiverDao.selectByPrimaryKey(receiverId);
        if (StringUtils.isEmpty(receiverId)) {
            LOGGER.warn("method[selectByPrimaryKey] param[receiverId] is null");
            return null;
        }
        return TaskReceiverTransformer.fromPo(taskReceiver);
    }
    /**
     * 根据主键数组查询
     *
     * @param receiverIdArrays 主键数组
     * @return List<TaskReceiverDTO> 返回集合对象
     */
    public List<TaskReceiverDTO> selectByPrimaryKeyArrays(final String[] receiverIdArrays){
        if (ArrayUtils.isEmpty(receiverIdArrays)) {
            LOGGER.warn("method[selectByPrimaryKeyArrays] param[receiverIdArrays] is null");
            return Collections.emptyList();
        }
        List<TaskReceiver> listTaskReceiver = taskReceiverDao.selectByPrimaryKeyArrays(receiverIdArrays);
        return TaskReceiverTransformer.fromPo(listTaskReceiver);
    }
    /**
     * 根据dto实体查询列表
     *
     * @param taskReceiverDTO inspection_task_receiverDTO对象
     * @return List<TaskReceiver>  返回集合
     */
    public List<TaskReceiverDTO> select(final TaskReceiverDTO taskReceiverDTO){
        if(null == taskReceiverDTO){
            LOGGER.warn("select Object taskReceiverDTO is null");
            return Collections.emptyList();
        }
        TaskReceiver taskReceiver = TaskReceiverTransformer.toPo(taskReceiverDTO);
        List<TaskReceiver> listTaskReceiver = taskReceiverDao.select(taskReceiver);
        return TaskReceiverTransformer.fromPo(listTaskReceiver);
    }

    /**
     * 根据DTO实体查询条数
     *
     * @param taskReceiverDTO inspection_task_receiverDTO对象
     * @return int 数据条数
     */
    public int selectCount(final TaskReceiverDTO taskReceiverDTO){
        if(null == taskReceiverDTO){
            LOGGER.warn("selectCount Object taskReceiverDTO is null");
        }
        TaskReceiver taskReceiver = TaskReceiverTransformer.toPo(taskReceiverDTO);
        return taskReceiverDao.selectCount(taskReceiver);
    }

    /** slf4j*/
    private static final Logger   LOGGER = LoggerFactory.getLogger(TaskReceiverBizImpl.class);

    /** 数据访问层接口*/
    @Autowired
    private TaskReceiverDao taskReceiverDao;

} 
