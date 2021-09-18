package com.aspire.mirror.template.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * 模板同步实体
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.template.api.dto
 * 类名称:    TemplateDateSyncVo.java
 * 类描述:    模板同步实体
 * 创建人:    JinSu
 * 创建时间:  2018/11/29 18:05
 * 版本:      v1.0
 */
@Data
public class TemplateDateSyncVo implements Serializable {
    /**
     * 操作类型
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String operateType;

    public static enum Operate {
        C, D, U;
    }

}
