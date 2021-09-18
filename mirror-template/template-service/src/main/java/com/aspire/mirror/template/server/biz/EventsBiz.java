package com.aspire.mirror.template.server.biz;

import java.util.List;

import com.aspire.mirror.template.server.dao.po.Events;
import com.aspire.mirror.template.api.dto.model.EventsDTO;

/**
 * 事件业务层接口
 *
 * 项目名称: 微服务运维平台
 * 包:      com.aspire.mirror.template.server.biz
 * 类名称:   EventsBiz.java
 * 类描述:   事件业务逻辑层接口
 * 创建人:   JinSu
 * 创建时间: 2018-07-27 12:14:48
 */
public interface EventsBiz {

    /**
     * 新增数据
     *
     * @param eventsDTO 事件DTO对象
     * @return int 新增数据条数
     */
    String insert(EventsDTO eventsDTO);

    /**
     * 批量新增事件数据
     *
     * @param listEventsDTO 事件DTO集合对象
     * @return int 新增数据条数
     */
    int insertByBatch(List<EventsDTO> listEventsDTO);
    /**
     * 根据主键删除数据
     *
     * @param eventId 主键
     * @return int 删除数据条数
     */
    int deleteByPrimaryKey(String eventId);

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
     * @param eventsDTO  事件DTO对象
     * @return int 删除数据条数
     */
    int delete(EventsDTO eventsDTO);

    /**
     * 根据参数选择性更新数据
     *
     * @param eventsDTO 事件DTO对象
     * @return int 数据条数
     */
    int updateByPrimaryKeySelective(EventsDTO eventsDTO);

    /**
     * 根据主键更新数据
     *
     * @param eventsDTO 事件DTO对象
     * @return int 数据条数
     */
    int updateByPrimaryKey(EventsDTO eventsDTO);

    /**
     * 根据主键查询
     *
     * @param eventId 主键
     * @return EventsDTO 返回对象
     */
    EventsDTO selectByPrimaryKey(String eventId);

    /**
     * 根据主键数组查询
     *
     * @param eventIdArrays 主键数组
     * @return List<EventsDTO> 返回集合对象
     */
    List<EventsDTO> selectByPrimaryKeyArrays(String[] eventIdArrays);
    /**
     * 根据dto实体查询列表
     *
     * @param eventsDTO 事件DTO对象
     * @return List<Events>  返回集合
     */
    List<EventsDTO> select(EventsDTO eventsDTO);

    /**
     * 根据DTO实体查询条数
     *
     * @param eventsDTO 事件DTO对象
     * @return int 数据条数
     */
    int selectCount(EventsDTO eventsDTO);

} 
