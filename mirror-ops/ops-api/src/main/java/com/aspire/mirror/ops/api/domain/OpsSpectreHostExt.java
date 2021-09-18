package com.aspire.mirror.ops.api.domain;

import lombok.Data;

/**
 * 设备关联cmdb扩展
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api.domain
 * 类名称:    OpsSpectreHostExt
 * 类描述:    设备关联cmdb扩展
 * 创建人:    JinSu
 * 创建时间:  2020/9/21 16:16
 * 版本:      v1.0
 */
@Data
public class OpsSpectreHostExt {
    private Long proxyId;
    private String agentIp;
    private String pool;
    private String poolName;
    private String department1;
    private String department2;
    private String bizSystem;
    private String osType;
    private String osTypeName;
    private String osStatus;
    private String osStatusName;
    private String department1Name;
    private String department2Name;
    private String bizSystemName;
    private String deviceName;
    private String deviceClass;
    private String deviceType;
    private String deviceClassName;
    private String deviceTypeName;
    private String roomId;
    private String roomName;
    private String bizEmployee;
}
