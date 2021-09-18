package com.aspire.mirror.template.api.dto.model;

import lombok.Data;

/**
 * 模板数据同步
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.template.api.dto.model
 * 类名称:    TemplateDataSyncDTO.java
 * 类描述:    模板数据同步
 * 创建人:    JinSu
 * 创建时间:  2018/9/10 18:29
 * 版本:      v1.0
 */
@Data
public class TemplateDataSyncDTO {
    /**
     * 同步数据类型
     */
    private String syncDataType;
    /**
     * 数据ID
     */
    private String dataId;
    /**
     * 同步ID
     */
    private int syncId;
    /**
     * 同步类型
     */
    private String operateType;
}
