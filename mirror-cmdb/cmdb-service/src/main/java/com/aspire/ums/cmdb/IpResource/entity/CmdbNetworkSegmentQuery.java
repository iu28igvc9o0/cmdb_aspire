package com.aspire.ums.cmdb.IpResource.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 网段查询参数
 *
 * @Author: fanshenquan
 * @Datetime: 2020/6/16 18:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CmdbNetworkSegmentQuery implements Serializable {
    private static final long serialVersionUID = -3537550707303306353L;
    // 所属资源池
    private String idcVal;
    // 网段类型
    private String segmentType;
    // 网段子类
    private String segmentSubType;
    // 分配一级业务线
    private String firstBusiness;
    // 独立业务线
    private String aloneBusiness;
    // 网段地址
    private String segmentAddress;
    // 数量（台数）
    private Integer deviceCount;

    private int pageNo;
    private int pageSize;

}
