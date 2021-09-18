package com.aspire.mirror.ops.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api.domain
 * 类名称:    AgentHostQueryModel
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/9/22 11:07
 * 版本:      v1.0
 */
@Data
public class AgentHostQueryModel  extends AbsPageQueryParamsAware {
    @JsonProperty("agent_ip")
    private String agentIp;

    @JsonProperty("pool")
    private String pool;

    @JsonProperty("pool_name")
    private String poolName;


//    @JsonProperty("department1")
    private String department1;

    @JsonProperty("department1_name")
    private String department1Name;

    private String department2;

    @JsonProperty("department2_name")
    private String department2Name;


    @JsonProperty("biz_system")
    private String bizSystem;

    @JsonProperty("biz_system_name")
    private String bizSystemName;

    @JsonProperty("os_type")
    private String osType;

    @JsonProperty("os_type_name")
    private String osTypeName;

    @JsonProperty("os_status")
    private String osStatus;

    @JsonProperty("os_status_name")
    private String osStatusName;


    @JsonProperty("agent_ip_list")
    private List<String> agentIpList;

    @JsonProperty("is_password_download")
    private Boolean isPasswordDownload = false;

    @JsonProperty("username_like")
    private String usernameLike;

    private String username;
}
