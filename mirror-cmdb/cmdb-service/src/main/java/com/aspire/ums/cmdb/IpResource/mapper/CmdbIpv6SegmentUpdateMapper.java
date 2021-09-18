package com.aspire.ums.cmdb.IpResource.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author fanwenhui
 * @date 2020-12-17 11:51
 * @description
 */
public interface CmdbIpv6SegmentUpdateMapper {

    Integer batchUpdateIpv6Segment(@Param("tableName") String tableName,
                                    @Param("address") String address,
                                    @Param("idcType") String idcType,
                                    @Param("instanceData") Map<String, Object> instanceData);
}
