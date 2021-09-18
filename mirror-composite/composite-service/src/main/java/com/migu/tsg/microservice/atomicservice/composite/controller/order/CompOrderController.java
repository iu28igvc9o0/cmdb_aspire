package com.migu.tsg.microservice.atomicservice.composite.controller.order;

import com.aspire.mirror.composite.service.order.IOrderService;
import com.aspire.mirror.composite.service.order.payload.HomePageInstAnalysisReq;
import com.aspire.mirror.composite.service.order.payload.PageBean;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.composite.config.OrderConfig;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
import com.migu.tsg.microservice.atomicservice.composite.helper.BpmTokenHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.controller.order
 * 类名称:    CompOrderController.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/9/2 13:52
 * 版本:      v1.0
 */
@RestController
public class CompOrderController implements IOrderService {
    @Autowired
    OrderConfig orderConfig;

    @Autowired
    BpmTokenHelper bpmTokenHelper;

    Logger logger = LoggerFactory.getLogger(CompOrderController.class);
    @Override
    @ResAction(action = "select", resType = "order")
    public Object instTrend(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String
            endDate,@RequestParam(value = "isWhole") Integer isWhole) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        String account = authCtx.getUser().getUsername();
        String token = bpmTokenHelper.getToken();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization", "Bearer "+token);
        requestHeaders.add("Content-Type", "application/json;charset=utf-8");
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> req = Maps.newHashMap();
        req.put("startDate", startDate);
        req.put("endDate", endDate);
        req.put("account", account);
        req.put("isWhole", isWhole);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(orderConfig.getTrendsUrl());
        req.entrySet().stream().forEach(o -> builder.queryParam(o.getKey(),o.getValue()));
        String url =  builder.build().encode().toString();
        HttpEntity<Map> formErntity = new HttpEntity<>(req, requestHeaders);
        Object result = restTemplate.exchange(url, HttpMethod.GET, formErntity, Object.class, req);
//        Object result = restTemplate.getForObject(orderConfig.getTrendsUrl(), Object.class, req);
        return result;
    }

    @Override
    @ResAction(action = "select", resType = "order")
    public Object listJson(@RequestBody PageBean pageBean) {
        String token = bpmTokenHelper.getToken();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization", "Bearer "+token);
        requestHeaders.add("Content-Type", "application/json;charset=utf-8");
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<PageBean> formErntity = new HttpEntity<>(pageBean, requestHeaders);
        Object result = restTemplate.exchange(orderConfig.getAllTypeUrl(), HttpMethod.POST, formErntity, Object.class, pageBean);
//        Object result = restTemplate.postForEntity(orderConfig.getAllTypeUrl(), pageBean, Object.class);
        return result;
    }

    @Override
    @ResAction(action = "select", resType = "order")
    public Object instDistribution(@RequestParam(value = "type") Integer type,@RequestParam(value = "isWhole") Integer isWhole ) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        String account = authCtx.getUser().getUsername();
        String token = bpmTokenHelper.getToken();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization", "Bearer "+token);
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> req = Maps.newHashMap();
        req.put("account", account);
        req.put("type", type);
        req.put("isWhole", isWhole);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(orderConfig.getDistributionUrl());
        req.entrySet().stream().forEach(o -> builder.queryParam(o.getKey(),o.getValue()));
        String url =  builder.build().encode().toString();
        HttpEntity<Map> formErntity = new HttpEntity<>(req, requestHeaders);
        Object result = restTemplate.exchange(url, HttpMethod.GET, formErntity, Object.class);
        return result;
    }

    @Override
    @ResAction(action = "select", resType = "order")
    public Object getAccountByParam() {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        String account = authCtx.getUser().getUsername();
        String token = bpmTokenHelper.getToken();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization", "Bearer "+token);
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> req = Maps.newHashMap();
        req.put("account", account);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(orderConfig.getOrderNumUrl());
        req.entrySet().stream().forEach(o -> builder.queryParam(o.getKey(),o.getValue()));
        String url =  builder.build().encode().toString();
        HttpEntity<Map> formErntity = new HttpEntity<>(req, requestHeaders);
//        Object result = restTemplate.getForObject(url, Object.class, formErntity);
        Object result = restTemplate.exchange(url, HttpMethod.GET, formErntity, Object.class);
        return result;
    }

    @Override
    @ResAction(action = "select", resType = "order")
    public Object instStatistics(@RequestParam(value = "defKey") String defKey, @RequestParam(value = "startDate") String
            startDate, @RequestParam(value = "endDate") String endDate,@RequestParam(value = "isWhole") Integer isWhole) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        String account = authCtx.getUser().getUsername();
        String token = bpmTokenHelper.getToken();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization", "Bearer "+token);
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> req = Maps.newHashMap();
        req.put("defKey", defKey);
        req.put("startDate", startDate);
        req.put("endDate", endDate);
        req.put("account", account);
        req.put("isWhole", isWhole);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(orderConfig.getOrderStatisticsUrl());
        req.entrySet().stream().forEach(o -> builder.queryParam(o.getKey(),o.getValue()));
        String url =  builder.build().encode().toString();
        HttpEntity<Map> formErntity = new HttpEntity<>(req, requestHeaders);
        Object result = restTemplate.exchange(url, HttpMethod.GET, formErntity, Object.class);
