package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建应用响应类
 * Project Name:composite-api
 * File Name:CompApplicationCreateResponse.java
 * Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload
 * ClassName: CompApplicationCreateResponse <br/>
 * date: 2017年9月19日 上午9:43:30 <br/>
 * 创建应用响应类
 *
 * @author weishuai
 * @since JDK 1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompApplicationCreateResponse {

    private String uuid;
    private String app_name;
}
