package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.cmic;

import com.aspire.mirror.composite.service.cmdb.cmic.ICmdbModuleEventHandlerClassAPI;
import com.aspire.ums.cmdb.cmic.payload.CmdbModuleEventHandlerClass;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.cmic.CmdbModuleEventHandlerClassClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbModuleEventHandlerClassController
 * Author:   hangfang
 * Date:     2020/5/22
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbModuleEventHandlerClassController implements ICmdbModuleEventHandlerClassAPI {
    @Autowired
    private CmdbModuleEventHandlerClassClient handlerClassClient;
    @Override
    public List<CmdbModuleEventHandlerClass> getModuleHandlerClassList(@RequestParam(value = "eventClass", required = false) String eventClass) {
        return handlerClassClient.getModuleHandlerClassList(eventClass);
    }
}
