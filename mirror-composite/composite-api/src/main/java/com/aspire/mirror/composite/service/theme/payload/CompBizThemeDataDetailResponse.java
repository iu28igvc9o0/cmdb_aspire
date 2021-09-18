package com.aspire.mirror.composite.service.theme.payload;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.theme.payload
 * 类名称:    CompBizThemeDataDetailResponse.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2018/10/31 19:37
 * 版本:      v1.0
 */
@Data
@ToString
public class CompBizThemeDataDetailResponse implements Serializable {
    private Map<String, Object> resMap;

    private Boolean success;

    private String message;

    private String noCatchMsg;
}
