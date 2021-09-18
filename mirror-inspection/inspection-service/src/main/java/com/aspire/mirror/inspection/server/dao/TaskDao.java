package com.aspire.mirror.inspection.server.dao ;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.inspection.server.dao.po.Task;

/**
 * inspection_task数据访问层接口
 *
 * 项目名称:  微服务运维平台
 * 包:       com.aspire.mirror.inspection.server.dao
 * 类名称:    TaskDao.java
 * 类描述:    inspection_task数据访问层接口
 * 创建人:    ZhangSheng
 * 创建时间:  2018-07-27 13:48:06
 */
@Mapper
public interface TaskDao {

  

    /**
     * 批量新增inspection_task数据
     *
     * @param listTask inspection_taskPO集合对象
     * @return int 新增数据条数
     */
    int insertByBatch(List<Task> listTask);
    /**
     * 根据主键删除数据
     *
     * @param taskId 主键
     * @return int 删除数据条数
     */
    int deleteByPrimaryKey(@Param(value="taskId")String taskId);

    /**
     * 根据主键数组批量删除数据
     *
     * @param taskIdArrays 主键数组
     * @return int 删除数据条数
     */
    int deleteByPrimaryKeyArrays(String[] taskIdArrays);

    /**
     * 根据条件删除数据
     *
     * @param task  inspection_taskPO对象
     * @return int 删除数据条数
     */
    int delete(Task task);

    /**
     * 根据参数选择性更新数据
     *
     * @param task inspection_taskPO对象
     * @return int 数据条数
     */
    int updateByPrimaryKeySelective(Task task);

    /**
     * 根据主键更新数据
     *
     * @param task inspection_taskPO对象
     * @return int 数据条数
     */
    int updateByPrimaryKey(Task task);

    /**
     * 根据主键查询
     *
     * @param taskId 主键
     * @return Task 返回对象
     */
    Task selectByPrimaryKey(@Param(value="taskId")String taskId);

    /**
     * 根据主键数组查询
     *
     * @param taskIdArrays 主键数组
     * @return List<Task> 返回集合对象
     */
    List<Task> selectByPrimaryKeyArrays(String[] taskIdArrays);
    /**
     * 根据po实体查询列表
     *
     * @param task inspection_taskPO对象
     * @return List<Task>  返回集合
     */
    List<Task> select(Task task);
  //----------------------以下手写-------------------------->
    /**
     * 新增数据
     *
     * @param task inspection_taskPO对象
     * @return 
     * @return int 新增数据条数
     */
    int insert(Task task);
    /**
     * 根据page查询统计
     * @param page
     * @return
     */
	int selectCountByPage(Page page);
	/**
	 * 分页查询task
	 * @param page
	 * @return
	 */
	List<Task> selectByPage(Page page);
	
	/**
	 * 任务运行界面page查询统计
	 * @param page
	 * @return
	 */
	int selectTaskRunCount(Page page);
	
	int selectTaskRunCount2(Page page);

    Integer selectCountByName(@Param(value = "name") String name);

    Task selectByName(@Param(value = "name") String name);
} 
