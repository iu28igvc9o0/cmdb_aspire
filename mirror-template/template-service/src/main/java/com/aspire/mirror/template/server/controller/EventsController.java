package com.aspire.mirror.template.server.controller;

import com.aspire.mirror.template.api.dto.EventsCreateRequest;
import com.aspire.mirror.template.api.dto.EventsCreateResponse;
import com.aspire.mirror.template.api.dto.EventsUpdateRequest;
import com.aspire.mirror.template.api.dto.EventsUpdateResponse;
import com.aspire.mirror.template.api.dto.model.EventsDTO;
import com.aspire.mirror.template.api.dto.vo.EventsVO;
import com.aspire.mirror.template.api.service.EventsService;
import com.aspire.mirror.template.server.biz.EventsBiz;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 事件控制层实现类
 *
 * 项目名称: mirror平台
 * 包:      com.aspire.mirror.template.server.controller
 * 类名称:   EventsController.java
 * 类描述:   事件业务逻辑层实现类
 * 创建人:   JinSu
 * 创建时间: 2018-07-27 12:14:48
 */
@RestController
@CacheConfig(cacheNames = "EventsCache")
public class EventsController implements EventsService {

    /**
     * 创建事件信息
     *
     * @param eventsCreateRequest 事件创建请求对象
     * @return EventsCreateResponse 事件创建响应对象
     */
    public EventsCreateResponse createdEvents(@RequestBody final EventsCreateRequest eventsCreateRequest){
        if(null == eventsCreateRequest){
            LOGGER.error("created param eventsCreateRequest is null");
            throw new RuntimeException("eventsCreateRequest is null");
        }
        EventsDTO eventsDTO = new EventsDTO();
        BeanUtils.copyProperties(eventsCreateRequest, eventsDTO);
        String eventId = eventsBiz.insert(eventsDTO);
        EventsCreateResponse eventsCreateResponse = new EventsCreateResponse();
        BeanUtils.copyProperties(eventsDTO, eventsCreateResponse);
        eventsCreateResponse.setEventId(eventId);
        return eventsCreateResponse;
    }
    /**
     * 根据主键删除单条事件信息
     *
     * @param eventId 主键
     * @@return Result 返回结果
     */
    public ResponseEntity<String> deleteByPrimaryKey(@PathVariable("event_id") final String eventId){
        try {
            eventsBiz.deleteByPrimaryKey(eventId);
            return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            LOGGER.error("deleteByProjectId error:" + e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据主键删除多条事件信息
     *
     * @param eventIds 主键（以逗号分隔）
     * @@return Result 返回结果
     */
    public ResponseEntity<String> deleteByPrimaryKeyArrays(@PathVariable("event_ids") final String eventIds){
        try {
            if (StringUtils.isEmpty(eventIds)) {
                throw new RuntimeException("eventIds is null");
            }
            String[] eventIdArrays = eventIds.split(",");
            eventsBiz.deleteByPrimaryKeyArrays(eventIdArrays);
            return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            LOGGER.error("deleteByPrimaryKeyArrays error:" + e.getMessage(), e);
            return new ResponseEntity<String>("删除错误！", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据主键修改事件信息
     *
     * @param eventsUpdateRequest 事件修改请求对象
     * @return EventsUpdateResponse 事件修改响应对象
     */
    public EventsUpdateResponse modifyByPrimaryKey(@PathVariable("event_id") final String eventId,@RequestBody final EventsUpdateRequest eventsUpdateRequest){
        EventsDTO eventsdTO = new EventsDTO();
        BeanUtils.copyProperties(eventsUpdateRequest, eventsdTO);
        eventsdTO.setEventId(eventId);
        eventsBiz.updateByPrimaryKey(eventsdTO);
        EventsDTO findEventsDTO = eventsBiz.selectByPrimaryKey(eventId);
        EventsUpdateResponse eventsUpdateResponse = new EventsUpdateResponse();
        BeanUtils.copyProperties(findEventsDTO, eventsUpdateResponse);
        return eventsUpdateResponse;
    }

    /**
     * 根据主键查找事件详情信息
     *
     * @param eventId 事件主键
     * @return EventsVO 事件详情响应对象
     */
    public EventsVO findByPrimaryKey(@PathVariable("event_id") final String eventId){
        if (StringUtils.isEmpty(eventId)) {
            LOGGER.warn("findByPrimaryKey param eventId is null");
            return null;
        }
        EventsDTO eventsDTO = eventsBiz.selectByPrimaryKey(eventId);
        EventsVO eventsVO = new EventsVO();
        if (null != eventsDTO) {
            BeanUtils.copyProperties(eventsDTO, eventsVO);
        }

        return eventsVO;
    }

    /**
     * 根据主键查询事件集合信息
     *
     * @param eventIds 事件主键
     * @return EventsVO 事件查询响应对象
     */
    public List<EventsVO> listByPrimaryKeyArrays(@PathVariable("event_id") final String eventIds){
        if (StringUtils.isEmpty(eventIds)) {
            LOGGER.error("listByPrimaryKeyArrays param eventIds is null");
            return Lists.newArrayList();
        }
        String[] eventIdArrays = eventIds.split(",");
        List<EventsDTO> listEventsDTO = eventsBiz.selectByPrimaryKeyArrays(eventIdArrays);
        List<EventsVO> listEventsVO = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(listEventsDTO)) {
            for (EventsDTO eventsDTO : listEventsDTO) {
                EventsVO eventsVO = new EventsVO();
                BeanUtils.copyProperties(eventsDTO, eventsVO);
                listEventsVO.add(eventsVO);
            }
        }
        return listEventsVO;
    }

    /** slf4j*/
    private static final Logger LOGGER           = LoggerFactory.getLogger(EventsController.class);
    
    @Autowired
    private EventsBiz eventsBiz;

}