//        Object result = restTemplate.getForObject(orderConfig.getOrderStatisticsUrl(), Object.class, req);
        return result;
    }

    @Override
    public Object instOverview() {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        String account = authCtx.getUser().getUsername();
        String token = bpmTokenHelper.getToken();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization", "Bearer "+token);
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> req = Maps.newHashMap();
        req.put("account", account);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(orderConfig.getInstOverviewUrl());
        req.entrySet().stream().forEach(o -> builder.queryParam(o.getKey(),o.getValue()));
        String url =  builder.build().encode().toString();
        HttpEntity<Map> formErntity = new HttpEntity<>(req, requestHeaders);
        Object result = restTemplate.exchange(url, HttpMethod.GET, formErntity, Object.class);
        return result;
    }

    @Override
    public Object homepageProInstAnalysis(@RequestBody HomePageInstAnalysisReq req) {
        String token = bpmTokenHelper.getToken();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization", "Bearer "+token);
        requestHeaders.add("Content-Type", "application/json;charset=utf-8");
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<HomePageInstAnalysisReq> formErntity = new HttpEntity<>(req, requestHeaders);
        Object result = restTemplate.exchange(orderConfig.getHomepageProInstAnalysisUrl(), HttpMethod.POST, formErntity, Object.class, req);
        return result;
    }

    @Override
    public Object instDistributionNew() {
        String token = bpmTokenHelper.getToken();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization", "Bearer "+token);
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> req = Maps.newHashMap();
        req.put("type", 1);
        req.put("account", null);
        req.put("isWhole", 1);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(orderConfig.getDistributionUrl());
        req.entrySet().stream().forEach(o -> builder.queryParam(o.getKey(),o.getValue()));
        String url =  builder.build().encode().toString();
        HttpEntity<Map> formErntity = new HttpEntity<>(req, requestHeaders);
        Object result = restTemplate.exchange(url, HttpMethod.GET, formErntity, Object.class);
        return result;
    }

    @Override
    public Object instRuntimeAnalysis() {
        String token = bpmTokenHelper.getToken();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization", "Bearer "+token);
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> req = Maps.newHashMap();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(orderConfig.getInstRuntimeAnalysisUrl());
        req.entrySet().stream().forEach(o -> builder.queryParam(o.getKey(),o.getValue()));
        String url =  builder.build().encode().toString();
        HttpEntity<Map> formErntity = new HttpEntity<>(req, requestHeaders);
        Object result = restTemplate.exchange(url, HttpMethod.GET, formErntity, Object.class);
        return result;
    }

    @Override
    public Object instStatisticsNew(String startDate, String endDate) {
        String token = bpmTokenHelper.getToken();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization", "Bearer "+token);
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> req = Maps.newHashMap();
        req.put("defKey", 0);
        req.put("startDate", startDate);
        req.put("endDate", endDate);
        req.put("account", null);
        req.put("isWhole", 1);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(orderConfig.getOrderStatisticsUrl());
        req.entrySet().stream().forEach(o -> builder.queryParam(o.getKey(),o.getValue()));
        String url =  builder.build().encode().toString();
        HttpEntity<Map> formErntity = new HttpEntity<>(req, requestHeaders);
        Object result = restTemplate.exchange(url, HttpMethod.GET, formErntity, Object.class);
        return result;
    }

    @Override
    public Object getInstListByDefKey(@RequestParam(value = "defKey") String defKey) {
        String token = bpmTokenHelper.getToken();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization", "Bearer "+token);
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> req = Maps.newHashMap();
        req.put("defKey", defKey);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(orderConfig.getGetInstListByDefKeyUrl());
        req.entrySet().stream().forEach(o -> builder.queryParam(o.getKey(),o.getValue()));
        String url =  builder.build().encode().toString();
        HttpEntity<Map> formErntity = new HttpEntity<>(req, requestHeaders);
        Object result = restTemplate.exchange(url, HttpMethod.GET, formErntity, Object.class);
        return result;
    }

    @Override
    public Object getInstListByDefKeyAndStatus(@RequestParam(value = "defKey") String defKey, @RequestParam(value = "status") String status) {
        String token = bpmTokenHelper.getToken();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization", "Bearer "+token);
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> req = Maps.newHashMap();
        req.put("defKey", defKey);
        req.put("status", status);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(orderConfig.getGetInstListByDefKeyAndStatusUrl());
        req.entrySet().stream().forEach(o -> builder.queryParam(o.getKey(),o.getValue()));
        String url =  builder.build().encode().toString();
        HttpEntity<Map> formErntity = new HttpEntity<>(req, requestHeaders);
        Object result = restTemplate.exchange(url, HttpMethod.GET, formErntity, Object.class);
        return result;
    }
}
