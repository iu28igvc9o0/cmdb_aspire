package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 获取应用详情响应类
 * Project Name:composite-api
 * File Name:ApplicationDetailsResponse.java
 * Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload
 * ClassName: ApplicationDetailsResponse <br/>
 * date: 2017年9月20日 上午11:05:56 <br/>
 * 获取应用详情响应类
 *
 * @author weishuai
 * @since JDK 1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ApplicationDetailsResponse {
    private String uuid;
    private String type;
    private CatalogInfo catalog_info;
    private String region;
    private String namespace;
    private String current_status;
    private String privilege;
    private String created_by;
    private JSONArray services;
    private String app_name;
    private JSONArray labels;
    private List<String> resource_actions;

    @Data
    static class CatalogInfo {
        private boolean has_dashboard;
        private String dashboard_url;
        private DisplayInfo display_info;
    }

    @Data
    static class DisplayInfo {
        private JSONArray basic_info;
        private JSONArray editable_info;
    }

    public void selfAdjust() {
        if (services == null) {
            return;
        }
        // custom_instance_size
        for (int i = 0; i < services.size(); i++) {
            JSONObject serviceObj = services.getJSONObject(i);
            if (serviceObj != null && !serviceObj.containsKey("custom_instance_size")) {
                serviceObj.put("custom_instance_size", new JSONObject());
            }
        }
    }

    public void getToLowerCase() {
        if (services == null) {
            return;
        }
        // custom_instance_size
        for (int i = 0; i < services.size(); i++) {
            JSONObject serviceObj = services.getJSONObject(i);
            if (serviceObj != null && !serviceObj.containsKey("toLowerCase")) {
                serviceObj.put("toLowerCase", new JSONObject());
            }
        }
    }


}
