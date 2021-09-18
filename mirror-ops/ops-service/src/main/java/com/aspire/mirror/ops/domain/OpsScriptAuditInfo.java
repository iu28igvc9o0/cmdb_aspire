package com.aspire.mirror.ops.domain;

import lombok.Data;

/**
 * 自动化脚本编辑机审核对象
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.domain
 * 类名称:    OpsScriptAuditInfo
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/9/15 15:09
 * 版本:      v1.0
 */
@Data
public class OpsScriptAuditInfo extends OpsAuditInfo{
    private Long scriptId;
}
