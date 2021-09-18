package com.aspire.ums.cmdb.openstack.service;

import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceCreateRequest;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceDeleteRequest;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceRelationCreateRequest;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceRelationDeleteRequest;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceUpdateRequest;

/**
 * 同步云管vmware Instance业务逻辑实现.
 *
 * @author jiangxuwen
 * @date 2020/3/11 21:01
 */
public interface IOpenstackInstanceService {

    /**
     * 创建实例.
     * 
     * @param createRequest
     *            vmware请求体
     * @return JsonResultInfo
     */
    void instanceCreate(InstanceCreateRequest createRequest);

    /**
     * 修改实例.
     *
     * @param updateRequest
     *            vmware请求体
     * @return JsonResultInfo
     */
    void instanceUpdate(InstanceUpdateRequest updateRequest);

    /**
     * 删除实例.
     *
     * @param deleteRequest
     *            vmware请求体
     * @return JsonResultInfo
     */
    void instanceDelete(InstanceDeleteRequest deleteRequest);

    /**
     * 创建实例关联关系.
     *
     * @param createRequest
     *            vmware请求体
     * @return JsonResultInfo
     */
    void instanceRelationCreate(InstanceRelationCreateRequest createRequest);

    /**
     * 删除实例关联关系.
     *
     * @param createRequest
     *            vmware请求体
     * @return JsonResultInfo
     */
    void instanceRelationDelete(InstanceRelationDeleteRequest createRequest);
}
