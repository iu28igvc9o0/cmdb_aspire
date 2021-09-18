package com.aspire.ums.cmdb.IpResource.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/6/22 17:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SegmentIpInfoQuery {
    // 所属资源池
    private String idcVal;
    // 网段类型
    private String segmentType;
    // 网段子类
    private String segmentSubType;
    // 用途分类
    private String ipUseClass;
    // 用途子类
    private String ipUseSubClass;
    // 一级业务线
    private String firstBusiness;
    // 独立业务线
    private String aloneBusiness;
    // 网段地址
    private String segmentAddress;
    // 分配状态
    private String assignStatus;
    // IP
    private String[] ipArr;
    // IP类型
    private String ipType;
    // 页码
    private int pageNo;
    // 行数
    private int pageSize;
}
