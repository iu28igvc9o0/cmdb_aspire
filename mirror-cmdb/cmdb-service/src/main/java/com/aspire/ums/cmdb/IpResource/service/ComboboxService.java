package com.aspire.ums.cmdb.IpResource.service;

import java.util.List;
import java.util.Map;

/**
 * 下拉框
 *
 * @Author: fanshenquan
 * @Datetime: 2020/6/16 20:06
 */
public interface ComboboxService {

    /**
     * 资源池
     *
     * @return
     */
    List<Map<String, Object>> getIdcVal();

    /**
     * 一级业务线->独立业务线 查询
     *
     * @param financialId 一级业务线ID
     * @return
     */
    List<Map<String, Object>> getFirstBusiness(String financialId);

    /**
     * 独立业务线->独立业务子模块 查询
     *
     * @param aloneId
     * @return
     */
    List<Map<String, Object>> getAloneBusiness(String aloneId);

    /**
     * 网段类型
     *
     * @param pid
     * @return
     */
    List<Map<String, Object>> getSegmentType(String pid);

    /**
     * 设备分类->设备类型 查询
     *
     * @param pid
     * @return
     */
    List<Map<String, Object>> getDevicesClassOrType(String pid);

    /**
     * 机房位置
     *
     * @return
     */
    List<Map<String, Object>> getIdcLocationType();

    /**
     * 内网IP用途分类
     *
     * @param pid 网段子类ID
     * @return
     */
    List<Map<String, Object>> getInnerSegmentUse(String pid);

    /**
     * 内网IP用途子类
     *
     * @param pid 内网IP用途分类ID
     * @return
     */
    List<Map<String, Object>> getInnerSegmentSubUse(String pid);

    /**
     * 公网IP 用途分类->用途子类
     *
     * @param pid
     * @return
     */
    List<Map<String, Object>> getPublicSegmentUse(String pid);

    /**
     * ipv6 用途分类->用途子类
     *
     * @param pid
     * @return
     */
    List<Map<String, Object>> getIpv6SegmentUse(String pid);

    /**
     * ip类型
     *
     * @param pid
     * @return
     */
    List<Map<String, Object>> getIpTypeForAsset(String pid);

}
