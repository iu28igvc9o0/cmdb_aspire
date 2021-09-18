package com.aspire.mirror.composite.service.template.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.template.payload
 * 类名称:    CompMonitorApiServerConfig.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2018/9/6 13:52
 * 版本:      v1.0
 */
@Data
public class CompMonitorApiServerConfig {
    public static final String SERVER_TYPE_ZABBIX = "ZABBIX";
    private String url;
    private String username;
    private String password;
    private String room;
    @JsonProperty("server_type")
    private String serverType;
}
