package com.aspire.ums.cmdb.ipCollect.payload.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 存活IP实体类-网络设备
 *
 * @Author: fanshenquan
 * @Datetime: 2020/5/18 18:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class CmdbIpAddressPoolEntity extends BaseIpCollectEntity implements Serializable {

    private static final long serialVersionUID = -2321283026431725801L;

    // 网关设备IP
    private String gateway;
}
