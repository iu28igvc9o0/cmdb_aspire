package com.aspire.ums.cmdb.ipAudit.service;

import com.aspire.ums.cmdb.ipAudit.entity.IpAuditSurvivingUnassignIpV6;
import com.aspire.ums.cmdb.ipAudit.payload.IpAuditUpdateRequest;
import com.aspire.ums.cmdb.ipAudit.payload.UnassignIpV6Req;
import com.aspire.ums.cmdb.ipAudit.payload.UnassignIpV6Resp;

/**
 * @author fanwenhui
 * @date 2020-05-28 20:15
 * @description ip稽核-存活未分配IP（IPv6） 业务逻辑接口
 */
public interface IIpAuditSurvivingUnassignIpV6Service extends BaseIpAuditService
        <IpAuditSurvivingUnassignIpV6, UnassignIpV6Resp, UnassignIpV6Req>{

    /**
     * 更新公网IP地址库和网段库相关信息
     * @param req 更新内容
     */
    void updateIpv6Info(IpAuditUpdateRequest req);
}
