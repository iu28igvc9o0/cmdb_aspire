package com.aspire.ums.cmdb.ipResource.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 网段-IP查询回参实体
 *
 * @Author: fanshenquan
 * @Datetime: 2020/6/22 16:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SegmentIpInfoResp {
    // IP地址实例ID
    private String instanceId;
    // 模型ID
    private String moduleId;
    // ip
    private String ip;
    // 网段地址
    private String segmentAddress;
    // IP类型
    private String ipType;
    // 资源池
    private String idcVal;
    // 网段类型
    private String segmentType;
    // 网段子类
    private String segmentSubClass;
    // 用途分类
    private String ipUseClass;
    // 用途子类
    private String ipUseSubClass;
    // 分配一级业务
    private String firstBusinessName;
    // 分配独立业务
    private String aloneBusinessName;
}
