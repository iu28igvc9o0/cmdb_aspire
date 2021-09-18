package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.instance;

import com.aspire.mirror.composite.service.cmdb.instance.IInstanceBpmAPI;
import com.aspire.mirror.composite.service.cmdb.instance.payload.CmdbBpmProcQuery;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.composite.config.OrderConfig;
import com.migu.tsg.microservice.atomicservice.composite.helper.BpmTokenHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: InstanceBpmController
 * Author:   hangfang
 * Date:     2019/9/6
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class InstanceBpmController implements IInstanceBpmAPI {

    @Autowired
    OrderConfig orderConfig;

    @Autowired
    BpmTokenHelper bpmTokenHelper;

    @Override
    public Object findInstListByDeviceId(@RequestBody CmdbBpmProcQuery bpmProcQuery) {
        String token = bpmTokenHelper.getToken();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization", "Bearer " + token);
        requestHeaders.add("Content-Type", "application/json;charset=utf-8");
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> req = Maps.newHashMap();
        req.put("deviceId", bpmProcQuery.getDeviceId());
        req.put("procDefKey", bpmProcQuery.getProcDefKey());
        req.put("startTime", bpmProcQuery.getStartTime());
        req.put("endTime", bpmProcQuery.getEndTime());
        req.put("pageNo", bpmProcQuery.getPageNo());
        req.put("pageSize", bpmProcQuery.getPageSize());
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(orderConfig.getOrderBase() + orderConfig.getInstListByDeviceUrl());
        req.entrySet().stream().forEach(o -> builder.queryParam(o.getKey(), o.getValue()));
        String url = builder.build().toString();
        org.springframework.http.HttpEntity<Map> formErntity = new HttpEntity<>(req, requestHeaders);
        return restTemplate.exchange(url, HttpMethod.GET, formErntity, Object.class, req);
    }

    @Override
    public Object getAllFlowDefList() {
        String token = bpmTokenHelper.getToken();
        if (StringUtils.isEmpty(token)) {
            log.error("获取token异常 token值为{}", token);
            throw new RuntimeException("获取token异常");
        }
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization", "Bearer " + token);
        requestHeaders.add("Content-Type", "application/json;charset=utf-8");
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> req = Maps.newHashMap();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(orderConfig.getOrderBase() + orderConfig.getAllFlowDefListUrl());
        String url = builder.build().encode().toString();
        org.springframework.http.HttpEntity<Map> formErntity = new HttpEntity<>(req, requestHeaders);
        return restTemplate.exchange(url, HttpMethod.GET, formErntity, Object.class, req);
    }

}

