package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 获取应用标签响应类
* Project Name:composite-api
* File Name:ServiceApplicationLabelResponse.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload
* ClassName: ServiceApplicationLabelResponse <br/>
* date: 2017年10月1日 上午10:19:15 <br/>
* 获取应用标签响应类
* @author weishuai
* @version 
* @since JDK 1.8
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceApplicationLabelResponse {
     private String key;
     private String value;
     private boolean editable;
     private boolean propagate;
}
