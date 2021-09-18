package com.aspire.ums.cmdb.sync.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述：网管同步的独立业务线
 * 
 * @author
 * @date 2020-05-20 09:16:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbBusinessLine {

    /**
     * 主键id
     */
    private String id;

    /**
     * 业务名称.
     */
    private String businessName;

    /**
     * 一级业务线名称.
     */
    private String parentBusinessName;

    /**
     * 
     */
    private String parentId;

    /**
     * 业务group
     */
    private String businessGroup;

    /**
     * 业务code
     */
    private String businessCode;

    /**
     * 是否已删除
     */
    private String delFlag;

    /**
     * 是否禁用
     */
    private String disabled;

    /**
     * 一级业务线code
     */
    private String parentCode;
}
