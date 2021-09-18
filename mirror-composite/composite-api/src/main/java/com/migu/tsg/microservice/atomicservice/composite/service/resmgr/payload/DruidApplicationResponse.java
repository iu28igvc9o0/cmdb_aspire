package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
* Druid接口 /applications的GET方法响应类
* Project Name:composite-api
* File Name:DruidApplicationResponse.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload
* ClassName: DruidApplicationResponse <br/>
* date: 2017年9月20日 上午9:14:41 <br/>
* Druid接口 /applications的响应类
* @author weishuai
* @version 
* @since JDK 1.8
*/
@Data    
@NoArgsConstructor
@AllArgsConstructor
public class DruidApplicationResponse {
    private String unique_name;
    private String region;
    private String created_by;
    private JSONArray services;
    private String current_status;
    
    private String image_name;
    private String image_tag;
    private JSONObject custom_instance_size;
    private String knamespace;
}
