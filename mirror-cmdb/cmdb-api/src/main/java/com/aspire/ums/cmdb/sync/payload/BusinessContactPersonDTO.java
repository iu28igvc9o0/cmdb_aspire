package com.aspire.ums.cmdb.sync.payload;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 移动接口人
 *
 * @author jiangxuwen
 * @date 2020/5/13 11:03
 */
@Data
public class BusinessContactPersonDTO implements Serializable {

    private static final long serialVersionUID = 4841902094120009624L;

    /** 主键id. */
    @JsonProperty("id")
    @JSONField(name = "id")
    private String id;

    /** 独立业务线id. */
    @JsonProperty("business_id")
    @JSONField(name = "business_id")
    private String businessId;

    /** 网管同步过来的是businessCode,保存的时候要根据businessCode查询businessId. */
    @JsonProperty("business_code")
    @JSONField(name = "business_code")
    private String businessCode;

    /** 独立业务. */
    @JsonProperty("business_level1")
    @JSONField(name = "business_level1")
    private String businessLevel1;

    /** 独立业务子模块. */
    @JsonProperty("business_level2")
    @JSONField(name = "business_level2")
    private String businessLevel2;

    /** 业务线联系人. */
    @JsonProperty("business_cm_contact_name")
    @JSONField(name = "business_cm_contact_name")
    private String businessCmContactName;

    /** 业务线联系电话. */
    @JsonProperty("business_cm_contact_tel")
    @JSONField(name = "business_cm_contact_tel")
    private String businessCmContactTel;

    /** 业务线联系邮箱. */
    @JsonProperty("business_cm_contact_mail")
    @JSONField(name = "business_cm_contact_mail")
    private String businessCmContactMail;

    /** 平台联系人. */
    @JsonProperty("mgr_cm_contact_name")
    @JSONField(name = "mgr_cm_contact_name")
    private String mgrCmContactName;

    /** 平台联系人电话. */
    @JsonProperty("mgr_cm_contact_tel")
    @JSONField(name = "mgr_cm_contact_tel")
    private String mgrCmContactTel;

    /** 平台联系人邮箱. */
    @JsonProperty("mgr_cm_contact_mail")
    @JSONField(name = "mgr_cm_contact_mail")
    private String mgrCmContactMail;

    /** . */
    @JsonProperty("interface_flag")
    @JSONField(name = "interface_flag")
    private String interfaceFlag;
}
