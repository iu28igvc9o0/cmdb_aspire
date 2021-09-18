package com.aspire.mirror.template.server.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.mirror.template.server.dao.po.Events;

/**
 * 事件数据访问层接口
 *
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.server.dao
 * 类名称:    EventsDao.java
 * 类描述:    事件数据访问层接口
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 12:14:48
 */
@Mapper
public interface EventsDao {

    /**
     * 新增数据
     *
     * @param events 事件PO对象
     * @return int 新增数据条数
     */
    int insert(Events events);

    /**
     * 批量新增事件数据
     *
     * @param listEvents 事件PO集合对象
     * @return int 新增数据条数
     */
    int insertByBatch(List<Events> listEvents);
    /**
     * 根据主键删除数据
     *
     * @param eventId 主键
     * @return int 删除数据条数
     */
    int deleteByPrimaryKey(@Param(value = "eventId") String eventId);

    /**
     * 根据主键数组批量删除数据
     *
     * @param eventIdArrays 主键数组
     * @return int 删除数据条数
     */
    int deleteByPrimaryKeyArrays(String[] eventIdArrays);

    /**
     * 根据条件删除数据
     *
     * @param events  事件PO对象
     * @return int 删除数据条数
     */
    int delete(Events events);

    /**
     * 根据参数选择性更新数据
     *
     * @param events 事件PO对象
     * @return int 数据条数
     */
    int updateByPrimaryKeySelective(Events events);

    /**
     * 根据主键更新数据
     *
     * @param events 事件PO对象
     * @return int 数据条数
     */
    int updateByPrimaryKey(Events events);

    /**
     * 根据主键查询
     *
     * @param eventId 主键
     * @return Events 返回对象
     */
    Events selectByPrimaryKey(@Param(value = "eventId") String eventId);

    /**
     * 根据主键数组查询
     *
     * @param eventIdArrays 主键数组
     * @return List<Events> 返回集合对象
     */
    List<Events> selectByPrimaryKeyArrays(String[] eventIdArrays);
    /**
     * 根据po实体查询列表
     *
     * @param events 事件PO对象
     * @return List<Events>  返回集合
     */
    List<Events> select(Events events);

    /**
     * 根据po实体查询条数
     *
     * @param events 事件PO对象
     * @return int 数据条数
     */
    int selectCount(Events events);

} 
