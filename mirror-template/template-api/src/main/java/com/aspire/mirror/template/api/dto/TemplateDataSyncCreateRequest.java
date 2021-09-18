package com.aspire.mirror.template.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 模板数据同步创建
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.template.api.dto
 * 类名称:    TemplateDataSyncCreateRequest.java
 * 类描述:    模板数据同步创建
 * 创建人:    JinSu
 * 创建时间:  2018/9/10 18:03
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
public class TemplateDataSyncCreateRequest  implements Serializable {
    private static final long serialVersionUID = -2756593969530750650L;
    /**
     * 同步数据类型
     */
    private String syncDataType;
    /**
     * 数据ID
     */
    private String dataId;
}
