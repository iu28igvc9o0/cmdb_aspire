package com.aspire.ums.cmdb.sync.payload;

import java.io.Serializable;

import lombok.Data;

/**
 * 业务线支撑方联系人(代维厂家)
 *
 * @author jiangxuwen
 * @date 2020/5/13 11:04
 */
@Data
public class BusinessMaintenceContactDTO implements Serializable {

    private static final long serialVersionUID = 1305160096530727828L;

    private String id;

    private String businessId;

    private String businessLevel1;

    private String businessLevel2;

    private String businessVendor;

    private String businessContact;

    private String businessTel;

    private String businessMail;

    private String concatType;

    private String concatGroup;
}
