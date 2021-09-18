package com.aspire.mirror.theme.api.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

/**
 * 主题数据详情
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.theme.api.dto
 * 类名称:    BizThemeDataDetailResponse.java
 * 类描述:    主题数据详情
 * 创建人:    JinSu
 * 创建时间:  2018/10/30 14:50
 * 版本:      v1.0
 */
@Data
@ToString
public class BizThemeDataDetailResponse implements Serializable {
    private static final long serialVersionUID = -3545820981334683038L;
    private Map<String, Object> resMap;

    private Boolean success;

    private String message;

    private String noCatchMsg;
}
