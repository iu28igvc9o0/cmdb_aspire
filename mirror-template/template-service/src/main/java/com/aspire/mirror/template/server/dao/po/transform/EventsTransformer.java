package com.aspire.mirror.template.server.dao.po.transform;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.util.CollectionUtils;

import com.aspire.mirror.template.server.dao.po.Events;
import com.aspire.mirror.template.api.dto.model.EventsDTO;

import java.util.List;
import java.util.Map;

/**
 * 事件对象转换类
 *
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.server.dao.po.transform
 * 类名称:    EventsTransformer.java
 * 类描述:    事件对应的PO与DTO的转换类
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 12:14:48
 */
public final class EventsTransformer  {

    private EventsTransformer(){
    }

   /**
    * 将事件PO实体转换为事件DTO实体
    *
    * @param  events 事件PO实体
    * @return EventsDTO 事件DTO实体
    */
    public static EventsDTO fromPo(final Events events) {
        if (null == events) {
            return null;
        }
        
        EventsDTO eventsDTO = new EventsDTO();
        eventsDTO.setEventId(events.getEventId());
        eventsDTO.setSource(events.getSource());
        eventsDTO.setObject(events.getObject());
        eventsDTO.setObjectId(events.getObjectId());
        eventsDTO.setValue(events.getValue());
        eventsDTO.setAcknowledged(events.getAcknowledged());
        eventsDTO.setClock(events.getClock());
        eventsDTO.setNs(events.getNs());
        eventsDTO.setDeviceId(events.getDeviceId());

        return eventsDTO;
    }

    /**
     * 将事件业务实体对象集合转换为事件持久化对象集合
     *
     * @param listEvents 事件业务实体对象集合
     * @return List<EventsDTO> 事件持久化对象集合
     */
    public static List<EventsDTO> fromPo(final List<Events> listEvents) {
        if (CollectionUtils.isEmpty(listEvents)) {
            return Lists.newArrayList();
        }
        List<EventsDTO> listEventsDTO = Lists.newArrayList();

        for (Events events : listEvents) {
            listEventsDTO.add(EventsTransformer.fromPo(events));
        }
        return listEventsDTO;
    }

   /**
    * 将事件DTO实体转换为事件PO实体
    *
    * @param  eventsDTO 事件DTO实体类
    * @return Events 事件PO实体
    */
    public static Events toPo(final EventsDTO eventsDTO) {
        if (null == eventsDTO) {
            return null;
        }

        Events events = new Events();
        events.setEventId(eventsDTO.getEventId());
        events.setSource(eventsDTO.getSource());
        events.setObject(eventsDTO.getObject());
        events.setObjectId(eventsDTO.getObjectId());
        events.setValue(eventsDTO.getValue());
        events.setAcknowledged(eventsDTO.getAcknowledged());
        events.setClock(eventsDTO.getClock());
        events.setNs(eventsDTO.getNs());
        events.setDeviceId(eventsDTO.getDeviceId());

        return events;
    }

    /**
     * 将事件业务实体对象集合转换为事件持久化对象集合
     *
     * @param listEventsDTO 事件业务实体对象集合
     * @return List<Events> 事件持久化对象集合
     */
    public static List<Events> toPo(final List<EventsDTO> listEventsDTO) {
        if (CollectionUtils.isEmpty(listEventsDTO)) {
            return Lists.newArrayList();
        }
        List<Events> listEvents = Lists.newArrayList();

        for (EventsDTO eventsdTO : listEventsDTO) {
            listEvents.add(EventsTransformer.toPo(eventsdTO));
        }
        return listEvents;
    }
    /**
     * 将事件业务实体对象集合转换为Map
     *
     * @param listEventsDTO 事件业务实体对象集合
     * @return Map<String, EventsDTO> Map[key=String|value=EventsDTO]
     */
    public static Map<String, EventsDTO> toDTOMap(final List<EventsDTO> listEventsDTO) {
        if (CollectionUtils.isEmpty(listEventsDTO)) {
            return Maps.newHashMap();
        }
        Map<String, EventsDTO> eventsDTOMaps = Maps.newHashMap();

        for (EventsDTO eventsDTO : listEventsDTO) {
            eventsDTOMaps.put(eventsDTO.getEventId(), eventsDTO);
        }
        return eventsDTOMaps;
    }

    /**
     * 将事件业务实体对象集合中的主键进行提取并封装成数组
     *
     * @param listEventsDTO 事件业务实体对象集合
     * @return String[] 主键数组
     */
    public static String[] toDTOPrimaryKeyArrays(final List<EventsDTO> listEventsDTO) {
        if (CollectionUtils.isEmpty(listEventsDTO)) {
            return null;
        }
        int size = listEventsDTO.size();
        String[] eventIdArrays = new String[size];
        for (int i = 0 ;i< size; i++) {
            eventIdArrays[i] = listEventsDTO.get(i).getEventId();
        }
        return eventIdArrays;
        }
} 
