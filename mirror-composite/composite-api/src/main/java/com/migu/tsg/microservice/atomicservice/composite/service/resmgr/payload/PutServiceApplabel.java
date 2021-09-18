package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* Update/add an application's labels 请求体
* Project Name:composite-api
* File Name:PutServiceApplabelRequest.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload
* ClassName: PutServiceApplabelRequest <br/>
* date: 2017年10月1日 下午2:18:27 <br/>
* Update/add an application's labels 请求体
* @author weishuai
* @version 
* @since JDK 1.8
*/
@Data  
@NoArgsConstructor
@AllArgsConstructor
public class PutServiceApplabel {
    
    private String key;
    private String value;
    private boolean  propagate;  

}
