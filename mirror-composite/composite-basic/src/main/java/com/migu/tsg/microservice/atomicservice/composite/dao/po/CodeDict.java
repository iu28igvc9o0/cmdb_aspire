package com.migu.tsg.microservice.atomicservice.composite.dao.po;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.common.server.dao.po
 * 类名称:    CodeDict.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2018/11/12 10:59
 * 版本:      v1.0
 */
@Data
@ToString
public class CodeDict implements Serializable {
    private static final long serialVersionUID = 3416525500757988776L;
    private String codeType;


    private String key;


    private String value;


    private String codeDesc;


    private String validFlag;


    private Integer orderId;

    private Integer parentId;

    private Integer id;
}
