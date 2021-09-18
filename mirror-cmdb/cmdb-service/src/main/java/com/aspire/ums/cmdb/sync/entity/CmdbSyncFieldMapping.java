package com.aspire.ums.cmdb.sync.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述：
 * 
 * @author
 * @date 2020-05-19 19:25:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbSyncFieldMapping {

    /**
     * 
     */
    private String id;

    /**
     * 映射类型
     */
    private String mappingType;

    /**
     * 映射的key
     */
    private String mappingKey;

    /**
     * 映射的value
     */
    private String mappingValue;

    /**
     * 源表的字段名称
     */
    private String srcField;

    /**
     * 目标表对应的字段名称
     */
    private String destField;

    /** 模型是否需要同步到网管. */
    private String syncOsaFlag;
}
