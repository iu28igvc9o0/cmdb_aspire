package com.aspire.mirror.composite.service.common.payload;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.common.payload
 * 类名称:    CompCodeDictResponse.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2018/11/9 16:52
 * 版本:      v1.0
 */
@Data
@ToString
public class CompCodeDictResponse implements Serializable {

    private String code;

    private String desc;

    private String result;
}
