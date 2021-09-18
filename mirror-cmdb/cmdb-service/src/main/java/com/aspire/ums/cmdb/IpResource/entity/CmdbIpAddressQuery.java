package com.aspire.ums.cmdb.IpResource.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * IP地址查询条件
 *
 * @Author: fanshenquan
 * @Datetime: 2020/6/16 18:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class CmdbIpAddressQuery extends CmdbIpAddressEntity implements Serializable {
    private static final long serialVersionUID = -5811357195630945329L;
    // 网段主键
    private String segmentId;
    // 分配状态
    private String assignStatus;
    // 网段地址
    private String segmentAddress;
    // 所属资源池
    private String idcVal;

    private int pageNo;
    private int pageSize;

    public CmdbIpAddressQuery(String assignStatus, String idcVal) {
        this.assignStatus = assignStatus;
        this.idcVal = idcVal;
    }
}
