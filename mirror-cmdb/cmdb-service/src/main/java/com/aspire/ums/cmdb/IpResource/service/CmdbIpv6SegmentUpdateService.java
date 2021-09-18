package com.aspire.ums.cmdb.IpResource.service;

import java.util.Map;

/**
 * @author fanwenhui
 * @date 2020-12-17 11:43
 * @description IPv6网段更新相关接口
 */
public interface CmdbIpv6SegmentUpdateService {

    Integer batchUpdateIpv6Segment(String moduleId,Map<String, Object> dataMap);
}
