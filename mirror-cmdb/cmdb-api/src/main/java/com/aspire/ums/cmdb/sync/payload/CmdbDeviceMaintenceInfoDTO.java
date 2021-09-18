package com.aspire.ums.cmdb.sync.payload;

import java.io.Serializable;

import lombok.Data;

/**
 * 硬件维保
 *
 * @author jiangxuwen
 * @date 2020/5/13 11:06
 */
@Data
public class CmdbDeviceMaintenceInfoDTO implements Serializable {

    private static final long serialVersionUID = -6140793398805655101L;

    private String id;

    private String deviceId;

    /** 出保时间. */
    private String maintenceEndTime;

    /** 本期维保开始时间. */
    private String maintenceTermStartDate;

    /** 本期维保结束时间. */
    private String maintenceTermEndDate;

    /** 实际购买维保类型. */
    private String maintencePurchaseType;

    /** 原厂维保购买必要性说明. */
    private String maintencePurchaseDesc;

    /** 是否购买维保. */
    private String maintencePurchaseFlag;

    /** 是否需原厂维保. */
    private String venderMaintenceFlag;

    /** 维保管理员. */
    private String maintenceAdmin;

    /** 维保厂家名称. */
    private String maintenceFactoryName;

    /** 维保供应商联系方式. */
    private String maintenceProviderContact;

    /** 维保厂家联系方式. */
    private String maintenceFactoryContact;
}
