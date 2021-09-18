package com.migu.tsg.microservice.atomicservice.composite.service.configfile.dto;

import lombok.Data;

/**
 * Project Name:composite-api
 * File Name:ZuulConfigUpdateRequest.java
 * Package Name:com.migu.tsg.microservice.atomicservice.composite.service.configs.dto
 * ClassName: ZuulConfigUpdateRequest <br/>
 * date: 2017年10月9日 上午10:40:07 <br/>
 *
 * @author baiwp
 * @since JDK 1.6
 */
@Data
public class ZuulConfigResponse {
    // 编码
    private String code;
    // 消息
    private String message;
    // 内容
    private Object data;
}