package com.migu.tsg.microservice.atomicservice.composite.service.event.dto;

import lombok.Data;

/**
* TODO 一句话描述类
* Project Name:composite-api
* File Name:RequestEventInfo.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.event.dto
* ClassName: RequestEventInfo <br/>
* date: 2017年11月29日 下午10:45:08 <br/>
* TODO 详细描述这个类的功能等
* @author weishuai
* @version 
* @since JDK 1.8
*/
@Data   
public class RequestEventInfo {
    
    private String resource_type;
    private String operation;

}
