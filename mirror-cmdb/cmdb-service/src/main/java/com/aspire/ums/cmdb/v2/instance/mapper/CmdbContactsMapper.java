package com.aspire.ums.cmdb.v2.instance.mapper;

import com.aspire.ums.cmdb.instance.payload.CmdbContactsResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/6/2 15:14
 */
@Mapper
@Repository
public interface CmdbContactsMapper {

    /**
     * 根据实例ID查询接口人信息
     *
     * @param instanceId 实例ID
     * @param tableName  表名
     * @return
     */
    List<CmdbContactsResponse> findContactsById(@Param("instanceId") String instanceId,
                                                @Param("tableName") String tableName);

}
