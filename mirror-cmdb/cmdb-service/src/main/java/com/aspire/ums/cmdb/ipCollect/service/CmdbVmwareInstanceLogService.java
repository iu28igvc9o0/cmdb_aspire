package com.aspire.ums.cmdb.ipCollect.service;

import com.aspire.ums.cmdb.ipCollect.payload.vmware.CmdbVmwareInstanceLog;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.BaseInstanceRequest;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/5/19 19:40
 */
public interface CmdbVmwareInstanceLogService {
    void add(CmdbVmwareInstanceLog cmdbVmwareInstanceLog);

    /**
     * 保存云管请求的日志.
     *
     * @param instanceRequest 云管instance请求
     * @return
     */
    void saveInstanceLog(BaseInstanceRequest instanceRequest);
}
