package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 获取应用程序元数据响应类
 * Project Name:composite-api
 * File Name:AppMetaDataResponse.java
 * Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload
 * ClassName: AppMetaDataResponse <br/>
 * date: 2017年9月20日 下午7:45:34 <br/>
 * 获取应用程序元数据响应类
 *
 * @author weishuai
 * @since JDK 1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppMetaDataResponse {
    private String unique_name;
    private String region_id;
    private String namespace;
    private String privilege;
    private String created_by;
    private String app_name;
    private List<String> resource_actions;
}
