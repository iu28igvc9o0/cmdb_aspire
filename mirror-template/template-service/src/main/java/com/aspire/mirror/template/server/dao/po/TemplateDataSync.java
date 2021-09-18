package com.aspire.mirror.template.server.dao.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 模板同步数据Po
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.template.server.dao.po
 * 类名称:    TemplateDataSync.java
 * 类描述:    模板同步数据Po
 * 创建人:    JinSu
 * 创建时间:  2018/9/11 11:02
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
public class TemplateDataSync implements Serializable {
    private static final long serialVersionUID = 7123306671582101083L;

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

    public TemplateDataSync(String dataId, String syncDataType, String operateType) {
        this.syncDataType = syncDataType;
        this.dataId = dataId;
        this.operateType = operateType;
    }
}
