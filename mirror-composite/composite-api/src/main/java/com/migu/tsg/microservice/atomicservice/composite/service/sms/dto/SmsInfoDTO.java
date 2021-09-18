package com.migu.tsg.microservice.atomicservice.composite.service.sms.dto;

import lombok.Data;

/**
 * 项目名称: 咪咕微服务运营平台 包:
 * com.migu.tsg.microservice.atomicservice.composite.service.sms 类名称:
 * SmsInfoDTO.java 类描述: 创建人: GaoYang 创建时间: 2017/12/18 16:48 版本: v1.0
 */
@Data
public class SmsInfoDTO {
    /**
     * 发送地址(手机号)
     */
    private String addresses;
    /**
     * 发送者名称
     */
    private String senderName;
    /**
     * 发送信息
     */
    private String message;
    /**
     * 业务类型
     */
    private String bizType;
    /**
     * 状态
     */
    private String status;
    /**
     * level
     */
    private String level;
    /**
     * 一级系统
     */
    private String firstSys;
    /**
     * 二级系统
     */
    private String secondarySys;
    /**
     * uuid
     */
    private String uuid;
    /**
     * 时间
     */
    private Long time;
}