package com.aspire.mirror.ops.domain;

import lombok.Data;

import java.util.Date;

/**
 * 自动化审核对象
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.domain
 * 类名称:    OpsAuditInfo
 * 类描述:    自动化审核对象
 * 创建人:    JinSu
 * 创建时间:  2020/9/15 15:07
 * 版本:      v1.0
 */
@Data
public class OpsAuditInfo {
    private String auditStatus;
    private String auditDesc;
    private String reviewer;
    private Date reviewTime;
}
