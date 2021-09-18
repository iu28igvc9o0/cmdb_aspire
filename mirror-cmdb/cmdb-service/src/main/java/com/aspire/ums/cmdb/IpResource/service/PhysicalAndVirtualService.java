package com.aspire.ums.cmdb.IpResource.service;

import com.aspire.ums.cmdb.IpResource.entity.CmdbIpAddressEntity;
import com.aspire.ums.cmdb.IpResource.entity.CmdbIpAddressUpdate;
import com.aspire.ums.cmdb.IpResource.entity.CmdbNetworkSegmentEntity;
import com.aspire.ums.cmdb.ipResource.payload.*;

import java.util.List;
import java.util.Map;

/**
 * 物理机、虚拟机资源申请表
 *
 * @Author: fanshenquan
 * @Datetime: 2020/6/16 18:06
 */
public interface PhysicalAndVirtualService {

    /**
     * 获取网段列表
     *
     * @return
     */
    List<CmdbNetworkSegmentEntity> getNetworkSegmentList(SegmentInfoParam param);

    /**
     * 获取网段列表总数
     *
     * @param param
     * @return
     */
    Integer getNetworkSegmentListCount(SegmentInfoParam param);

    /**
     * 根据资源池、网段地址获取IP列表
     *
     * @param param
     * @return
     */
    List<CmdbIpAddressEntity> getIpAddressList(IpInfoParam param);

    /**
     * 获取IP列表总数
     *
     * @param param
     * @return
     */
    Integer getIpAddressListCount(IpInfoParam param);

    /**
     * 获取自动分IP（管理IP、业务IP1、业务IP2、console IP）
     *
     * @return
     */
    Map<String, Object> ipAutoAssign(AutoAllocateIpParam param);

    /**
     * 内网IP、IPV6、公网IP列表查询
     *
     * @param param
     * @return
     */
    List<SegmentIpInfoResp> getSegmentIpList(SegmentIpInfoParam param);

    /**
     * 内网IP、IPV6、公网IP列表总数
     *
     * @param param
     * @return
     */
    Integer getSegmentIpListCount(SegmentIpInfoParam param);

    /**
     * 内网IP、公网IP、ipv6 地址库更新
     *
     * @param param
     * @return
     */
    void updateIpInfo(IpInfoUpdateParam param);

}
