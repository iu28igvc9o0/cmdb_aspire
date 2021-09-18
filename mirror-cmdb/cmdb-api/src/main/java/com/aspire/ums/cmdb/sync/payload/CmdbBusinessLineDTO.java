package com.aspire.ums.cmdb.sync.payload;

import java.io.Serializable;

import lombok.Data;

/**
 * 独立业务线DTO--对应cmdb_BusinessAttach
 *
 * @author jiangxuwen
 * @date 2020/5/13 10:57
 */
@Data
public class CmdbBusinessLineDTO implements Serializable {

    private static final long serialVersionUID = -4036759288747579487L;

    private String id;

    private String businessName;

    private String parentBusinessName;

    private String parentId;

    private String businessGroup;

    private String businessCode;

    private String delFlag;

    private String disabled;

    private String parentCode;
}
