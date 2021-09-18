package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.cmic;

import com.aspire.mirror.composite.service.cmdb.cmic.ICmdbModuleEventAPI;
import com.aspire.ums.cmdb.cmic.payload.CmdbModuleEvent;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.cmic.CmdbModuleEventClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: ICmdbModuleEventAPI
 * Author:   zhu.juwang
 * Date:     2020/5/19 10:23
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbModuleEventController implements ICmdbModuleEventAPI {

    @Autowired
    private CmdbModuleEventClient eventClient;

    @Override
    public List<CmdbModuleEvent> getModuleEventList(@RequestParam("moduleId") String moduleId,
                                                    @RequestParam(value = "eventClass", required = false) String eventClass) {
        return eventClient.getModuleEventList(moduleId, eventClass);
    }

    @Override
    public Map<String, Object> save(@RequestParam("moduleId") String moduleId,
                                    @RequestBody List<CmdbModuleEvent> eventList) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        String userName = authCtx.getUser().getUsername();
        return eventClient.save(userName, moduleId, eventList);
    }

    @Override
    public Map<String, Object> getCodeEventData(@RequestParam("moduleId") String moduleId,
                                                @RequestParam("codeId") String codeId,
                                                @RequestBody Map<String, Object> selectItem,
                                                @RequestParam("eventType") String eventType) {
        return eventClient.getCodeEventData(moduleId, codeId, selectItem, eventType);
    }

    @Override
    public Map<String, List<Map<String, Object>>> getHaveEventCodeList(@RequestParam("moduleId") String moduleId) {
        return eventClient.getHaveEventCodeList(moduleId);
    }
}
