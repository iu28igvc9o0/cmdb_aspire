package com.migu.tsg.microservice.atomicservice.composite.service.sms.payload;

import lombok.Data;

/**
 * 项目名称: 咪咕微服务运营平台 包:
 * com.migu.tsg.microservice.atomicservice.composite.service.sms.payload 类名称:
 * SmsQueryRequest.java 类描述: 创建人: GaoYang 创建时间: 2017/12/18 17:12 版本: v1.0
 */
@Data
public class SmsQueryRequest {
    private String namespace;
    private Long startTime;
    private Long endTime;
    private Integer pageNo;
    private Integer pageSize;
    private String addresses;
    private String bizType;
    private String firstSys;
    private String secondSys;
    private String message;
    private String time;
}