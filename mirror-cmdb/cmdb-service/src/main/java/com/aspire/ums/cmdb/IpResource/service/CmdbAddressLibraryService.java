package com.aspire.ums.cmdb.IpResource.service;

import java.util.Map;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/6/30 16:03
 */
public interface CmdbAddressLibraryService {
    /**
     * 内网网段更新
     *
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

    /**
     * 根据网段删除对应的IP数据 （逻辑删除）
     * @param map
     */
    void delIpBySegment(Map<String,String> map);
}
