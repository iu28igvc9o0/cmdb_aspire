package com.aspire.mirror.composite.service.scada.payload;

import lombok.Data;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.scada.payload
 * 类名称:    SillVO.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/10/26 16:40
 * 版本:      v1.0
 */
@Data
public class SillVO {
    private String expressionSymbol;
    private String expressionValue;
    private String color;
}
