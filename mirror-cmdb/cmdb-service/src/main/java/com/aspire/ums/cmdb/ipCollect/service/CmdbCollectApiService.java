package com.aspire.ums.cmdb.ipCollect.service;

import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceDeleteRequest;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceCreateRequest;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceUpdateRequest;

/**
 * 接收自动化推送接口逻辑
 */
public interface CmdbCollectApiService {

    /**
     * 新增
     *
     * @param request
     */
    boolean instanceCreate(InstanceCreateRequest request);

    /**
     * 修改
     *
     * @param request
     */
    boolean instanceUpdate(InstanceUpdateRequest request);

    /**
     * 删除
     *
     * @param request
     */
    boolean instanceDelete(InstanceDeleteRequest request);

}
