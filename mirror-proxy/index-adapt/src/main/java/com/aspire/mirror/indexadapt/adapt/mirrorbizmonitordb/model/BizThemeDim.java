package com.aspire.mirror.indexadapt.adapt.mirrorbizmonitordb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 主题维度信息
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.indexadapt.adapt.mirrorbizmonitordb.model
 * 类名称:    BizThemeDim.java
 * 类描述:    主题维度信息
 * 创建人:    JinSu
 * 创建时间:  2019/7/1 9:35
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BizThemeDim implements Serializable {
    private String dimName;

    private String dimCode;

    private String value;
}
