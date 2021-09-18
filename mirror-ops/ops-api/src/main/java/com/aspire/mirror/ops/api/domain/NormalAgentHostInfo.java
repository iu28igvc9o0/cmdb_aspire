package com.aspire.mirror.ops.api.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api.domain
 * 类名称:    NormalAgentHostInfo
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/9/22 10:57
 * 版本:      v1.0
 */
@Data
public class NormalAgentHostInfo {
    @JsonProperty("pool")
    private String			pool;

    @JsonProperty("proxy_id")
    private Long			proxyId;

//    @JsonProperty("agent_ip")
//    private String			agentIp;

    @Excel(name = "资源池", width = 30)
    @JsonProperty("pool_name")
    private String poolName;

    @Excel(name = "设备IP", width = 20)
    @JsonProperty("agent_ip")
    private String			agentIp;

    private String department1;

    private String department2;

    @Excel(name = "一级部门", width = 20)
    @JsonProperty("department1_name")
    private String department1Name;

    @Excel(name = "二级部门", width = 20)
    @JsonProperty("department2_name")
    private String department2Name;

    @JsonProperty("biz_system")
    private String bizSystem;

    @Excel(name = "业务系统", width = 20)
    @JsonProperty("biz_system_name")
    private String bizSystemName;

    @JsonProperty("os_type")
    private String osType;

    @Excel(name = "操作系统", width = 20)
    @JsonProperty("os_type_name")
    private String osTypeName;

    @JsonProperty("os_status")
    private String osStatus;

    @JsonProperty("os_status_name")
    private String osStatusName;

    @JsonProperty("device_name")
    private String deviceName;


    @JsonProperty("device_class")
    private String deviceClass;

    @JsonProperty("device_type")
    private String deviceType;

    @Excel(name = "设备大类", width = 20)
    @JsonProperty("device_class_name")
    private String deviceClassName;

    @Excel(name = "设备小类", width = 20)
    @JsonProperty("device_type_name")
    private String deviceTypeName;

    private String status;

    @JsonProperty("room_id")
    private String roomId;

    @Excel(name = "机房名称", width = 20)
    @JsonProperty("room_name")
    private String roomName;

    @JsonProperty("biz_employee")
    private String bizEmployee;

}
