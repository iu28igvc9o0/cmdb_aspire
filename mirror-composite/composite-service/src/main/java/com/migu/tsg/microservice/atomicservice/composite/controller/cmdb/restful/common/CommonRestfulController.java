package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.restful.common;

import com.aspire.mirror.composite.service.cmdb.restful.common.ICommonRestfulAPI;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.restful.payload.StatisticRequestEntity;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.restful.common.ICommonRestfulClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: CommonRestfulController
 * Author:   zhu.juwang
 * Date:     2020/3/11 10:56
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CommonRestfulController implements ICommonRestfulAPI {

    @Autowired
    private ICommonRestfulClient restfulClient;

    @Override
    @ResAction(action = "view", resType = "cmdb", loadResFilter=true)
    public Result<Map<String, Object>> getInstanceList(@RequestBody Map<String, Object> params, HttpServletResponse response) throws Exception {
        return restfulClient.getInstanceList(params);
    }

    @Override
    @ResAction(action = "view", resType = "cmdb", loadResFilter=true)
    public Map<String, Object> getInstanceDetail(@RequestBody Map<String, Object> params) {
        return restfulClient.getInstanceDetail(params);
    }

    @Override
    @ResAction(action = "view", resType = "cmdb", loadResFilter=true)
    public Object getInstanceStatistics(@RequestBody StatisticRequestEntity statisticRequestEntity) {
        return restfulClient.getInstanceStatistics(statisticRequestEntity);
    }
}
