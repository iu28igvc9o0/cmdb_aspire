package com.aspire.mirror.indexproxy.domain;

import lombok.Data;

/**
 * 模板关联对象表
 * <p>
 * 项目名称:  mirror平台
 * 类名称:    TemplateObjectRecord
 * 类描述:    模板关联对象表
 * 创建人:    JinSu
 * 创建时间:  2020/11/16 10:44
 * 版本:      v1.0
 */
@Data
public class TemplateObjectRecord {
    /** 模版设备关系ID */
    private String templateObjectId;

    /** 模版ID */
    private String templateId;

    /** 设备ID */
    private String objectId;

    /**
     * 对象类型
     * 1：设备ID
     * 2: 业务系统
     */
    private String objectType;
}
