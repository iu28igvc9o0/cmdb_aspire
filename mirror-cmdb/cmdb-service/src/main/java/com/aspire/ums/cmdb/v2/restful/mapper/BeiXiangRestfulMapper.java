package com.aspire.ums.cmdb.v2.restful.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: BeiXiangRestfulMapper
 * Author:   hangfang
 * Date:     2020/1/3
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Mapper
public interface BeiXiangRestfulMapper {

    /**
     * 资源池数量统计
     * 1、大屏登录后的初始展示界面；
     * 2、展示中移信息当前最大的6个资源池数量统计占比；
     * @param top 展示前几
     */
    List<Map<String, Object>> countIdcTypePer(@Param("top") Integer top);

    /**
     * 资源池设备数量统计
     * 1、展示上述6个最大的资源池的设备数量统计；
     * 2、每个资源池展示X86、云主机、网络设备、安全设备4种设备的数量统计；
     * 3、并且从左到右，从大到小的方式排序；
     * @param top 展示前几
     */
    List<Map<String, Object>> countIdcDeviceNumber(@Param("top") Integer top);


    /**
     * 租户业务系统数量占比
     * 1、展示最大的6个租户业务系统数量占比数据；(一级部门且二级部门不为基础平台)
     * @param top 展示前几
     */
    List<Map<String, Object>> countDepart1BizPer(@Param("top") Integer top);

    /**
     * 业务系统个数统计
     * 1、展示最大的6个租户业务系统个数统计，并排序；
     * @param top 展示前几
     */
    List<Map<String, Object>> countDepart1BizNumber(@Param("top")Integer top);

    /**
     * 主机资源数量统计
     * 1、展示最大的6个租户，主机资源数量统计；
     * 2、具体指标包括：X86主机、云主机数量；
     */
    List<Map<String, Object>> countDepart1DeviceNumber(@Param("top")Integer top);

    /**
     * 信息港基础资源占比
     * 1、展示信息港资源池5类资源的总数及占比；
     * 2、5类资源包括：X86服务器、云主机、网络设备、安全设备、及存储设备；
     * 3、指标数据包括总数和已分配数；
     * @param idcType 资源池名称
     */
    List<Map<String, Object>> countDeviceByIdcType(@Param("idcType") String idcType);

    /**
     * TOP10租户资源数量统计
     * 1、展示TOP10租户的资源统计数量；
     * 2、具体指标包括：X86服务器台数、云主机个数、以及使用的存储设备容量；
     * 3、按照从上到下、从大到小排序；
     * 统计已分配数量(存储设备暂时不统计)
     * @param top 展示前几
     */
    List<Map<String, Object>> countDepart1Device(@Param("top")Integer top);

    /**
     * 机房资产信息统计
     * 1、展示3050机房内部四类设备的数量及占比；
     */
    List<Map<String, Object>> roomDevicePer();
}

