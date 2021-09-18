package com.aspire.ums.cmdb.IpResource.service;

import java.util.Map;

/**
 * @author fanwenhui
 * @date 2020-12-14 17:25
 * @description 内网IP网段更新相关接口
 */
public interface CmdbInnerSegmentUpdateService {

    Integer batchUpdateInnerSegment(String moduleId,Map<String, Object> dataMap);
}
