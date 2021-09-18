package com.aspire.ums.cmdb.cmic.web;

import com.aspire.ums.cmdb.cmic.ICmdbModuleEventAPI;
import com.aspire.ums.cmdb.cmic.payload.CmdbModuleEvent;
import com.aspire.ums.cmdb.cmic.service.ICmdbModuleEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbModuleEventController
 * Author:   hangfang
 * Date:     2020/5/22
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbModuleEventController implements ICmdbModuleEventAPI {

    @Autowired
    private ICmdbModuleEventService eventService;

    @Override
    public List<CmdbModuleEvent> getModuleEventList(@RequestParam("moduleId") String moduleId,
                                                    @RequestParam("eventClass") String eventClass) {
        return eventService.getModuleEventList(moduleId, eventClass);
    }

    @Override
    public Map<String, Object>  save(@RequestParam("userName") String userName,
                                    @RequestParam("moduleId") String moduleId,
                                    @RequestBody List<CmdbModuleEvent> eventList) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            eventService.saveModuleEvents(userName, moduleId, eventList);
            resultMap.put("success", true);
            resultMap.put("message", "保存成功！");
        }catch (Exception e) {
            log.error("CmdbModuleEventController.save 保存失败！error: {}", e);
            resultMap.put("success", true);
            resultMap.put("message", "保存失败！error: " +  e.toString());
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> getCodeEventData(@RequestParam("moduleId") String moduleId,
                                                  @RequestParam("codeId") String codeId,
                                                  @RequestBody Map<String, Object> selectItem,
                                                  @RequestParam("eventType") String eventType) {
        return eventService.handlerCodeDataEvent(moduleId, codeId, selectItem, eventType);
    }


    @Override
    public Map<String, List<Map<String, Object>>> getHaveEventCodeList(@RequestParam("moduleId") String moduleId) {
        return eventService.getHaveEventCodeList(moduleId);
    }
}
