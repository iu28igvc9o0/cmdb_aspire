package com.aspire.ums.cmdb.IpResource.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author fanwenhui
 * @date 2020-12-17 15:03
 * @description
 */
public interface CmdbPublicSegmentUpdateMapper {

    Integer batchUpdatePublicSegment(@Param("tableName") String tableName,
                                   @Param("address") String address,
                                   @Param("idcType") String idcType,
                                   @Param("instanceData") Map<String, Object> instanceData);
}
