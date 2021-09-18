package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.restful.automate;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.restful.automate.IAutomateRestfulAPI;
import com.aspire.ums.cmdb.instance.payload.CmdbInstance;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.restful.automate.IAutomateRestfulClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: AutomateRestfulController
 * Author:   zhu.juwang
 * Date:     2019/9/11 11:00
 * Description: 该接口类用来专门提供接口给自动化运维工具使用
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class AutomateRestfulController implements IAutomateRestfulAPI {
    @Autowired
    private IAutomateRestfulClient automateRestfulClient;
    @Override
    public List<CmdbInstance> getInstanceList(@RequestBody Map<String, Object> params) {
        log.info("Request AutomateRestfulController.getInstanceList params -> {}", JSONObject.toJSON(params).toString());
        return automateRestfulClient.getInstanceList(params);
    }
}
