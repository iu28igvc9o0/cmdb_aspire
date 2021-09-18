package com.aspire.ums.cmdb.IpResource.mapper;

import com.aspire.ums.cmdb.IpResource.entity.*;
import com.aspire.ums.cmdb.ipResource.payload.SegmentIpInfoResp;

import java.util.List;
import java.util.Map;

/**
 * 地址库查询
 *
 * @Author: fanshenquan
 * @Datetime: 2020/6/16 15:25
 */
public interface CmdbAddressLibraryMapper {

    /**
     * 获取网段列表
     *
     * @param query
     * @return
     */
    List<CmdbNetworkSegmentEntity> findNetworkSegment(CmdbNetworkSegmentQuery query);

    Integer findNetworkSegmentCount(CmdbNetworkSegmentQuery query);

    /**
     * 根据资源池、网段地址获取IP列表
     *
     * @param query
     * @return
     */
    List<CmdbIpAddressEntity> findIpAddress(CmdbIpAddressQuery query);

    Integer findIpAddressCount(CmdbIpAddressQuery query);

    /**
     * 内网IP、IPV6、公网IP查询
     *
     * @param query
     * @return
     */
    List<SegmentIpInfoResp> getSegmentIpList(SegmentIpInfoQuery query);

    Integer getSegmentIpListCount(SegmentIpInfoQuery query);

    /**
     * 内网IP地址库更新
     *
     * @param update
     * @return
     */
    Integer updateInnerIp(CmdbIpAddressUpdate update);

    /**
     * 公网IP地址库更新
     *
     * @param update
     * @return
     */
    Integer updatePublicIp(CmdbIpAddressUpdate update);

    /**
     * ipv6 地址库更新
     *
     * @param update
     * @return
     */
    Integer updateIpv6Ip(CmdbIpAddressUpdate update);

    /**
     * 内网网段更新
     *
     * @param map
     * @return
     */
    Integer updateInnerSegment(Map<String, Object> map);

    /**
     * IPV6网段更新
     *
     * @param map
     * @return
     */
    Integer updateIPV6Segment(Map<String, Object> map);

    /**
     * 公网网段更新
     *
     * @param map
     * @return
     */
    Integer updatePublicSegment(Map<String, Object> map);

    void delInnerIp(Map<String,String> map);
    void delPublicIp(Map<String,String> map);
    void delIpv6Ip(Map<String,String> map);

}
