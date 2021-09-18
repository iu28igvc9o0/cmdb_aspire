package com.aspire.mirror.common.api;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 字典对象返回
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.common.api
 * 类名称:    DictResponse.java
 * 类描述:    字典对象返回
 * 创建人:    JinSu
 * 创建时间:  2018/11/9 17:44
 * 版本:      v1.0
 */
@Data
@ToString
public class DictResponse implements Serializable {

    private String code;

    private String desc;

    private String result;
}
