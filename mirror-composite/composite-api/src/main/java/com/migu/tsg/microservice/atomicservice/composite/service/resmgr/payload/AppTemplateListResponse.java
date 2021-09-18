package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 获取 app template 列表
 * Project Name:composite-api
 * File Name:AppTemplateListResponse.java
 * Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload
 * ClassName: AppTemplateListResponse <br/>
 * date: 2017年9月27日 下午4:46:21 <br/>
 * 获取 app template 列表
 *
 * @author weishuai
 * @since JDK 1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppTemplateListResponse {

    private String uuid;

    private String name;

    private String namespace;

    private String knamespace;

    private String description;

    private String template;

    private String created_at;

    private String updated_at;

    private String cteated_by;

    private String privilege;

    @JsonProperty("resource_actions")
    private List<String> resourceActions;
}
