package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.restful.bpm;

import com.aspire.mirror.composite.service.cmdb.restful.bpm.IIdcMonthReportAPI;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.restful.bpm.config.BpmConfig;
import com.migu.tsg.microservice.atomicservice.composite.helper.BpmTokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: IdcMonthReportController
 * Author:   zhu.juwang
 * Date:     2020/5/14 11:12
 * Description: 一级资源池运营月报调用BPM接口数据
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
public class IdcMonthReportController implements IIdcMonthReportAPI {
    @Autowired
    private BpmTokenHelper bpmTokenHelper;
    @Autowired
    private BpmConfig bpmConfig;

    public Object getOrderReport(@RequestParam("month") String month, @RequestParam("idcType") String pool) {
        return getBpmData(bpmConfig.getOrderReportUrl(), month, pool);
    }

    public Object getAlertErrorReport(@RequestParam("month") String month, @RequestParam("idcType") String pool) {
        return getBpmData(bpmConfig.getAlertErrorReportUrl(), month, pool);
    }

    private Object getBpmData(String httpUrl, String month, String pool) {
        String token = bpmTokenHelper.getToken();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(type);
        requestHeaders.add("Authorization", "Bearer "+token);
        requestHeaders.add("Accept", MediaType.APPLICATION_JSON.toString());
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> req = Maps.newHashMap();
        req.put("month", month);
        req.put("pool", pool);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(httpUrl);
        req.entrySet().stream().forEach(o -> builder.queryParam(o.getKey(),o.getValue()));
        HttpEntity<Map> formEntity = new HttpEntity<>(req, requestHeaders);
        Object result = restTemplate.exchange(builder.build().toString(), HttpMethod.GET, formEntity, Object.class, req);
        return result;
    }
}
