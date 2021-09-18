package com.aspire.ums.cmdb.sync.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述：业务线移动接口人
 * 
 * @author
 * @date 2020-05-20 09:16:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbBusinessContact {

    /** 主键id. */
    private String id;

    // /** 独立业务线id. */
    // private String businessId;

    /** 网管同步过来的是businessCode,保存的时候要根据businessCode查询businessId. */
    private String businessCode;

    /** 独立业务. */
    private String businessLevel1;

    /** 独立业务子模块. */
    private String businessLevel2;

    /** 业务线联系人. */
    private String businessCmContactName;

    /** 业务线联系电话. */
    private String businessCmContactTel;

    /** 业务线联系邮箱. */
    private String businessCmContactMail;

    /** 平台联系人. */
    private String mgrCmContactName;

    /** 平台联系人电话. */
    private String mgrCmContactTel;

    /** 平台联系人邮箱. */
    private String mgrCmContactMail;

    /** . */
    private String interfaceFlag;
}
