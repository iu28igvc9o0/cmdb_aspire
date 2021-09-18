package com.aspire.ums.cmdb.ipCollect.payload.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 存活IP基类.
 *
 * @Author: fanshenquan
 * @Datetime: 2020/6/5 10:19
 */
@Data
@ToString(callSuper = true)
public class BaseIpCollectEntity extends BaseCollectEntity implements Serializable {
    private static final long serialVersionUID = 6647049032649604121L;

    // 来源设备IP
    private String srcip;

    // IP地址
    private String ip;

    // IP类型
    private String iptype;

    // MAC地址
    private String mac;
}
