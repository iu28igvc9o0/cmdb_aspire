package com.aspire.mirror.template.api.dto.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * monitor_api_server_config持久对象类
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.api.dto.model
 * 类名称:    ApiServerConfigDTO.java
 * 类描述:    monitor_api_server_config业务类，定义与表字段对应的属性
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 13:48:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class ApiServerConfigDTO implements Serializable {

    private static final long serialVersionUID = -4689109162254627774L;

    /**
     * api配置ID
     */
    @ApiModelProperty(value = "api配置ID")
    private String apiServerId;

    /**
     * 服务URL地址
     */
    @ApiModelProperty(value = "服务URL地址")
    private String url;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 所属机房域
     */
    @ApiModelProperty(value = "所属机房域")
    private String room;

    @ApiModelProperty(value = "服务类型")
    private String serverType;
} 
