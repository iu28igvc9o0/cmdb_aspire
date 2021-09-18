package com.aspire.mirror.template.api.dto.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

//import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * monitor_api_server_config视图层对象
 * <p>
 * 项目名称: mirror平台
 * 包:      com.aspire.mirror.template.api.dto.vo
 * 类名称:   ApiServerConfigVO.java
 * 类描述:   monitor_api_server_config视图层属性，属性范围>=表结构属性.
 * 创建人:   JinSu
 * 创建时间: 2018-07-27 13:48:09
 */
@Data
//@AllArgsConstructor
@NoArgsConstructor
public class ApiServerConfigVO implements Serializable {

    private static final long serialVersionUID = -7049479853711667451L;

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

    @JsonProperty("server_type")
    private String serverType;

} 
