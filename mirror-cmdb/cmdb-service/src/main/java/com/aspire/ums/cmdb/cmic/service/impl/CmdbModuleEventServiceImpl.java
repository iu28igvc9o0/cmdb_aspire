package com.aspire.ums.cmdb.cmic.service.impl;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.ums.cmdb.cmic.mapper.CmdbModuleEventMapper;
import com.aspire.ums.cmdb.cmic.payload.CmdbModuleEvent;
import com.aspire.ums.cmdb.cmic.payload.CmdbModuleEventHandlerClass;
import com.aspire.ums.cmdb.cmic.service.ICmdbModuleEventHandlerClassService;
import com.aspire.ums.cmdb.cmic.service.ICmdbModuleEventService;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v3.module.event.AbstractModuleEvent;

import lombok.extern.slf4j.Slf4j;

/**
* 描述：
* @author
* @date 2020-05-18 18:27:54
*/
@Service
@Slf4j
public class CmdbModuleEventServiceImpl implements ICmdbModuleEventService {

    @Autowired
    private CmdbModuleEventMapper mapper;
    @Autowired
    private ICmdbModuleEventHandlerClassService handlerClassService;
    @Autowired
    private ConfigDictService dictService;


    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<CmdbModuleEvent> list() {
        return mapper.list();
    }

    @Override
    public List<CmdbModuleEvent> getModuleEventList(String moduleId, String eventClass) {
        CmdbModuleEvent event = new CmdbModuleEvent();
        event.setModuleId(moduleId);
        event.setEventClass(eventClass);
        return mapper.listByEntity(event);
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public CmdbModuleEvent get(CmdbModuleEvent entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public void insert(CmdbModuleEvent entity) {
        mapper.insert(entity);
    }

    @Override
    public void saveModuleEvents(String userName, String moduleId, List<CmdbModuleEvent> eventList) {
        List<CmdbModuleEvent> addEventList = new ArrayList<>();
        List<String> currEventIdList = new ArrayList<>();
        for (CmdbModuleEvent event : eventList) {
            if (StringUtils.isNotEmpty(event.getId())) {
                currEventIdList.add(event.getId());
            } else {
                event.setId(UUIDUtil.getUUID());
                event.setIsDelete(0);
                event.setInsertPerson(userName);
                event.setInsertTime(new Date());
            }
            event.setModuleId(moduleId);
            event.setUpdatePerson(userName);
            event.setUpdateTime(new Date());
            addEventList.add(event);
        }
        // 先删除不需要的事件
        mapper.deleteByNotExistId(moduleId, currEventIdList);
        if (eventList.size()>0) {
            // 再存储
            mapper.insertByBatch(eventList);
        }
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(CmdbModuleEvent entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(CmdbModuleEvent entity) {
        mapper.delete(entity);
    }

    @Override
    public Object handlerModuleDataEvent(String moduleId, String instanceId, String codeId, Map<String, Object> handleData, String eventType) {
        String dictId = dictService.getIdByNoteAndCol(eventType, "event_type_module");
        handleData.put("changeType", eventType);
        return handlerEvent(moduleId, instanceId,codeId, handleData, dictId);
    }

    @Override
    public Map<String, Object> handlerCodeDataEvent(String moduleId, String codeId, Map<String, Object> selectItem, String eventType) {
        Map<String, Object> handleData = new HashMap<>();
        if (selectItem.keySet().size() > 0) {
            handleData.put("selectItem", selectItem);
        }
        return handlerEvent(moduleId, null, codeId, handleData, eventType);
    }

    @Override
    public Map<String, List<Map<String, Object>>> getHaveEventCodeList(String moduleId) {
        Map<String, Object> result = new HashMap<>();
        List<CmdbModuleEvent> codeEvents = mapper.listCodeEventByModuleId(moduleId);
        Map<String, List<Map<String, Object>>> resultMap = new HashMap<>();
        for (CmdbModuleEvent event : codeEvents) {
            if (!resultMap.containsKey(event.getCodeId())) {
                resultMap.put(event.getCodeId(), new ArrayList<>());
            }
            Map<String, Object> map = new HashMap<>();
            map.put("eventType", event.getEventType());
            map.put("eventTypeName", dictService.getDictById(event.getEventType()).getDictNote());
            resultMap.get(event.getCodeId()).add(map);
        }
        return resultMap;
    }

    private Map<String, Object> handlerEvent(String moduleId, String instanceId, String codeId, Map<String, Object> handleData, String eventType) {
        CmdbModuleEvent queryEventParams = new CmdbModuleEvent();
        queryEventParams.setEventType(eventType);
        queryEventParams.setModuleId(moduleId);
        if (StringUtils.isNotEmpty(codeId)) {
            handleData.put("codeId", codeId);
            queryEventParams.setCodeId(codeId);
        }
        List<CmdbModuleEvent> eventList = mapper.listByEntity(queryEventParams);
        Map<String, Object> returnMap = new HashMap<>();
        if (eventList != null && eventList.size() > 0) {
            AbstractModuleEvent abstractEvent;
            for (CmdbModuleEvent event : eventList) {
                CmdbModuleEventHandlerClass handlerClass = new CmdbModuleEventHandlerClass();
                try {
                    handlerClass = handlerClassService.getById(event.getHandlerClassId());
                    Class clz = Class.forName(handlerClass.getHandlerClass());
                    Constructor constructor = clz.getConstructor();
                    abstractEvent = (AbstractModuleEvent) constructor.newInstance();
                    returnMap = abstractEvent.run(moduleId, instanceId, handleData);
                    if (!returnMap.containsKey("flag")) {
                        throw new RuntimeException("Event[" + handlerClass.getHandlerName() + "] return message error, property flag is require.");
                    }
                    if (("false").equals(String.valueOf(returnMap.get("flag")).toLowerCase(Locale.ENGLISH))) {
                        if (!returnMap.containsKey("msg")) {
                            throw new RuntimeException("Event[" + handlerClass.getHandlerName() + "] return message error, if result error, property msg is require.");
                        }
                        throw new RuntimeException("Event[" + handlerClass.getHandlerName() + "] execute error. Cause:" + returnMap.get("msg"));
                    }
                } catch (Exception e) {
                    log.error("执行事件异常", e);
//                    throw new RuntimeException("Event[" + event.getEventClass() + "] parse error. Handler class must be extends class AbstractModuleEvent.class. or error: " + e.getMessage());
                }
            }
        }
        return returnMap;
    }
}
