package com.aspire.mirror.template.server.dao.po;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * monitor_api_server_config持久对象类
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.server.dao.po
 * 类名称:    ApiServerConfig.java
 * 类描述:    monitor_api_server_config持久类，定义与表字段对应的属性
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 12:14:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiServerConfig implements Serializable {

    private static final long serialVersionUID = -5392875096447333480L;

    // api配置ID
    private String apiServerId;

    // 服务URL地址
    private String url;

    // 用户名
    private String username;

    // 密码
    private String password;

    // 所属机房域
    private String room;

    private String serverType;

} 
