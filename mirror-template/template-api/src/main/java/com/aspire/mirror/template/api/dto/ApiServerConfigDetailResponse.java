package com.aspire.mirror.template.api.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * monitor_api_server_config详情对象类
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.api.dto
 * 类名称:    ApiServerConfigDetailResponse.java
 * 类描述:    monitor_api_server_config创建响应对象
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 13:48:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiServerConfigDetailResponse implements Serializable {

    private static final long serialVersionUID = -8362964063533035051L;

    /**
     * api配置ID
     */
    @JsonProperty("api_server_id")
    private String apiServerId;

    /**
     * 服务URL地址
     */
    private String url;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 所属机房域
     */
    private String room;

} 
