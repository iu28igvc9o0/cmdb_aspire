package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.json.JSONArray;

/**
* Get service's lb health info 响应类
* Project Name:composite-api
* File Name:CompServiceLbhealthinforResponse.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload
* ClassName: CompServiceLbhealthinforResponse <br/>
* date: 2017年9月30日 下午3:59:00 <br/>
* Get service's lb health info 响应类
* @author weishuai
* @version 
* @since JDK 1.8
*/
@Data  
@NoArgsConstructor
public class CompServiceLbhealthinforResponse {
    private List<Listence> listeners;
    private String id;
    private String address;
   
    @Data
    @NoArgsConstructor
    static class Listence {
        private JSONArray instances;
        private String protocol;
        private String port;
    }
}
