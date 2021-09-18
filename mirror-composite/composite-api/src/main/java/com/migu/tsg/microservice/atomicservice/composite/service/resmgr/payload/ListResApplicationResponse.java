package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
* 应用列表查询响应model
* Project Name:composite-api
* File Name:ListResApplicationResponse.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload
* ClassName: ListResApplicationResponse <br/>
* date: 2017年9月18日 下午9:07:41 <br/>
* 应用列表查询响应model
* @author weishuai
* @version 
* @since JDK 1.8
*/
@Data    
@NoArgsConstructor
@AllArgsConstructor
public class ListResApplicationResponse {
    private String type;
    private String uuid;
    private String region;
    private String namespace;
    private String current_status;
    private String created_by;
    private JSONArray services;
    private String app_name;
    private JSONArray labels;
    private List<String> resource_actions;
    private String region_name;
    
    private String image_name;
    private String image_tag;
    private JSONObject custom_instance_size;
    private String knamespace;
    
}
