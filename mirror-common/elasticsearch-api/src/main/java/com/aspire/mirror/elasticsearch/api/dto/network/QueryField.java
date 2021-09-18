package com.aspire.mirror.elasticsearch.api.dto.network;

import lombok.Data;

/**
 * @author hewang
 * @version 1.0
 * @date 2021/3/5 10:48
 */
@Data
public class QueryField {

    /**
     *  条件字段名称
     */
    private String fieldName;

    /**
     *  条件字段类型
     */
    private String fieldType;

    /**
     *  条件字段值
     */
    private String fieldValue;
}
