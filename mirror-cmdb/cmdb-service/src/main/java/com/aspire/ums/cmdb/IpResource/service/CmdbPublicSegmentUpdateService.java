package com.aspire.ums.cmdb.IpResource.service;

import java.util.Map;

/**
 * @author fanwenhui
 * @date 2020-12-17 11:43
 * @description 公网网段更新相关接口
 */
public interface CmdbPublicSegmentUpdateService {

    Integer batchUpdatePublicSegment(String moduleId, Map<String, Object> dataMap);
}
