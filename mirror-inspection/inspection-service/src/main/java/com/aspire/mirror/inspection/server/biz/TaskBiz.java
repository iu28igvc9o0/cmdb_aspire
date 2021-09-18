package com.aspire.mirror.inspection.server.biz ;

import java.util.List;

import com.aspire.mirror.inspection.server.dao.po.Task;
import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.inspection.api.dto.TaskPageRequest;
import com.aspire.mirror.inspection.api.dto.model.TaskDTO;
import com.aspire.mirror.inspection.api.dto.model.TaskRunDTO;

/**
 * inspection_task业务层接口
 *
 * 项目名称: 微服务运维平台
 * 包:      com.aspire.mirror.inspection.server.biz
 * 类名称:   TaskBiz.java
 * 类描述:   inspection_task业务逻辑层接口
 * 创建人:   ZhangSheng
 * 创建时间: 2018-07-27 13:48:06
 */
public interface TaskBiz {
    int updateByPrimaryKey(TaskDTO taskDTO);
    /**
     * 根据主键删除数据
     * @param taskId 主键
     * @return int 删除数据条数
     */
    int deleteByPrimaryKey(String taskId);

    /**
     * 根据主键数组批量删除数据
     * @param taskIdArrays 主键数组
     * @return int 删除数据条数
     */
    int deleteByPrimaryKeyArrays(String[] taskIdArrays);
    /**
     * 新增task
     * @param taskDTO
     * @return
     */
    String insert(TaskDTO taskDTO);
    
    /**
     * 根据主键查询
     * @param taskId 主键
     * @return TaskDTO 返回对象
     */
    TaskDTO selectByPrimaryKey(String taskId);

    /**
     * 根据任务分页请求对象查询
     *
     * @param taskIdArrays 主键数组
     * @return List<TaskDTO> 返回集合对象
     */
    List<TaskDTO> list(PageRequest taskPageRequest);

    /**
     * 根据page实体查询条数
     * @param taskDTO inspection_taskDTO对象
     * @return int 数据条数
     */
    int selectCount(PageRequest pageRequest);
    
    /**
     * 任务运行页面列表
     * @param pageRequest
     * @return
     */
	List<TaskRunDTO> runList(PageRequest pageRequest);
	
	/**
	 * 运行任务页面统计
	 * @param pageRequest
	 * @return
	 */
	int selectTaskRunCount(PageRequest pageRequest);

    Integer getDeviceNumByTaskId(final String taskId);
} 
