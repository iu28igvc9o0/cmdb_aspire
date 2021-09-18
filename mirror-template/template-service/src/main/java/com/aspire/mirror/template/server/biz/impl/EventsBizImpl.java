package com.aspire.mirror.template.server.biz.impl;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.aspire.mirror.template.server.dao.EventsDao;
import com.aspire.mirror.template.api.dto.model.EventsDTO;
import com.aspire.mirror.template.server.biz.EventsBiz;
import com.aspire.mirror.template.server.dao.po.Events;
import com.aspire.mirror.template.server.dao.po.transform.EventsTransformer;

import org.apache.commons.lang.ArrayUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.util.CollectionUtils;

/**
 * 事件业务层实现类
 *
 * 项目名称:  微服务运维平台
 * 包:       com.aspire.mirror.template.server.biz.impl
 * 类名称:    EventsBizImpl.java
 * 类描述:    事件业务逻辑层实现类
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 12:14:48
 */
@Service
public class EventsBizImpl implements EventsBiz {

    /**
     * 新增数据
     *
     * @param eventsDTO 事件DTO对象
     * @return String 新增数据ID
     */
    public String insert(final EventsDTO eventsDTO){
        if(null == eventsDTO){
            LOGGER.error("method[insert] param[eventsDTO] is null");
            throw new RuntimeException("param[eventsDTO] is null");
        }
        String eventId = UUID.randomUUID().toString();
        Events events = EventsTransformer.toPo(eventsDTO);
        events.setEventId(eventId);
        eventsDao.insert(events);
        return eventId;
    }

    /**
     * 批量新增事件数据
     *
     * @param listEventsDTO 事件DTO集合对象
     * @return int 新增数据条数
     */
    public int insertByBatch(final List<EventsDTO> listEventsDTO){
        if (CollectionUtils.isEmpty(listEventsDTO)) {
            LOGGER.error("method[insertByBatch] param[listEventsDTO] is null");
            throw new RuntimeException("param[listEventsDTO] is null");
        }
        List<Events> listEvents = EventsTransformer.toPo(listEventsDTO);
        return eventsDao.insertByBatch(listEvents);
    }
    /**
     * 根据主键删除数据
     *
     * @param eventId 主键
     * @return int 删除数据条数
     */
    public int deleteByPrimaryKey(final String eventId){
        if (StringUtils.isEmpty(eventId)) {
            LOGGER.error("method[eleteByPrimaryKey] param[eventId] is null");
            throw new RuntimeException("param[eventId] is null");
        }
        return eventsDao.deleteByPrimaryKey(eventId);
    }
    /**
    * 根据主键数组批量删除数据
    *
    * @param eventIdArrays 主键数组
    * @return int 删除数据条数
    */
    public int deleteByPrimaryKeyArrays(final String[] eventIdArrays){
        if (ArrayUtils.isEmpty(eventIdArrays)) {
            LOGGER.error("method[deleteByPrimaryKeyArrays] param[eventIdArrays] is null");
            throw new RuntimeException("param[eventIdArrays] is null");
        }
        return eventsDao.deleteByPrimaryKeyArrays(eventIdArrays);
    }

    /**
     * 根据条件删除数据
     *
     * @param eventsDTO  事件DTO对象
     * @return int 删除数据条数
     */
    public int delete(final EventsDTO eventsDTO){
        if(null == eventsDTO){
            LOGGER.error("method[delete] param[eventsDTO] is null");
            throw new RuntimeException("param[eventsDTO] is null");
        }
        Events events = EventsTransformer.toPo(eventsDTO);
        return eventsDao.delete(events);
    }

    /**
     * 根据参数选择性更新数据
     *
     * @param eventsDTO 事件DTO对象
     * @return int 数据条数
     */
    public int updateByPrimaryKeySelective(final EventsDTO eventsDTO){
        if(null == eventsDTO){
            LOGGER.error("method[updateByPrimaryKey] param[eventsDTO] is null");
            throw new RuntimeException("param[eventsDTO] is null");
        }
        Events events = EventsTransformer.toPo(eventsDTO);
        return eventsDao.updateByPrimaryKeySelective(events);
    }

    /**
     * 根据主键更新数据
     *
     * @param eventsDTO 事件DTO对象
     * @return int 数据条数
     */
    public int updateByPrimaryKey(final EventsDTO eventsDTO){
        if(null == eventsDTO){
            LOGGER.error("method[updateByPrimaryKey] param[eventsDTO] is null");
            throw new RuntimeException("param[eventsDTO] is null");
        }
        if (StringUtils.isEmpty(eventsDTO.getEventId())) {
            LOGGER.warn("method[updateByPrimaryKey] param[eventId] is null");
            throw new RuntimeException("param[eventId] is null");
        }
        Events events = EventsTransformer.toPo(eventsDTO);
        return eventsDao.updateByPrimaryKey(events);
    }

    /**
     * 根据主键查询
     *
     * @param eventId 主键
     * @return EventsDTO 返回对象
     */
    public EventsDTO selectByPrimaryKey(final String eventId){
        Events events = eventsDao.selectByPrimaryKey(eventId);
        if (StringUtils.isEmpty(eventId)) {
            LOGGER.warn("method[selectByPrimaryKey] param[eventId] is null");
            return null;
        }
        return EventsTransformer.fromPo(events);
    }
    /**
     * 根据主键数组查询
     *
     * @param eventIdArrays 主键数组
     * @return List<EventsDTO> 返回集合对象
     */
    public List<EventsDTO> selectByPrimaryKeyArrays(final String[] eventIdArrays){
        if (ArrayUtils.isEmpty(eventIdArrays)) {
            LOGGER.warn("method[selectByPrimaryKeyArrays] param[eventIdArrays] is null");
            return Collections.emptyList();
        }
        List<Events> listEvents = eventsDao.selectByPrimaryKeyArrays(eventIdArrays);
        return EventsTransformer.fromPo(listEvents);
    }
    /**
     * 根据dto实体查询列表
     *
     * @param eventsDTO 事件DTO对象
     * @return List<Events>  返回集合
     */
    public List<EventsDTO> select(final EventsDTO eventsDTO){
        if(null == eventsDTO){
            LOGGER.warn("select Object eventsDTO is null");
            return Collections.emptyList();
        }
        Events events = EventsTransformer.toPo(eventsDTO);
        List<Events> listEvents = eventsDao.select(events);
        return EventsTransformer.fromPo(listEvents);
    }

    /**
     * 根据DTO实体查询条数
     *
     * @param eventsDTO 事件DTO对象
     * @return int 数据条数
     */
    public int selectCount(final EventsDTO eventsDTO){
        if(null == eventsDTO){
            LOGGER.warn("selectCount Object eventsDTO is null");
        }
        Events events = EventsTransformer.toPo(eventsDTO);
        return eventsDao.selectCount(events);
    }

    /** slf4j*/
    private static final Logger   LOGGER = LoggerFactory.getLogger(EventsBizImpl.class);

    /** 数据访问层接口*/
    @Autowired
    private EventsDao eventsDao;

} 
