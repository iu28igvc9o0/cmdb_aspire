package com.aspire.ums.cmdb.ipCollect.mapper;

import com.aspire.ums.cmdb.ipCollect.payload.vmware.CmdbVmwareInstanceLog;
import org.springframework.stereotype.Repository;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/5/19 19:38
 */
@Repository
public interface CmdbVmwareInstanceLogMapper {
    void insert(CmdbVmwareInstanceLog entity);
}
