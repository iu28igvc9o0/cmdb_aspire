package com.aspire.ums.cmdb.report.playload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.report.entity
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/6/14 16:23
 * 版本: v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportReq {
    private String bizSystem;
    private String idcType;
    private String department1;
    private String department2;
}
