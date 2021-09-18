package com.aspire.mirror.inspection.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.inspection.server.dao.po
 * 类名称:    ReportItemExt.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/4/14 13:51
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportItemExt {
    private Long reportItemExtId;
    private Long reportItemId;
    private String deviceClass;
    private String deviceType;
    private String log;
    private String agentIp;
    private String execStatus;
    private String deviceName;
    private String bizSystem;
    private String idcType;
    private String expression;
    private String itemGroup;
    private String itemName;
    private String bizGroup;
}
