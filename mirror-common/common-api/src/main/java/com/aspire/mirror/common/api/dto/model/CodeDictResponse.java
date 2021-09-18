package com.aspire.mirror.common.api.dto.model;

import lombok.Data;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.common.api.dto.model
 * 类名称:    CodeDictResponse.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/6/26 16:35
 * 版本:      v1.0
 */
@Data
public class CodeDictResponse {
    private String codeType;


    private String key;


    private String value;


    private String codeDesc;


    private String validFlag;


    private Integer orderId;

    private Integer parentId;

    private Integer id;
}
