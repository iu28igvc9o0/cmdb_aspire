package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* Get Service Logs Response
* Project Name:composite-api
* File Name:ServiceLogResponse.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload
* ClassName: ServiceLogResponse <br/>
* date: 2017年10月1日 下午4:35:23 <br/>
* Get Service Logs Response
* @author weishuai
* @version 
* @since JDK 1.8
*/
@Data 
@NoArgsConstructor
@AllArgsConstructor
public class ServiceLogResponse {
    
       private String total_items;
       private List<Logs> logs;
       private String total_page;

}
